package me.kapehh.BattleEquipControl;

import me.kapehh.BattleEquipControl.core.ArmorConfig;
import me.kapehh.main.pluginmanager.config.PluginConfig;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Karen on 25.08.2014.
 */
public class Main extends JavaPlugin {
    ArmorConfig armorConfig;

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("PluginManager") == null) {
            getLogger().info("PluginManager not found!!!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        armorConfig = new ArmorConfig();

        getServer().getPluginManager().registerEvents(new MainListener(this), this);

        PluginConfig pluginConfig = new PluginConfig(this);
        pluginConfig.addEventClasses(new MainConfig(this, pluginConfig));
        pluginConfig.setup();
        pluginConfig.loadData();
    }

    @Override
    public void onDisable() {

    }

    public ArmorConfig getArmorConfig() {
        return armorConfig;
    }
}
