package me.kapehh.BattleEquipControl.sets;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karen on 25.08.2014.
 */
public class ArmorSet {
    Material material = Material.AIR;
    Integer maxLevel;
    List<Integer> strangeList = new ArrayList<Integer>();

    public ArmorSet(Material material, Integer maxLevel, List<Integer> strangeList) {
        this.strangeList = strangeList;
        this.maxLevel = maxLevel;
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public int getStrange(int level) {
        return strangeList.get(level - 1);
    }

    public int getMaxLevel() {
        return maxLevel;
    }
}
