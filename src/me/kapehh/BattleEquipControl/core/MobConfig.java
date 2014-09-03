package me.kapehh.BattleEquipControl.core;

import me.kapehh.BattleEquipControl.sets.MobSet;
import me.kapehh.BattleEquipControl.sets.WeaponSet;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karen on 03.09.2014.
 */
public class MobConfig {
    List<MobSet> mobSetList = new ArrayList<MobSet>();

    public void addMobSet(MobSet mobSet) {
        mobSetList.add(mobSet);
    }

    public MobSet getMobSet(EntityType entityType) {
        for (MobSet mobSet : mobSetList) {
            if (mobSet.getEntityType().equals(entityType)) {
                return mobSet;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "MobConfig{" +
                "mobSetList=" + mobSetList +
                '}';
    }
}
