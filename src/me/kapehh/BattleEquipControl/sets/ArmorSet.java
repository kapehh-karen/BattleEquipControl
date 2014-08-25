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
    List<Integer> strangeList = new ArrayList<Integer>();

    public ArmorSet(Material material, Integer maxLevel, String evalLevelStrange) {
        // TODO: Сделать генерацию strange для каждого уровня и добавить в список
        this.strangeList.add(5);
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

    @Override
    public String toString() {
        return "ArmorSet{" +
                "material=" + material +
                ", maxLevel=" + maxLevel +
                ", strangeList=" + strangeList +
                '}';
    }
}
