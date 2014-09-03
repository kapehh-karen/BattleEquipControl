package me.kapehh.BattleEquipControl.core;

import me.kapehh.BattleEquipControl.sets.ArmorSet;
import me.kapehh.BattleEquipControl.sets.WeaponSet;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karen on 26.08.2014.
 */
public class WeaponConfig {
    List<WeaponSet> weaponSets = new ArrayList<WeaponSet>();

    public void addWeaponSet(WeaponSet weaponSet) {
        weaponSets.add(weaponSet);
    }

    public WeaponSet getWeaponSet(Material material) {
        for (WeaponSet weaponSet : weaponSets) {
            if (weaponSet.getMaterial().equals(material)) {
                return weaponSet;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "WeaponConfig{" +
                "weaponSets=" + weaponSets +
                '}';
    }
}
