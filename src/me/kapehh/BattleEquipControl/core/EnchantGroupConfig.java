package me.kapehh.BattleEquipControl.core;

import me.kapehh.BattleEquipControl.sets.EnchantGroupSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karen on 13.12.2014.
 */
public class EnchantGroupConfig {
    public List<EnchantGroupSet> enchantGroupSets = new ArrayList<EnchantGroupSet>();

    public void addEnchantGroupSet(EnchantGroupSet enchantGroupSet) {
        enchantGroupSets.add(enchantGroupSet);
    }

    public EnchantGroupSet getEnchantGroupSet(String name) {
        if (name == null) return null;
        for (EnchantGroupSet set : enchantGroupSets) {
            if (set.getName().equalsIgnoreCase(name)) {
                return set;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "EnchantGroupConfig{" +
                "enchantGroupSets=" + enchantGroupSets +
                '}';
    }
}
