package me.kapehh.BattleEquipControl;

import me.kapehh.BattleEquipControl.bukkit.EnchantmentManager;
import me.kapehh.BattleEquipControl.core.*;
import me.kapehh.BattleEquipControl.sets.ArmorSet;
import me.kapehh.BattleEquipControl.sets.ISet;
import me.kapehh.BattleEquipControl.sets.WeaponSet;
import me.kapehh.BattleEquipControl.upgrade.UpgradeManager;
import me.kapehh.main.pluginmanager.config.PluginConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Karen on 25.08.2014.
 */
public class Main extends JavaPlugin {
    public static Material WOOD_MY_SPADE;

    UpgradeManager upgradeManager = new UpgradeManager();
    ArmorConfig armorConfig = new ArmorConfig();
    WeaponConfig weaponConfig = new WeaponConfig();
    MobConfig mobConfig = new MobConfig();
    NodamageConfig nodamageConfig = new NodamageConfig();
    UpgradeConfig upgradeConfig = new UpgradeConfig();
    EnchantGroupConfig enchantGroupConfig = new EnchantGroupConfig();
    ArrayList<Material> unbrokenList = new ArrayList<Material>();

    /*public static void main(String[] args) {
        int rnd = 35 % 2;
        System.out.println(rnd);
    }*/

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
        pluginConfig.addEventClasses(new MainConfig(this));
        pluginConfig.setup();
        pluginConfig.loadData();

        // init upgrades
        upgradeManager.init(this);

        //EnchantmentManager.init();
        /*ShapedRecipe recipe = new ShapedRecipe(new ItemStack(Material.WOOD_SWORD, 1));
        recipe.shape("   ", " I ", " U ");
        recipe.setIngredient('I', Material.DIAMOND_SWORD);
        recipe.setIngredient('U', Material.GOLD_INGOT);
        Bukkit.getServer().addRecipe(recipe);*/
        /*ShapedRecipe recipe = new ShapedRecipe(new ItemStack(Material.WOOD_SWORD, 1));
        recipe.shape("UUU", "UIU", "UUU");
        recipe.setIngredient('I', Material.DIAMOND_SWORD);
        recipe.setIngredient('U', Material.GOLD_INGOT);
        Bukkit.getServer().addRecipe(recipe);*/
        /*ItemStack resultFurnace = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta itemMeta = resultFurnace.getItemMeta();
        itemMeta.setDisplayName("Upgrade!");
        resultFurnace.setItemMeta(itemMeta);
        FurnaceRecipe furnaceRecipe = new FurnaceRecipe(resultFurnace, Material.DIAMOND_SWORD);
        Bukkit.addRecipe(furnaceRecipe);*/

        /*try {
            Field f = Material.class.getDeclaredField("durability");
            f.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);

            f.set(Material.WOOD_SPADE, (short) 0);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        /*try {
            Constructor<Material> constructor = Material.class.getDeclaredConstructor(
                    Integer.class,
                    Integer.class,
                    Integer.class,

            );
            constructor.setAccessible(true);
            WOOD_MY_SPADE = constructor.newInstance(269, 1, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        /*try {
            Material[] oldValues = (Material[]) ReflectionUtil.getStaticFieldValue(Material.class, "$VALUES");

            Material me = (Material) ReflectionUtil.invokeEnumConstructor(Material.class, new Class[] {
                    String.class, Material.class
            }, new Object[]{ "Test", null });

            for (Material value : Material.values()) {
                System.out.println("- " + value);
            }

            //ReflectionUtil.setStaticFieldValue(Material.class, "$VALUES", oldValues);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onDisable() {

    }

    public ISet getIset(ItemStack itemStack) {
        WeaponSet weaponSet = getWeaponConfig().getWeaponSet(itemStack.getType());
        ArmorSet armorSet = getArmorConfig().getArmorSet(itemStack.getType());
        if (weaponSet != null || armorSet != null)
            return (weaponSet == null) ? armorSet : weaponSet;
        else
            return null;
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
    public UpgradeManager getUpgradeManager() {
        return upgradeManager;
    }
    public EnchantGroupConfig getEnchantGroupConfig() {
        return enchantGroupConfig;
    }
    public ArrayList<Material> getUnbrokenList() {
        return unbrokenList;
    }
}
