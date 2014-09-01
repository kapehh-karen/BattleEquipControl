package me.kapehh.BattleEquipControl.bukkit;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;

import java.lang.reflect.Field;

/**
 * Created by Karen on 01.09.2014.
 */
public class EnchantmentManager {
    public static final EnchantLevel ENCHANT_LEVEL = new EnchantLevel(200);
    public static final EnchantExp ENCHANT_EXP = new EnchantExp(201);
    public static final EnchantVersion ENCHANT_VERSION = new EnchantVersion(202);

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
        EnchantmentWrapper.registerEnchantment(ENCHANT_LEVEL);
        EnchantmentWrapper.registerEnchantment(ENCHANT_EXP);
        EnchantmentWrapper.registerEnchantment(ENCHANT_VERSION);
        isInit = true;
    }

    private EnchantmentManager() {
        // deny access
    }
}
