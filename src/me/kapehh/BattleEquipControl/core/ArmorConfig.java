package me.kapehh.BattleEquipControl.core;

import me.kapehh.BattleEquipControl.sets.ArmorSet;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karen on 25.08.2014.
 */
public class ArmorConfig {
    List<ArmorSet> armorSets = new ArrayList<ArmorSet>();

    public void addArmorSet(ArmorSet armorSet) {
        armorSets.add(armorSet);
    }
    
    public ArmorSet getArmorSet(Material material) {
        for (ArmorSet armorSet : armorSets) {
            if (armorSet.getMaterial().equals(material)) {
                return armorSet;
            }
        }
        return null;
    }
}
