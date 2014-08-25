package me.kapehh.BattleEquipControl;

import me.kapehh.BattleEquipControl.sets.ArmorSet;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Logger;

/**
 * Created by Karen on 25.08.2014.
 */
public class MainListener implements Listener {
    /*
        TODO: Вычислять строку формулы для Экспы/Дамага/Защиты при инициализации для всех левелов сразу
     */

    Main main;

    public MainListener(Main main) {
        this.main = main;
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

        /*if (playerAttacker != null && playerAttacked != null) {
            main.getLogger().info("EntityDamageByEntityEvent");
            main.getLogger().info(playerAttacker.toString());
            main.getLogger().info(playerAttacked.toString());
            main.getLogger().info(String.valueOf(event.getDamage()));
        }*/

        Logger logger = main.getLogger();
        logger.info("EntityDamageByEntityEvent");
        if (playerAttacked != null) {
            ItemStack itemStack = playerAttacked.getInventory().getHelmet();
            logger.info("playerAttacked: " + playerAttacked.toString());
            if (itemStack != null) {
                ArmorSet armorSet = main.getArmorConfig().getArmorSet(itemStack.getType());
                logger.info("itemStack: " + itemStack.toString());
                if (armorSet != null) {
                    logger.info("armorSet: " + armorSet.toString());
                    playerAttacked.sendMessage("SEND TO ATTACKER");
                    playerAttacker.sendMessage(armorSet.toString());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageEvent(EntityDamageEvent event) {
        Player playerAttacked = getFromEntity(event.getEntity()); // Того кого атакуют

        /*if (playerAttacked != null) {
            main.getLogger().info("EntityDamageEvent");
            main.getLogger().info(playerAttacked.toString());
            main.getLogger().info(String.valueOf(event.getDamage()));
        }*/
    }
}
