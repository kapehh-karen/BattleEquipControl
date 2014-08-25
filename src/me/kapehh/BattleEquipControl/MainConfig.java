package me.kapehh.BattleEquipControl;

import me.kapehh.BattleEquipControl.core.ArmorConfig;
import me.kapehh.BattleEquipControl.sets.ArmorSet;
import me.kapehh.main.pluginmanager.config.EventPluginConfig;
import me.kapehh.main.pluginmanager.config.EventType;
import me.kapehh.main.pluginmanager.config.PluginConfig;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

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

    Main main;
    PluginConfig pluginConfig;

    public MainConfig(Main main, PluginConfig pluginConfig) {
        this.main = main;
        this.pluginConfig = pluginConfig;
    }

    @EventPluginConfig(EventType.LOAD)
    public void onLoad() {
        FileConfiguration cfg = pluginConfig.getConfig();
        ArmorConfig armorConfig = main.getArmorConfig();

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

        for (Material material : allArmors) {
            armorConfig.addArmorSet(new ArmorSet(
                material,
                cfg.getInt("ARMOR." + material.toString() + ".max_level", 1),
                cfg.getString("ARMOR." + material.toString() + ".eval_level_strange", "0")
            ));
        }

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
    }
}
