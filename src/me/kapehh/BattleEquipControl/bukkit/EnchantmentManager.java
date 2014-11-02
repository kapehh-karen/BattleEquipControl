package me.kapehh.BattleEquipControl.bukkit;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;

import java.lang.reflect.Field;

/**
 * Created by Karen on 01.09.2014.
 */
public class EnchantmentManager {
    public static final EnchantUpgrade ENCHANT_UPGRADE = new EnchantUpgrade(210);

    private static boolean isInit = false;

    public static void init() {
        if (isInit) return;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        EnchantmentWrapper.registerEnchantment(ENCHANT_UPGRADE);
        isInit = true;
    }

    private EnchantmentManager() {
        // deny access
    }
}
