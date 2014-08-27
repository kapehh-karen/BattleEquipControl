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
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

/**
 * Created by Karen on 25.08.2014.
 */
public class MainListener implements Listener {
    /*
        TODO: Вычислять строку формулы для Экспы/Дамага/Защиты при инициализации для всех левелов сразу

        TODO: У Лука учитывать силу натяжения (событие EntityShootBowEvent поле getForce)

        TODO: в EntityDamage у оружия уже имеется измененная прочность
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

    public double getDamageReduced(Player player)
    {
        PlayerInventory inv = player.getInventory();
        ItemStack boots = inv.getBoots();
        ItemStack helmet = inv.getHelmet();
        ItemStack chest = inv.getChestplate();
        ItemStack pants = inv.getLeggings();
        double red = 0.0;

        if(helmet.getType() == Material.LEATHER_HELMET)red = red + 0.04;
        else if(helmet.getType() == Material.GOLD_HELMET)red = red + 0.08;
        else if(helmet.getType() == Material.CHAINMAIL_HELMET)red = red + 0.08;
        else if(helmet.getType() == Material.IRON_HELMET)red = red + 0.08;
        else if(helmet.getType() == Material.DIAMOND_HELMET)red = red + 0.12;

        if(boots.getType() == Material.LEATHER_BOOTS)red = red + 0.04;
        else if(boots.getType() == Material.GOLD_BOOTS)red = red + 0.04;
        else if(boots.getType() == Material.CHAINMAIL_BOOTS)red = red + 0.04;
        else if(boots.getType() == Material.IRON_BOOTS)red = red + 0.08;
        else if(boots.getType() == Material.DIAMOND_BOOTS)red = red + 0.12;

        if(pants.getType() == Material.LEATHER_LEGGINGS)red = red + 0.08;
        else if(pants.getType() == Material.GOLD_LEGGINGS)red = red + 0.12;
        else if(pants.getType() == Material.CHAINMAIL_LEGGINGS)red = red + 0.16;
        else if(pants.getType() == Material.IRON_LEGGINGS)red = red + 0.20;
        else if(pants.getType() == Material.DIAMOND_LEGGINGS)red = red + 0.24;

        if(chest.getType() == Material.LEATHER_CHESTPLATE)red = red + 0.12;
        else if(chest.getType() == Material.GOLD_CHESTPLATE)red = red + 0.20;
        else if(chest.getType() == Material.CHAINMAIL_CHESTPLATE)red = red + 0.20;
        else if(chest.getType() == Material.IRON_CHESTPLATE)red = red + 0.24;
        else if(chest.getType() == Material.DIAMOND_CHESTPLATE)red = red + 0.32;

        return red;
    }

    private double getDamage(Entity entity) {
        Material material = Material.AIR;

        if (entity instanceof Player) {
            Player player = (Player) entity; // BY PLAYER
            ItemStack itemStack = player.getItemInHand();

            // Если в руках ничего нет
            if (isAir(itemStack)) {
                return -1;
            }

            material = itemStack.getType();
        } else if (entity instanceof Projectile) {
            material = Material.BOW; // BY ARROW TODO: Возможно придется в будущем заменить на Material.ARROW чтоб игроки не лупили простым луком
        } else {
            return -1; // WTF ?
        }

        WeaponSet weaponSet = main.getWeaponConfig().getWeaponSet(material);
        if (weaponSet == null) { // Если такого оружия не найдено в конфиге
            return -1;
        }

        // Возвращаем дамаг в зависимости от уровня вещи TODO: Доделать уровень вещи
        return weaponSet.getDamage(1);
    }

    private double getStrong(Entity entity) {
        Player player;

        if (entity instanceof Player) {
            player = (Player) entity;
        } else if ((entity instanceof Projectile) && (((Projectile) entity).getShooter() instanceof Player)) {
            player = (Player) ((Projectile) entity).getShooter();
        } else {
            return 0; // WTF ?
        }

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

            double attackerDamage = getDamage(event.getDamager());
            if (attackerDamage >= 0) {
                damage += attackerDamage;
            }

            ItemStack itemInHand = playerAttacker.getItemInHand();
            if (!isAir(itemInHand) && itemInHand.getDurability() != 0) {
                //itemInHand.setDurability((short) (itemInHand.getDurability() - 1)); // SAVE DURABILITY
                itemInHand.setDurability((short) 0);
                playerAttacker.updateInventory();
            }

            stringBuilder.append("ATTACKER DAMAGE: ").append(damage).append('\n');
        }

        // Если есть игрок которого атакуют
        if (playerAttacked != null) {
            stringBuilder.append("ATTACKED: ").append(playerAttacked.toString()).append('\n');
            stringBuilder.append("ATTACKED HP: ").append(playerAttacked.getHealth()).append('\n');

            double attackedStrong = getStrong(event.getEntity());
            damage = damage - (damage * (attackedStrong / 100));

            PlayerInventory inventory = playerAttacked.getInventory();
            ItemStack helmet = inventory.getHelmet();
            ItemStack chestplate = inventory.getChestplate();
            ItemStack leggins = inventory.getLeggings();
            ItemStack boots = inventory.getBoots();
            if (!isAir(helmet)) {
                helmet.setDurability((short) 0);
                inventory.setHelmet(helmet);
            }
            if (!isAir(chestplate)) {
                chestplate.setDurability((short) 0);
                inventory.setChestplate(chestplate);
            }
            if (!isAir(leggins)) {
                leggins.setDurability((short) 0);
                inventory.setLeggings(leggins);
            }
            if (!isAir(boots)) {
                boots.setDurability((short) 0);
                inventory.setBoots(boots);
            }
            /*if (!isAir(helmet) && helmet.getDurability() != 0) {
                helmet.setDurability((short) (helmet.getDurability() - 1)); // SAVE DURABILITY
                inventory.setHelmet(helmet);
            }
            if (!isAir(chestplate) && chestplate.getDurability() != 0) {
                chestplate.setDurability((short) (chestplate.getDurability() - 1)); // SAVE DURABILITY
                inventory.setChestplate(chestplate);
            }
            if (!isAir(leggins) && leggins.getDurability() != 0) {
                leggins.setDurability((short) (leggins.getDurability() - 1)); // SAVE DURABILITY
                inventory.setLeggings(leggins);
            }
            if (!isAir(boots) && boots.getDurability() != 0) {
                boots.setDurability((short) (boots.getDurability() - 1)); // SAVE DURABILITY
                inventory.setBoots(boots);
            }*/
            playerAttacked.updateInventory();

            stringBuilder.append("ATTACKED STRONG: ").append(attackedStrong).append('\n');
        }

        stringBuilder.append("RESULT DAMAGE: ").append(damage).append('\n');

        // Ну мало ли :DD
        if (damage < 0) damage = 0;

        event.setDamage(damage);

        //main.getLogger().info(stringBuilder.toString());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onShootBow(EntityShootBowEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ItemStack bow = event.getBow();
            bow.setDurability((short) 0);
            /*if (bow.getDurability() != 0) {
                bow.setDurability((short) (bow.getDurability() - 1)); // SAVE DURABILITY
                player.updateInventory();
            }*/
            player.updateInventory();
        }
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
