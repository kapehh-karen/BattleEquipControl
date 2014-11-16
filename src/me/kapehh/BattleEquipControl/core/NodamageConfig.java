package me.kapehh.BattleEquipControl.core;

import me.kapehh.BattleEquipControl.sets.ArmorSet;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karen on 29.09.2014.
 */
public class NodamageConfig {
    public List<Material> materials = new ArrayList<Material>();

    public void addMaterial(Material item) {
        materials.add(item);
    }

    public boolean containsMaterial(Material item) {
        return materials.contains(item);
    }

    @Override
    public String toString() {
        return "NodamageConfig{" +
                "materials=" + materials +
                '}';
    }
}
