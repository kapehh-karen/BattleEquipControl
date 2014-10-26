package me.kapehh.BattleEquipControl.sets;

import org.bukkit.Material;

import java.util.List;

/**
 * Created by Karen on 25.08.2014.
 */
public class ArmorSet implements ISet {
    Material material = Material.AIR;
    Integer maxLevel = 1;
    Integer maxLevelUpgrade = 1;
    List<Double> strongList = null;
    List<Double> expList = null;

    public ArmorSet(Material material, Integer maxLevel, Integer maxLevelUpgrade, List<Double> strongList, List<Double> expList) {
        this.strongList = strongList;
        this.expList = expList;
        this.maxLevel = maxLevel;
        this.material = material;
        this.maxLevelUpgrade = maxLevelUpgrade;
    }

    public Material getMaterial() {
        return material;
    }

    public double getStrong(int level) {
        if (level < 1) level = 1;
        if (level > maxLevel) level = maxLevel;
        if (level > strongList.size()) return 0;
        return strongList.get(level - 1);
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

    public Integer getMaxLevelUpgrade() {
        return maxLevelUpgrade;
    }

    // Реализация интерфейса ISet

    @Override
    public double getIBonus(int level) {
        return getStrong(level);
    }

    @Override
    public String getIBonusName() {
        return "Защита";
    }

    @Override
    public int getIMaxLevel() {
        return getMaxLevel();
    }

    @Override
    public int getIMaxLevelUpgrade() {
        return getMaxLevelUpgrade();
    }

    @Override
    public double getIExp(int level) {
        return getExp(level);
    }

    @Override
    public String toString() {
        return "ArmorSet{" +
                "material=" + material +
                ", maxLevel=" + maxLevel +
                ", strongList=" + strongList +
                ", expList=" + expList +
                '}';
    }
}
