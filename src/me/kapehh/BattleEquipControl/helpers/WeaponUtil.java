package me.kapehh.BattleEquipControl.helpers;

import me.kapehh.BattleEquipControl.bukkit.EnchantmentManager;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Karen on 01.09.2014.
 */
public class WeaponUtil {
    private ItemStack itemStack;
    private int exp;
    private int level;
    private int version;

    public static boolean isEmptyEnchants(ItemStack itemStack) {
        for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
            if (!enchantment.equals(EnchantmentManager.ENCHANT_VERSION) &&
                !enchantment.equals(EnchantmentManager.ENCHANT_LEVEL) &&
                !enchantment.equals(EnchantmentManager.ENCHANT_EXP)) {
                return false;
            }
        }
        return true;
    }

    public WeaponUtil(ItemStack itemStack) {
        this.itemStack = itemStack;
        load();
    }

    public void load() {
        version = itemStack.getEnchantmentLevel(EnchantmentManager.ENCHANT_VERSION);
        level = itemStack.getEnchantmentLevel(EnchantmentManager.ENCHANT_LEVEL);
        exp = itemStack.getEnchantmentLevel(EnchantmentManager.ENCHANT_EXP);
    }

    public void save() {
        itemStack.addUnsafeEnchantment(EnchantmentManager.ENCHANT_VERSION, version);
        itemStack.addUnsafeEnchantment(EnchantmentManager.ENCHANT_LEVEL, level);
        itemStack.addUnsafeEnchantment(EnchantmentManager.ENCHANT_EXP, exp);
    }

    public void clear() {

    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public String toString() {
        return "WeaponUtil{" +
                "itemStack=" + itemStack +
                ", exp=" + exp +
                ", level=" + level +
                ", version=" + version +
                '}';
    }
}
