package me.kapehh.BattleEquipControl;

import me.kapehh.BattleEquipControl.core.ArmorConfig;
import me.kapehh.BattleEquipControl.core.WeaponConfig;
import me.kapehh.BattleEquipControl.sets.ArmorSet;
import me.kapehh.BattleEquipControl.sets.WeaponSet;
import me.kapehh.main.pluginmanager.config.EventPluginConfig;
import me.kapehh.main.pluginmanager.config.EventType;
import me.kapehh.main.pluginmanager.config.PluginConfig;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

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
            doubles.add((Double) scriptEngine.eval(eval));
        }
        return doubles;
    }

    @EventPluginConfig(EventType.LOAD)
    public void onLoad() {
        FileConfiguration cfg = pluginConfig.getConfig();
        ArmorConfig armorConfig = main.getArmorConfig();
        WeaponConfig weaponConfig = main.getWeaponConfig();

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
            Material.WOOD_AXE,
            Material.STONE_AXE,
            Material.IRON_AXE,
            Material.GOLD_AXE,
            Material.DIAMOND_AXE,
            Material.WOOD_PICKAXE,
            Material.STONE_PICKAXE,
            Material.IRON_PICKAXE,
            Material.GOLD_PICKAXE,
            Material.DIAMOND_PICKAXE
        };

        main.getLogger().info("Start read config!");

        for (Material material : allArmors) {
            int max = cfg.getInt("ARMOR." + material.toString() + ".max_level", 1);
            String eval = cfg.getString("ARMOR." + material.toString() + ".eval_level_strange", "0");
            try {
                ArmorSet armorSet = new ArmorSet(
                    material,
                    max,
                    evalString(eval, max)
                );
                armorConfig.addArmorSet(armorSet);
                main.getLogger().info(armorSet.toString());
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }

        for (Material material : allWeapons) {
            int max = cfg.getInt("WEAPONS." + material.toString() + ".max_level", 1);
            String eval = cfg.getString("WEAPONS." + material.toString() + ".eval_level_damage", "0");
            try {
                WeaponSet weaponSet = new WeaponSet(
                    material,
                    max,
                    evalString(eval, max)
                );
                weaponConfig.addWeaponSet(weaponSet);
                main.getLogger().info(weaponSet.toString());
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }

        main.getLogger().info("Finish read!");
    }
}
