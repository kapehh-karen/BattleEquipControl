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
    List<Double> expList = null;

    public WeaponSet(Material material, Integer maxLevel, List<Double> damageList, List<Double> expList) {
        this.damageList = damageList;
        this.expList = expList;
        this.maxLevel = maxLevel;
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public double getDamage(int level) {
        if (level < 1) level = 1;
        if (level > maxLevel) level = maxLevel;
        if (level > damageList.size()) return 0;
        return damageList.get(level - 1);
    }

    public double getExp(int level) {
        if (level < 1) level = 1;
        if (level > maxLevel) level = maxLevel;
        if (level > expList.size()) return 0;
        return expList.get(level - 1);
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public double getIBonus(int level) {
        return getDamage(level);
    }

    @Override
    public String getIBonusName() {
        return "Урон";
    }

    @Override
    public int getIMaxLevel() {
        return maxLevel;
    }

    @Override
    public double getIExp(int level) {
        return getExp(level);
    }

    @Override
    public String toString() {
        return "WeaponSet{" +
                "material=" + material +
                ", maxLevel=" + maxLevel +
                ", damageList=" + damageList +
                ", expList=" + expList +
                '}';
    }
}
