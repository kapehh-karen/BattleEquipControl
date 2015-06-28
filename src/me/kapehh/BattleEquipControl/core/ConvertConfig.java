package me.kapehh.BattleEquipControl.core;

import me.kapehh.BattleEquipControl.Main;
import me.kapehh.BattleEquipControl.sets.UpgradeSet;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karen on 28.06.2015.
 */
public class ConvertConfig {
    public double percent_of_convert;
    public UpgradeConfig upgradeConfig;

    private UpgradeSet getMinExp() {
        if (upgradeConfig == null) return null;
        UpgradeSet min = null;
        for (UpgradeSet upgradeSet : upgradeConfig.upgradeSetList) {
            if (min == null) {
                min = upgradeSet;
                continue;
            }

            if (min.getExp() > upgradeSet.getExp()) {
                min = upgradeSet;
            }
        }
        return min;
    }

    private UpgradeSet getMaxFromExp(double exp) {
        double max = -1;
        UpgradeSet ret = null;
        for (UpgradeSet upgradeSet : upgradeConfig.upgradeSetList) {
            if ((upgradeSet.getExp() <= exp) && (max < upgradeSet.getExp())) {
                max = upgradeSet.getExp();
                ret = upgradeSet;
            }
        }
        return ret;
    }

    public List<ItemStack> getConvertForItem(ItemStack item, double exp) {
        if (upgradeConfig == null) return null;

        if (Main.debug) System.out.println("getConvertForItem: first exp " + exp);

        List<ItemStack> stacks = new ArrayList<ItemStack>();
        ItemStack itemStack;
        UpgradeSet curr = null, prev = null;
        UpgradeSet min_exp = getMinExp();
        exp *= (percent_of_convert / 100);

        if (Main.debug) System.out.println("getConvertForItem: second exp " + exp + ", min exp " + min_exp);

        if (exp <= min_exp.getExp()) {
            stacks.add(new ItemStack(min_exp.getMaterial().getItemType(), 1));
            return stacks;
        }

        while (exp > min_exp.getExp()) {
            curr = getMaxFromExp(exp);

            if (Main.debug) System.out.println("getConvertForItem: now exp " + exp + ", curr " + curr);

            if (curr.equals(prev)) {
                itemStack = stacks.get(stacks.size() - 1);
                itemStack.setAmount(itemStack.getAmount() + 1);
            } else {
                stacks.add(new ItemStack(curr.getMaterial().getItemType(), 1));
            }
            prev = curr;

            exp -= curr.getExp();
        }

        return stacks;
    }

    @Override
    public String toString() {
        return "ConvertConfig{" +
                "percent_of_convert=" + percent_of_convert +
                ", upgradeConfig=" + upgradeConfig +
                '}';
    }
}
