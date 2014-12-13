package me.kapehh.BattleEquipControl.sets;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Karen on 13.12.2014.
 */
public class EnchantGroupSet {
    public static class EnchantException extends Exception {
        public EnchantException(String message) {
            super(message);
        }
    }

    String name;
    Map<Enchantment, Integer> mapOfEnchants = new HashMap<Enchantment, Integer>();

    public EnchantGroupSet(String name) {
        this.name = name;
    }

    public void addEnchant(Enchantment enchantment, Integer maxLevel) {
        mapOfEnchants.put(enchantment, maxLevel);
    }

    public String getName() {
        return name;
    }

    public void tryEnchant(ItemStack itemStack, int rnd) throws EnchantException {
        if (mapOfEnchants.size() <= 0) {
            return;
        }
        rnd = rnd % mapOfEnchants.size();
        for (Enchantment enchantment : mapOfEnchants.keySet()) {
            if (rnd > 0) {
                rnd--;
                continue;
            }
            int levelCurr = itemStack.getEnchantmentLevel(enchantment);
            if (levelCurr < mapOfEnchants.get(enchantment)) {
                //itemStack.removeEnchantment(enchantment);
                itemStack.addUnsafeEnchantment(enchantment, levelCurr + 1);
                throw new EnchantException(ChatColor.GREEN + "Получен чар: " + ChatColor.WHITE + enchantment.getName());
            }
            throw new EnchantException(ChatColor.RED + "Чар " + ChatColor.WHITE + enchantment.getName() + ChatColor.RED + " имеет максимальный уровень.");
        }
        throw new EnchantException(ChatColor.RED + "Произошла внутренняя ошибка. Обратитесь к администратору.");
    }

    @Override
    public String toString() {
        return "EnchantGroupSet{" +
                "name='" + name + '\'' +
                ", mapOfEnchants=" + mapOfEnchants +
                '}';
    }
}
