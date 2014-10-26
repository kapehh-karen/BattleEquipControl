package me.kapehh.BattleEquipControl.sets;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

/**
 * Created by Karen on 26.10.2014.
 */
public class UpgradeSet {
    Material material;
    int exp;

    public UpgradeSet(Material material, int exp) {
        this.material = material;
        this.exp = exp;
    }

    public Material getMaterial() {
        return material;
    }

    public int getExp() {
        return exp;
    }

    @Override
    public String toString() {
        return "UpgradeSet{" +
                "material=" + material +
                ", exp=" + exp +
                '}';
    }
}
