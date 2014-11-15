package me.kapehh.BattleEquipControl;

import me.kapehh.BattleEquipControl.bukkit.EnchantmentManager;
import me.kapehh.BattleEquipControl.core.*;
import me.kapehh.main.pluginmanager.config.PluginConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Random;

/**
 * Created by Karen on 25.08.2014.
 */
public class Main extends JavaPlugin {
    ArmorConfig armorConfig = new ArmorConfig();
    WeaponConfig weaponConfig = new WeaponConfig();
    MobConfig mobConfig = new MobConfig();
    NodamageConfig nodamageConfig = new NodamageConfig();
    UpgradeConfig upgradeConfig = new UpgradeConfig();

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

        // init enchants
        //EnchantmentManager.init();
        /*ShapedRecipe recipe = new ShapedRecipe(new ItemStack(Material.WOOD_SWORD, 1));
        recipe.shape("   ", " I ", " U ");
        recipe.setIngredient('I', Material.DIAMOND_SWORD);
        recipe.setIngredient('U', Material.GOLD_INGOT);
        Bukkit.getServer().addRecipe(recipe);*/
        ShapelessRecipe recipe = new ShapelessRecipe(new ItemStack(Material.WOOD_SWORD, 1));
        /*recipe.shape("   ", " I ", " U ");
        recipe.setIngredient('I', Material.DIAMOND_SWORD);
        recipe.setIngredient('U', Material.GOLD_INGOT);*/
        recipe.addIngredient(Material.DIAMOND_SWORD);
        recipe.addIngredient(Material.GOLD_INGOT);
        Bukkit.getServer().addRecipe(recipe);
        /*ItemStack resultFurnace = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta itemMeta = resultFurnace.getItemMeta();
        itemMeta.setDisplayName("Upgrade!");
        resultFurnace.setItemMeta(itemMeta);
        FurnaceRecipe furnaceRecipe = new FurnaceRecipe(resultFurnace, Material.DIAMOND_SWORD);
        Bukkit.addRecipe(furnaceRecipe);*/
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
    public MobConfig getMobConfig() {
        return mobConfig;
    }
    public NodamageConfig getNodamageConfig() {
        return nodamageConfig;
    }
    public UpgradeConfig getUpgradeConfig() {
        return upgradeConfig;
    }
}
