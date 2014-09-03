package me.kapehh.BattleEquipControl.sets;

import org.bukkit.entity.EntityType;

/**
 * Created by Karen on 03.09.2014.
 */
public class MobSet {
    EntityType entityType;
    int exp;

    public MobSet(EntityType entityType, int exp) {
        this.entityType = entityType;
        this.exp = exp;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public int getExp() {
        return exp;
    }

    @Override
    public String toString() {
        return "MobSet{" +
                "entityType=" + entityType +
                ", exp=" + exp +
                '}';
    }
}
