package me.kapehh.BattleEquipControl;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by Karen on 25.08.2014.
 */
public class MainListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        Entity attacked = event.getEntity();
        Player player = null;

        if (attacker instanceof Projectile) {
            Projectile projectile = (Projectile) attacker;
            player = (Player) projectile.getShooter();
        } else if (attacker instanceof Player) {
            player = (Player) attacker;
        }

        if (player != null) {
            player.sendMessage(attacker.toString());
            player.sendMessage(attacked.toString());
        }
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {

    }
}
