package me.kapehh.BattleEquipControl.sets;

import org.bukkit.Material;

import java.util.List;

/**
 * Created by Karen on 25.08.2014.
 */
public class ArmorSet implements ISet {
    Material material = Material.AIR;
    Integer maxLevel = 1;
    List<Double> strongList = null;

    public ArmorSet(Material material, Integer maxLevel, List<Double> strongList) {
        this.strongList = strongList;
        this.maxLevel = maxLevel;
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public double getStrong(int level) {
        if (level < 1) level = 1;
        if (level > maxLevel) level = maxLevel;
        return strongList.get(level - 1);
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public String toString() {
        return "ArmorSet{" +
                "material=" + material +
                ", maxLevel=" + maxLevel +
                ", strongList=" + strongList +
                '}';
    }

    @Override
    public double getBonus(int level) {
        return getStrong(level);
    }
}
