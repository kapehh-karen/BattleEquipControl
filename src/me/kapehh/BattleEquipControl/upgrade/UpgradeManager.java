package me.kapehh.BattleEquipControl.upgrade;

import me.kapehh.BattleEquipControl.Main;
import me.kapehh.BattleEquipControl.core.ArmorConfig;
import me.kapehh.BattleEquipControl.core.UpgradeConfig;
import me.kapehh.BattleEquipControl.core.WeaponConfig;
import me.kapehh.BattleEquipControl.sets.ArmorSet;
import me.kapehh.BattleEquipControl.sets.UpgradeSet;
import me.kapehh.BattleEquipControl.sets.WeaponSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karen on 16.11.2014.
 */
public class UpgradeManager {
    public ItemStack resultItem = null;
    public List<Recipe> recipes = new ArrayList<Recipe>();

    public void init(Main main) {
        if (resultItem == null) {
            resultItem = new ItemStack(Material.BOOK, 1, (short)123);
            ItemMeta meta = resultItem.getItemMeta();
            List<String> lore = new ArrayList<String>();
            lore.add("Кликните чтоб применить заточку");
            meta.setDisplayName(ChatColor.GREEN + "Заточка");
            meta.setLore(lore);
            resultItem.setItemMeta(meta);
        }

        ArmorConfig armorConfig = main.getArmorConfig();
        WeaponConfig weaponConfig = main.getWeaponConfig();
        UpgradeConfig upgradeConfig = main.getUpgradeConfig();

        for (UpgradeSet upgradeSet : upgradeConfig.upgradeSetList) {
            for (ArmorSet armorSet : armorConfig.armorSets) {
                addMaterialAndUpgrade(armorSet.getMaterial(), upgradeSet.getMaterial());
            }
            for (WeaponSet weaponSet : weaponConfig.weaponSets) {
                addMaterialAndUpgrade(weaponSet.getMaterial(), upgradeSet.getMaterial());
            }
        }
    }

    private void addMaterialAndUpgrade(Material material, Material upgrader) {
        ShapedRecipe recipe = new ShapedRecipe(resultItem);
        recipe.shape("UUU", "UIU", "UUU");
        recipe.setIngredient('I', material);
        recipe.setIngredient('U', upgrader);
        recipes.add(recipe);
        Bukkit.getServer().addRecipe(recipe);
    }

    public boolean isRecipe(Recipe recipe) {
        return recipe.getResult().equals(resultItem);
    }
}
