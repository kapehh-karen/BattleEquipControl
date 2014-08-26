package me.kapehh.BattleEquipControl.sets;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karen on 25.08.2014.
 */
public class ArmorSet {
    Material material = Material.AIR;
    Integer maxLevel = 1;
    List<Double> strangeList = null;

    public ArmorSet(Material material, Integer maxLevel, List<Double> strangeList) {
        this.strangeList = strangeList;
        this.maxLevel = maxLevel;
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public double getStrange(int level) {
        if (level < 1) level = 1;
        if (level > maxLevel) level = maxLevel;
        return strangeList.get(level - 1);
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public String toString() {
        return "ArmorSet{" +
                "material=" + material +
                ", maxLevel=" + maxLevel +
                ", strangeList=" + strangeList +
                '}';
    }
}
