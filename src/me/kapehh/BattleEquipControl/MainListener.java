package me.kapehh.BattleEquipControl;

import me.kapehh.BattleEquipControl.sets.ArmorSet;
import me.kapehh.BattleEquipControl.sets.WeaponSet;
import org.bukkit.ChatColor;
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
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by Karen on 25.08.2014.
 */
public class MainListener implements Listener {
    /*
        TODO: Вычислять строку формулы для Экспы/Дамага/Защиты при инициализации для всех левелов сразу

        TODO: У Лука учитывать силу натяжения (событие EntityShootBowEvent поле getForce)

        TODO: в EntityDamage у оружия уже имеется измененная прочность

        TODO: Попробовать в net.minecraft.server.v1_7_R3.Item менять MaxDurability
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

    private boolean isAir(ItemStack itemStack) {
        return (itemStack == null || itemStack.getType().equals(Material.AIR));
    }

    private double getDamage(Entity entity) {
        Material material = Material.AIR;

        if (entity instanceof Player) { // Если атакующий - игрок
            Player player = (Player) entity; // BY PLAYER
            ItemStack itemStack = player.getItemInHand();

            // Если в руках ничего нет
            if (isAir(itemStack)) {
                return -1;
            }

            material = itemStack.getType();
        } else if ((entity instanceof Projectile) && (((Projectile) entity).getShooter() instanceof Player)) { // Если атакующий это стрела и стрела выпущена игроком
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
        /*} else if ((entity instanceof Projectile) && (((Projectile) entity).getShooter() instanceof Player)) {
            player = (Player) ((Projectile) entity).getShooter();*/
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

        // Если есть атакующий игрок
        if (playerAttacker != null) {
            stringBuilder.append("Original damage: ").append(damage).append('\n');

            double attackerDamage = getDamage(event.getDamager());
            if (attackerDamage >= 0) {
                damage += attackerDamage;
            }

            stringBuilder.append("Bonus damage: ").append(attackerDamage).append('\n');
            /*ItemStack itemInHand = playerAttacker.getItemInHand();
            if (!isAir(itemInHand) && itemInHand.getDurability() != 0) {
                itemInHand.setDurability((short) 0);
                playerAttacker.updateInventory();
            }*/
        }

        // Если есть игрок которого атакуют
        if (playerAttacked != null) {
            double attackedStrong = getStrong(event.getEntity());
            damage = damage - (damage * (attackedStrong / 100));

            /*PlayerInventory inventory = playerAttacked.getInventory();
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
            playerAttacked.updateInventory();*/

            stringBuilder.append("Armor opponent: ").append(attackedStrong).append('\n');
        }

        if (playerAttacker != null) {
            stringBuilder.append("Result damage: ").append(damage);
            playerAttacker.sendMessage(stringBuilder.toString());
        }

        // Ну мало ли :DD
        if (damage < 0) damage = 0;

        event.setDamage(damage);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onShootBow(EntityShootBowEvent event) {
        if (event.isCancelled()) {
            return;
        }

        /*if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ItemStack bow = event.getBow();
            bow.setDurability((short) 0);
            player.updateInventory();
        }*/
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player playerAttacked = getFromEntity(event.getEntity()); // Того кого атакуют
    }

    // События свящанные с обновлением вещи

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }

        // Обновляем описание вещи
        updateLore(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCraft(CraftItemEvent event) {
        if (event.isCancelled()) {
            return;
        }

        // Обновляем описание вещи
        //updateLore(event.getCurrentItem());

        /*ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(Arrays.asList("One", ChatColor.BOLD + "NYAN"));
        itemStack.setItemMeta(itemMeta);

        net.minecraft.server.v1_7_R3.ItemStack nms = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag;
        if(nms.tag != null)
            tag = nms.tag;
        else
        {
            nms.tag = new NBTTagCompound();
            tag = nms.tag;
        }
        tag.setInt("MyKek", 546);
        itemStack = CraftItemStack.asCraftMirror(nms);*/
    }

    private void updateLore(Player player) {
        // Обновили в руке
        updateLore(player.getItemInHand());

        // Обновляем броню
        PlayerInventory inventory = player.getInventory();
        ItemStack helmet = inventory.getHelmet();
        ItemStack chestplate = inventory.getChestplate();
        ItemStack leggins = inventory.getLeggings();
        ItemStack boots = inventory.getBoots();

        updateLore(helmet);
        updateLore(chestplate);
        updateLore(leggins);
        updateLore(boots);

        inventory.setHelmet(helmet);
        inventory.setChestplate(chestplate);
        inventory.setLeggings(leggins);
        inventory.setBoots(boots);
    }

    private void updateLore(ItemStack itemStack) {
        if (isAir(itemStack)) {
            return;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.hasLore()) {
            return;
        }

        WeaponSet weaponSet = main.getWeaponConfig().getWeaponSet(itemStack.getType());
        ArmorSet armorSet = main.getArmorConfig().getArmorSet(itemStack.getType());
        if (weaponSet != null || armorSet != null) {
            String colorPreffix = ChatColor.RESET + "" + ChatColor.WHITE;
            String secondLine = (weaponSet != null) ?
                "Bonus damage: " + weaponSet.getDamage(1) :
                "Bonus protection: " + armorSet.getStrong(1);
            itemMeta.setLore(Arrays.asList(colorPreffix + "Level: 1", colorPreffix + secondLine));
            itemStack.setItemMeta(itemMeta);
        }
    }
}
