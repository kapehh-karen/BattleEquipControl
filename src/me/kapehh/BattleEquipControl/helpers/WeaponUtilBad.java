package me.kapehh.BattleEquipControl.helpers;

import me.kapehh.BattleEquipControl.bukkit.EnchantmentManager;
import me.kapehh.BattleEquipControl.sets.ISet;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karen on 01.09.2014.
 */
public class WeaponUtilBad {
    private static final int CURRENT_VERSION = 2;
    private static final String PREFFIX = ChatColor.RESET + "" + ChatColor.BOLD;

    private ISet iSet;
    private ItemStack itemStack;
    private ItemMeta itemMeta;
    private int exp;
    private int level;
    private int version;

    /*public static boolean isEmptyEnchants(ItemStack itemStack) {
        for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
            if (!enchantment.equals(EnchantmentManager.ENCHANT_VERSION) &&
                    !enchantment.equals(EnchantmentManager.ENCHANT_LEVEL) &&
                    !enchantment.equals(EnchantmentManager.ENCHANT_EXP)) {
                return false;
            }
        }
        return true;
    }*/

    public WeaponUtilBad(ItemStack itemStack, ISet iSet) {
        this.iSet = iSet;
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
        if (!itemMeta.hasLore()) {
            itemMeta.setLore(new ArrayList<String>());
        }
        load();
    }

    public void load() {
        List<String> lore = itemMeta.getLore();
        if (lore.size() < 1) {
            clear();
            return;
        }
        String m = lore.get(0);
        if (!m.matches("\\[\\d+,\\d+,\\d+\\]")) {
            clear();
            return;
        }
        String p[] = m.substring(1, m.length() - 1).split(",");
        exp = Integer.parseInt(p[0]);
        level = Integer.parseInt(p[1]);
        version = Integer.parseInt(p[2]);
        if (version != CURRENT_VERSION) {
            clear();
        }
        /*version = itemStack.getEnchantmentLevel(EnchantmentManager.ENCHANT_VERSION);
        level = itemStack.getEnchantmentLevel(EnchantmentManager.ENCHANT_LEVEL);
        exp = itemStack.getEnchantmentLevel(EnchantmentManager.ENCHANT_EXP);*/
    }

    public void save() {
        /*itemStack.addUnsafeEnchantment(EnchantmentManager.ENCHANT_VERSION, version);
        itemStack.addUnsafeEnchantment(EnchantmentManager.ENCHANT_LEVEL, level);
        itemStack.addUnsafeEnchantment(EnchantmentManager.ENCHANT_EXP, exp);*/
        List<String> lore = new ArrayList<String>();
        lore.add(String.format("[%d,%d,%d]", exp, level, version));
        printVals(lore);
    }

    public void clear() {
        exp = 1;
        level = 1;
        version = CURRENT_VERSION;
        save();
    }

    private void printVals(List<String> lore) {
        lore.add(PREFFIX + "Level: " + level);
        lore.add(PREFFIX + "Bonus: " + iSet.getBonus(level));
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
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
