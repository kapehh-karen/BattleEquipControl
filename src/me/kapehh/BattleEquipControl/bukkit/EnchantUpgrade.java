package me.kapehh.BattleEquipControl.bukkit;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Karen on 02.11.2014.
 */
public class EnchantUpgrade extends EnchantmentWrapper {

    public EnchantUpgrade(int id) {
        super(id);
    }

    @Override
    public String getName() {
        return "EnchantUpgrade";
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }
}
