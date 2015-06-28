package me.kapehh.BattleEquipControl;

import me.kapehh.BattleEquipControl.core.*;
import me.kapehh.BattleEquipControl.sets.*;
import me.kapehh.main.pluginmanager.config.EventPluginConfig;
import me.kapehh.main.pluginmanager.config.EventType;
import me.kapehh.main.pluginmanager.config.PluginConfig;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.material.MaterialData;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Karen on 25.08.2014.
 */
public class MainConfig {
    /*Material.LEATHER_HELMET
    Material.IRON_HELMET
    Material.GOLD_HELMET
    Material.DIAMOND_HELMET
    Material.CHAINMAIL_HELMET
    Material.LEATHER_CHESTPLATE
    Material.IRON_CHESTPLATE
    Material.GOLD_CHESTPLATE
    Material.DIAMOND_CHESTPLATE
    Material.CHAINMAIL_CHESTPLATE
    Material.LEATHER_LEGGINGS
    Material.IRON_LEGGINGS
    Material.GOLD_LEGGINGS
    Material.DIAMOND_LEGGINGS
    Material.CHAINMAIL_LEGGINGS
    Material.LEATHER_BOOTS
    Material.IRON_BOOTS
    Material.GOLD_BOOTS
    Material.DIAMOND_BOOTS
    Material.CHAINMAIL_BOOTS*/

    /*Material.BOW

    Material.WOOD_SWORD
    Material.STONE_SWORD
    Material.IRON_SWORD
    Material.GOLD_SWORD
    Material.DIAMOND_SWORD

    Material.WOOD_SPADE,
    Material.STONE_SPADE,
    Material.IRON_SPADE,
    Material.GOLD_SPADE,
    Material.DIAMOND_SPADE,

    Material.WOOD_AXE
    Material.STONE_AXE
    Material.IRON_AXE
    Material.GOLD_AXE
    Material.DIAMOND_AXE

    Material.WOOD_PICKAXE
    Material.STONE_PICKAXE
    Material.IRON_PICKAXE
    Material.GOLD_PICKAXE
    Material.DIAMOND_PICKAXE*/

    Main main;
    ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");

    public MainConfig(Main main) {
        this.main = main;
    }

    private List<Double> evalString(String eval, int max) throws ScriptException {
        List<Double> doubles = new ArrayList<Double>();
        for (int i = 1; i <= max; i++) {
            scriptEngine.put("lvl", i);
            Object ret = scriptEngine.eval(eval);
            if (ret instanceof Double)
                doubles.add((Double) ret);
            else if (ret instanceof Integer)
                doubles.add((double) (Integer) ret);
        }
        return doubles;
    }

