package me.kapehh.BattleEquipControl;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Karen on 25.08.2014.
 */
public class MainListener implements Listener {
    /*
        TODO: Вычислять строку формулы для Экспы/Дамага/Защиты при инициализации для всех левелов сразу
     */

    JavaPlugin plugin;

    public MainListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private Player getFromEntity(Entity entity) {
        if (entity == null) {
            return null;
        } else if (entity instanceof Player) {
            return (Player) entity;
        } else if ((entity instanceof Projectile) && (((Projectile) entity).getShooter() instanceof Player)) {
            return (Player) ((Projectile) entity).getShooter();
        }
        return null;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Player playerAttacker = getFromEntity(event.getDamager()); // Кто атакует
        Player playerAttacked = getFromEntity(event.getEntity()); // Того кого атакуют

        if (playerAttacker != null && playerAttacked != null) {
            plugin.getLogger().info("EntityDamageByEntityEvent");
            plugin.getLogger().info(playerAttacker.toString());
            plugin.getLogger().info(playerAttacked.toString());
            plugin.getLogger().info(String.valueOf(event.getDamage()));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageEvent(EntityDamageEvent event) {
        Player playerAttacked = getFromEntity(event.getEntity()); // Того кого атакуют

        if (playerAttacked != null) {
            plugin.getLogger().info("EntityDamageEvent");
            plugin.getLogger().info(playerAttacked.toString());
            plugin.getLogger().info(String.valueOf(event.getDamage()));
        }
    }
}
