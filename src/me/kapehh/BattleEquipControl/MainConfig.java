package me.kapehh.BattleEquipControl;

import me.kapehh.main.pluginmanager.config.EventPluginConfig;
import me.kapehh.main.pluginmanager.config.EventType;
import me.kapehh.main.pluginmanager.config.PluginConfig;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by Karen on 25.08.2014.
 */
public class MainConfig {
    PluginConfig pluginConfig;

    public MainConfig(PluginConfig pluginConfig) {
        this.pluginConfig = pluginConfig;
    }

    @EventPluginConfig(EventType.LOAD)
    public void onLoad() {
        FileConfiguration cfg = pluginConfig.getConfig();

        /*Material.LEATHER_HELMET
        Material.IRON_HELMET
        Material.GOLD_HELMET
        Material.DIAMOND_HELMET
        Material.CHAINMAIL_HELMET*/
        
        /*Material.LEATHER_CHESTPLATE
        Material.IRON_CHESTPLATE
        Material.GOLD_CHESTPLATE
        Material.DIAMOND_CHESTPLATE
        Material.CHAINMAIL_CHESTPLATE*/
        
        /*Material.LEATHER_LEGGINGS
        Material.IRON_LEGGINGS
        Material.GOLD_LEGGINGS
        Material.DIAMOND_LEGGINGS
        Material.CHAINMAIL_LEGGINGS*/
        
        /*Material.LEATHER_BOOTS
        Material.IRON_BOOTS
        Material.GOLD_BOOTS
        Material.DIAMOND_BOOTS
        Material.CHAINMAIL_BOOTS*/
    }
}
