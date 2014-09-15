package me.kapehh.BattleEquipControl;

import me.kapehh.BattleEquipControl.core.ArmorConfig;
import me.kapehh.BattleEquipControl.core.MobConfig;
import me.kapehh.BattleEquipControl.core.WeaponConfig;
import me.kapehh.BattleEquipControl.sets.ArmorSet;
import me.kapehh.BattleEquipControl.sets.MobSet;
import me.kapehh.BattleEquipControl.sets.WeaponSet;
import me.kapehh.main.pluginmanager.config.EventPluginConfig;
import me.kapehh.main.pluginmanager.config.EventType;
import me.kapehh.main.pluginmanager.config.PluginConfig;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

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
    PluginConfig pluginConfig;

    public MainConfig(Main main, PluginConfig pluginConfig) {
        this.main = main;
        this.pluginConfig = pluginConfig;
    }

    private List<Double> evalString(String eval, int max) throws ScriptException {
        List<Double> doubles = new ArrayList<Double>();
        ScriptEngine scriptEngine = main.getScriptEngine();
        for (int i = 1; i <= max; i++) {
            scriptEngine.put("lvl", i);
            Object ret = scriptEngine.eval(eval);
            if (ret instanceof Double)
                doubles.add((Double) ret);
            else if (ret instanceof Integer)
                doubles.add((double) (Integer) ret);
            //doubles.add((Double) scriptEngine.eval(eval));
        }
        return doubles;
    }

    @EventPluginConfig(EventType.LOAD)
    public void onLoad() {
        FileConfiguration cfg = pluginConfig.getConfig();
        ArmorConfig armorConfig = main.getArmorConfig();
        WeaponConfig weaponConfig = main.getWeaponConfig();
        MobConfig mobConfig = main.getMobConfig();

        // TODO: Сделать загрузку по keySet из конфига

        Material[] allArmors = new Material[] {
            Material.LEATHER_HELMET,
            Material.IRON_HELMET,
            Material.GOLD_HELMET,
            Material.DIAMOND_HELMET,
            Material.CHAINMAIL_HELMET,
            Material.LEATHER_CHESTPLATE,
            Material.IRON_CHESTPLATE,
            Material.GOLD_CHESTPLATE,
            Material.DIAMOND_CHESTPLATE,
            Material.CHAINMAIL_CHESTPLATE,
            Material.LEATHER_LEGGINGS,
            Material.IRON_LEGGINGS,
            Material.GOLD_LEGGINGS,
            Material.DIAMOND_LEGGINGS,
            Material.CHAINMAIL_LEGGINGS,
            Material.LEATHER_BOOTS,
            Material.IRON_BOOTS,
            Material.GOLD_BOOTS,
            Material.DIAMOND_BOOTS,
            Material.CHAINMAIL_BOOTS
        };

        Material[] allWeapons = new Material[] {
            Material.BOW,
            Material.WOOD_SWORD,
            Material.STONE_SWORD,
            Material.IRON_SWORD,
            Material.GOLD_SWORD,
            Material.DIAMOND_SWORD,
            Material.WOOD_SPADE,
            Material.STONE_SPADE,
            Material.IRON_SPADE,
            Material.GOLD_SPADE,
            Material.DIAMOND_SPADE,
            Material.WOOD_AXE,
            Material.STONE_AXE,
            Material.IRON_AXE,
            Material.GOLD_AXE,
            Material.DIAMOND_AXE
        };

        EntityType[] entityTypes = new EntityType[] {
            EntityType.PLAYER,
            EntityType.WOLF,
            EntityType.MAGMA_CUBE,
            EntityType.BLAZE,
            EntityType.WITCH,
            EntityType.CAVE_SPIDER,
            EntityType.ENDERMAN,
            EntityType.PIG_ZOMBIE,
            EntityType.GHAST,
            EntityType.SLIME,
            EntityType.ZOMBIE,
            EntityType.SPIDER,
            EntityType.SKELETON,
            EntityType.CREEPER,
            EntityType.ENDER_DRAGON
        };

        main.getLogger().info("Start read config!");

        for (Material material : allArmors) {
            int max = cfg.getInt("ARMOR." + material.toString() + ".max_level", 1);
            String evalProtect = cfg.getString("ARMOR." + material.toString() + ".eval_level_strong", "0");
            String evalExp = cfg.getString("ARMOR." + material.toString() + ".eval_exp", "0");
            try {
                ArmorSet armorSet = new ArmorSet(
                    material,
                    max,
                    evalString(evalProtect, max),
                    evalString(evalExp, max)
                );
                armorConfig.addArmorSet(armorSet);
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }

        for (Material material : allWeapons) {
            int max = cfg.getInt("WEAPONS." + material.toString() + ".max_level", 1);
            String evalDamage = cfg.getString("WEAPONS." + material.toString() + ".eval_level_damage", "0");
            String evalExp = cfg.getString("WEAPONS." + material.toString() + ".eval_exp", "0");
            try {
                WeaponSet weaponSet = new WeaponSet(
                    material,
                    max,
                    evalString(evalDamage, max),
                    evalString(evalExp, max)
                );
                weaponConfig.addWeaponSet(weaponSet);
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }

        for (EntityType entityType : entityTypes) {
            int exp = cfg.getInt("MOBS." + entityType.toString() + ".exp", 0);
            mobConfig.addMobSet(new MobSet(entityType, exp));
        }

        main.getLogger().info("Finish read!");
    }
}
