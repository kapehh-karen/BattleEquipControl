package me.kapehh.BattleEquipControl.core;

import me.kapehh.BattleEquipControl.sets.MobSet;
import me.kapehh.BattleEquipControl.sets.UpgradeSet;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karen on 26.10.2014.
 */
public class UpgradeConfig {
    public List<UpgradeSet> upgradeSetList = new ArrayList<UpgradeSet>();

    public void addUpgradeSet(UpgradeSet upgradeSet) {
        upgradeSetList.add(upgradeSet);
    }

    public UpgradeSet getUpgradeSet(MaterialData material) {
        for (UpgradeSet upgradeSet : upgradeSetList) {
            if (upgradeSet.getMaterial().equals(material)) {
                return upgradeSet;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "UpgradeConfig{" +
                "upgradeSetList=" + upgradeSetList +
                '}';
    }
}
