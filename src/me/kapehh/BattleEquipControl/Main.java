package me.kapehh.BattleEquipControl;

import me.kapehh.BattleEquipControl.core.ArmorConfig;
import me.kapehh.BattleEquipControl.core.WeaponConfig;
import me.kapehh.main.pluginmanager.config.PluginConfig;
import org.bukkit.plugin.java.JavaPlugin;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Created by Karen on 25.08.2014.
 */
public class Main extends JavaPlugin {
    ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");

    ArmorConfig armorConfig = new ArmorConfig();
    WeaponConfig weaponConfig = new WeaponConfig();

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("PluginManager") == null) {
            getLogger().info("PluginManager not found!!!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new MainListener(this), this);
        getCommand("battleequip").setExecutor(new MainCommand());

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

    public WeaponConfig getWeaponConfig() {
        return weaponConfig;
    }

    public ScriptEngine getScriptEngine() {
        return scriptEngine;
    }
}
