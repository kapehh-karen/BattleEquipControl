package me.kapehh.BattleEquipControl.sets;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.material.MaterialData;

/**
 * Created by Karen on 26.10.2014.
 */
public class UpgradeSet {
    MaterialData material;
    int exp;
    double chanceFailUpgrade;

    public UpgradeSet(MaterialData material, int exp, double chanceFailUpgrade) {
        this.material = material;
        this.exp = exp;
        this.chanceFailUpgrade = chanceFailUpgrade;
    }

    public MaterialData getMaterial() {
        return material;
    }

    public int getExp() {
        return exp;
    }

    public double getChanceFailUpgrade() {
        return chanceFailUpgrade;
    }

    @Override
    public String toString() {
        return "UpgradeSet{" +
                "material=" + material +
                ", exp=" + exp +
                '}';
    }
}
