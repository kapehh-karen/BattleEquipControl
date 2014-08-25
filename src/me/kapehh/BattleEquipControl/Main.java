package me.kapehh.BattleEquipControl;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Karen on 25.08.2014.
 */
public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("PluginManager") == null) {
            getLogger().info("PluginManager not found!!!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new MainListener(), this);
    }

    @Override
    public void onDisable() {

    }
}
