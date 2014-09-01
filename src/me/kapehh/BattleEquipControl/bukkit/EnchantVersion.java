package me.kapehh.BattleEquipControl.bukkit;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Karen on 01.09.2014.
 */
public class EnchantVersion extends EnchantmentWrapper {

    public EnchantVersion(int id) {
        super(id);
    }

    @Override
    public String getName() {
        return "EnchantVersion";
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
        return null;
    }

    @Override
    public int getMaxLevel() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }
}