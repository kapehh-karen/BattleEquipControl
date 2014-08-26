package me.kapehh.BattleEquipControl;

import me.kapehh.BattleEquipControl.sets.ArmorSet;
import me.kapehh.BattleEquipControl.sets.WeaponSet;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Created by Karen on 25.08.2014.
 */
public class MainListener implements Listener {
    /*
        TODO: Вычислять строку формулы для Экспы/Дамага/Защиты при инициализации для всех левелов сразу

        TODO: У Лука учитывать силу натяжения (событие EntityShootBowEvent поле getForce)
     */

    Main main;

    public MainListener(Main main) {
        this.main = main;
    }

    // TODO: REMOVE THIS
    private void debugPrint() {

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

    private boolean isAir(ItemStack itemStack) {
        return (itemStack == null || itemStack.getType().equals(Material.AIR));
    }

    private double getDamage(Player player) {
        ItemStack itemStack = player.getItemInHand();

        // Если в руках ничего нет
        if (isAir(itemStack)) {
            return -1;
        }

        WeaponSet weaponSet = main.getWeaponConfig().getWeaponSet(itemStack.getType());
        if (weaponSet == null) { // Если такого оружия не найдено в конфиге
            return -1;
        }

        // Возвращаем дамаг в зависимости от уровня вещи TODO: Доделать уровень вещи
        return weaponSet.getDamage(1);
    }

    private double getStrong(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack helmet = inventory.getHelmet();
        ItemStack chestplate = inventory.getChestplate();
        ItemStack leggins = inventory.getLeggings();
        ItemStack boots = inventory.getBoots();
        double procents = 0;

        if (!isAir(helmet)) {
            ArmorSet armorSet = main.getArmorConfig().getArmorSet(helmet.getType());
            if (armorSet != null) {
                procents += armorSet.getStrong(1);
            }
        }

        if (!isAir(chestplate)) {
            ArmorSet armorSet = main.getArmorConfig().getArmorSet(chestplate.getType());
            if (armorSet != null) {
                procents += armorSet.getStrong(1);
            }
        }

        if (!isAir(leggins)) {
            ArmorSet armorSet = main.getArmorConfig().getArmorSet(leggins.getType());
            if (armorSet != null) {
                procents += armorSet.getStrong(1);
            }
        }

        if (!isAir(boots)) {
            ArmorSet armorSet = main.getArmorConfig().getArmorSet(boots.getType());
            if (armorSet != null) {
                procents += armorSet.getStrong(1);
            }
        }

        if (procents < 0) procents = 0;
        if (procents > 100) procents = 100;
        return procents;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player playerAttacker = getFromEntity(event.getDamager()); // Кто атакует
        Player playerAttacked = getFromEntity(event.getEntity()); // Того кого атакуют
        double damage = event.getDamage();

        StringBuilder stringBuilder = new StringBuilder();

        /*if (playerAttacker != null && playerAttacked != null) {
            main.getLogger().info("EntityDamageByEntityEvent");
            main.getLogger().info(playerAttacker.toString());
            main.getLogger().info(playerAttacked.toString());
            main.getLogger().info(String.valueOf(event.getDamage()));
        }*/

        // TODO: Remove stringBuilder
        stringBuilder.append("CAUSE: ").append(event.getCause()).append('\n');
        stringBuilder.append("ORIGINAL DAMAGE: ").append(damage).append('\n');

        // Если есть атакующий игрок
        if (playerAttacker != null) {
            stringBuilder.append("ATTACKER: ").append(playerAttacker.toString()).append('\n');

            double attackerDamage = getDamage(playerAttacker);
            if (attackerDamage >= 0) {
                damage = attackerDamage;
            }

            stringBuilder.append("ATTACKER DAMAGE: ").append(damage).append('\n');
        }

        // Если есть игрок которого атакуют
        if (playerAttacked != null) {
            stringBuilder.append("ATTACKED: ").append(playerAttacked.toString()).append('\n');
            stringBuilder.append("ATTACKED HP: ").append(playerAttacked.getHealth()).append('\n');

            double attackedStrong = getStrong(playerAttacked);
            damage = damage - (damage * (attackedStrong / 100));

            stringBuilder.append("ATTACKED STRONG: ").append(attackedStrong).append('\n');
        }

        stringBuilder.append("RESULT DAMAGE: ").append(damage).append('\n');

        // Ну мало ли :DD
        if (damage < 0) damage = 0;

        event.setDamage(damage);

        main.getLogger().info(stringBuilder.toString());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player playerAttacked = getFromEntity(event.getEntity()); // Того кого атакуют

        /*if (playerAttacked != null) {
            main.getLogger().info("EntityDamageEvent");
            main.getLogger().info(playerAttacked.toString());
            main.getLogger().info(String.valueOf(event.getDamage()));
        }*/
    }
}
