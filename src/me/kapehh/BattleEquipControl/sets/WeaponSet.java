package me.kapehh.BattleEquipControl.sets;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karen on 26.08.2014.
 */
public class WeaponSet implements ISet {
    Material material = Material.AIR;
    Integer maxLevel = 1;
    List<Double> damageList = null;

    public WeaponSet(Material material, Integer maxLevel, List<Double> damageList) {
        this.damageList = damageList;
        this.maxLevel = maxLevel;
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public double getDamage(int level) {
        if (level < 1) level = 1;
        if (level > maxLevel) level = maxLevel;
        return damageList.get(level - 1);
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public String toString() {
        return "WeaponSet{" +
                "material=" + material +
                ", maxLevel=" + maxLevel +
                ", damageList=" + damageList +
                '}';
    }

    @Override
    public double getBonus(int level) {
        return getDamage(level);
    }
}
