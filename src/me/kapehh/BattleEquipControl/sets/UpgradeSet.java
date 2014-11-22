package me.kapehh.BattleEquipControl.sets;

import org.bukkit.material.MaterialData;

import java.util.List;

/**
 * Created by Karen on 26.10.2014.
 */
public class UpgradeSet {
    MaterialData material;
    int exp;
    List<Double> chanceFailUpgrade;
    int maxLevelUpgrade;

    public UpgradeSet(MaterialData material, int exp, int maxLevelUpgrade, List<Double> chanceFailUpgrade) {
        this.material = material;
        this.exp = exp;
        this.maxLevelUpgrade = maxLevelUpgrade;
        this.chanceFailUpgrade = chanceFailUpgrade;
    }

    public MaterialData getMaterial() {
        return material;
    }

    public int getExp() {
        return exp;
    }

    public double getChanceFailUpgrade(int level) {
        if (level < 1) level = 1;
        if (level > maxLevelUpgrade) level = maxLevelUpgrade;
        if (level > chanceFailUpgrade.size()) return 0;
        return chanceFailUpgrade.get(level - 1);
    }

    @Override
    public String toString() {
        return "UpgradeSet{" +
                "material=" + material +
                ", exp=" + exp +
                ", chanceFailUpgrade=" + chanceFailUpgrade +
                ", maxLevelUpgrade=" + maxLevelUpgrade +
                '}';
    }
}