    @Deprecated
    @EventPluginConfig(EventType.LOAD)
    public void onLoad(FileConfiguration cfg) {
        ArmorConfig armorConfig = main.getArmorConfig();
        WeaponConfig weaponConfig = main.getWeaponConfig();
        MobConfig mobConfig = main.getMobConfig();
        NodamageConfig nodamageConfig = main.getNodamageConfig();
        UpgradeConfig upgradeConfig = main.getUpgradeConfig();
        EnchantGroupConfig enchantGroupConfig = main.getEnchantGroupConfig();
        ConvertConfig convertConfig = main.getConvertConfig();
        ArrayList<Material> unbrokenList = main.getUnbrokenList();

        // clear
        armorConfig.armorSets.clear();
        weaponConfig.weaponSets.clear();
        mobConfig.mobSetList.clear();
        nodamageConfig.materials.clear();
        upgradeConfig.upgradeSetList.clear();
        enchantGroupConfig.enchantGroupSets.clear();
        unbrokenList.clear();
        // end clear

        main.getLogger().info("Start read config!");

        convertConfig.upgradeConfig = upgradeConfig;
        convertConfig.percent_of_convert = cfg.getDouble("MAIN.percent_of_convert");

        Main.debug = cfg.getBoolean("MAIN.debug");

        List<String> unbrk = cfg.getStringList("UNBROKEN");
        for (String unbrkItem : unbrk) {
            Material material = Material.valueOf(unbrkItem);
            if (material != null) {
                unbrokenList.add(material);
            }
        }

        int maxLevel = cfg.getInt("MAIN.max_level", 1);
        int maxLevelUpgrade = cfg.getInt("MAIN.max_level_upgrade", 1);
        int singleMaxLevel = Math.max(maxLevel, maxLevelUpgrade);
        String evalExp = cfg.getString("MAIN.eval_exp", "1");
        String evalChanceFail = cfg.getString("MAIN.chance_fail_upgrade", "0");
        List<Double> listExp;
        List<Double> chanceFailUpgrade;
        try {
            listExp = evalString(evalExp, singleMaxLevel);
            chanceFailUpgrade = evalString(evalChanceFail, singleMaxLevel);
        } catch (ScriptException e) {
            e.printStackTrace();
            return;
        }

        Object section = cfg.get("ENCHANT_GROUPS");
        if (section instanceof ConfigurationSection) {
            Set<String> setEnchantGroup = ((ConfigurationSection) cfg.get("ENCHANT_GROUPS")).getKeys(false);
            for (String nameGroup : setEnchantGroup) {
                EnchantGroupSet enchantGroupSet = new EnchantGroupSet(nameGroup);
                Set<String> listEnchants = ((ConfigurationSection) cfg.get("ENCHANT_GROUPS." + nameGroup)).getKeys(false);
                for (String nameEnchant : listEnchants) {
                    Enchantment enchantment = Enchantment.getByName(nameEnchant);
                    if (enchantment != null) {
                        enchantGroupSet.addEnchant(
                            enchantment,
                            cfg.getInt("ENCHANT_GROUPS." + nameGroup + "." + nameEnchant, 1)
                        );
                    } else {
                        main.getLogger().warning("Enchantment '" + nameEnchant + "' not found!");
                    }
                }
                enchantGroupConfig.addEnchantGroupSet(enchantGroupSet);
            }
        }

        Set<String> setUpgrades = ((ConfigurationSection)cfg.get("UPGRADE")).getKeys(false);
        Set<String> setArmors = ((ConfigurationSection)cfg.get("ARMOR")).getKeys(false);
        Set<String> setWeapons = ((ConfigurationSection)cfg.get("WEAPONS")).getKeys(false);
        Set<String> setMobs = ((ConfigurationSection)cfg.get("MOBS")).getKeys(false);

        for (String key : setUpgrades) {
            int exp = cfg.getInt("UPGRADE." + key + ".exp", 0);
            byte data = (byte) cfg.getInt("UPGRADE." + key + ".data", 0);
            upgradeConfig.addUpgradeSet(
                new UpgradeSet(new MaterialData(Material.valueOf(key), data), exp, singleMaxLevel, chanceFailUpgrade)
            );
        }

        for (String key : setArmors) {
            String evalProtect = cfg.getString("ARMOR." + key + ".eval_level_strong", "0");
            String enchantGroup = cfg.getString("ARMOR." + key + ".enchant_group");
            try {
                ArmorSet armorSet = new ArmorSet(
                    Material.valueOf(key),
                    maxLevel,
                    maxLevelUpgrade,
                    enchantGroupConfig.getEnchantGroupSet(enchantGroup),
                    evalString(evalProtect, singleMaxLevel),
                    listExp
                );
                armorConfig.addArmorSet(armorSet);
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }

        for (String key : setWeapons) {
            String evalDamage = cfg.getString("WEAPONS." + key + ".eval_level_damage", "0");
            String enchantGroup = cfg.getString("WEAPONS." + key + ".enchant_group");
            try {
                WeaponSet weaponSet = new WeaponSet(
                    Material.valueOf(key),
                    maxLevel,
                    maxLevelUpgrade,
                    enchantGroupConfig.getEnchantGroupSet(enchantGroup),
                    evalString(evalDamage, singleMaxLevel),
                    listExp
                );
                weaponConfig.addWeaponSet(weaponSet);
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }

        for (String key : setMobs) {
            int exp = cfg.getInt("MOBS." + key + ".exp", 0);
            mobConfig.addMobSet(new MobSet(EntityType.valueOf(key), exp));
        }

        List<String> nodamage = cfg.getStringList("NODAMAGE");
        for (String nodmg : nodamage) {
            nodamageConfig.addMaterial(Material.valueOf(nodmg));
        }

        main.getLogger().info("Finish read!");
    }
}
