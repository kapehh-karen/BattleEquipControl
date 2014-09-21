package me.kapehh.BattleEquipControl;

import me.kapehh.BattleEquipControl.helpers.WeaponUtil;
import me.kapehh.BattleEquipControl.helpers.WeaponUtilBad;
import me.kapehh.BattleEquipControl.sets.ArmorSet;
import me.kapehh.BattleEquipControl.sets.ISet;
import me.kapehh.BattleEquipControl.sets.MobSet;
import me.kapehh.BattleEquipControl.sets.WeaponSet;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
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
import java.util.Random;

/**
 * Created by Karen on 25.08.2014.
 */
public class MainListener implements Listener {
    /*
        TODO: У Лука учитывать силу натяжения (событие EntityShootBowEvent поле getForce)
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
        ItemStack itemStack = null;

        if (entity instanceof Player) { // Если атакующий - игрок
            Player player = (Player) entity; // BY PLAYER
            itemStack = player.getItemInHand();

            // Если в руках ничего нет
            if (isAir(itemStack)) {
                return -1;
            }

            material = itemStack.getType();
        } else if ((entity instanceof Projectile) && (((Projectile) entity).getShooter() instanceof Player)) { // Если атакующий это стрела и стрела выпущена игроком
            // TODO; Если стрела достанет игрока в то время, когда игрок сменит лук на другое оружие - будет жопа
            material = Material.BOW; // BY ARROW TODO: Возможно придется в будущем заменить на Material.ARROW чтоб игроки не лупили простым луком
        } else {
            return -1; // WTF ?
        }

        WeaponSet weaponSet = main.getWeaponConfig().getWeaponSet(material);
        if (weaponSet == null) { // Если такого оружия не найдено в конфиге
            return 0;
        }

        // Возвращаем дамаг в зависимости от уровня вещи
        if (itemStack != null) {
            WeaponUtilBad weaponUtilBad = new WeaponUtilBad(itemStack, weaponSet);
            return weaponSet.getDamage(weaponUtilBad.getLevel());
        }
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
                WeaponUtilBad weaponUtilBad = new WeaponUtilBad(helmet, armorSet);
                procents += armorSet.getStrong(weaponUtilBad.getLevel());
            }
        }

        if (!isAir(chestplate)) {
            ArmorSet armorSet = main.getArmorConfig().getArmorSet(chestplate.getType());
            if (armorSet != null) {
                WeaponUtilBad weaponUtilBad = new WeaponUtilBad(chestplate, armorSet);
                procents += armorSet.getStrong(weaponUtilBad.getLevel());
            }
        }

        if (!isAir(leggins)) {
            ArmorSet armorSet = main.getArmorConfig().getArmorSet(leggins.getType());
            if (armorSet != null) {
                WeaponUtilBad weaponUtilBad = new WeaponUtilBad(leggins, armorSet);
                procents += armorSet.getStrong(weaponUtilBad.getLevel());
            }
        }

        if (!isAir(boots)) {
            ArmorSet armorSet = main.getArmorConfig().getArmorSet(boots.getType());
            if (armorSet != null) {
                WeaponUtilBad weaponUtilBad = new WeaponUtilBad(boots, armorSet);
                procents += armorSet.getStrong(weaponUtilBad.getLevel());
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

        //StringBuilder stringBuilder = new StringBuilder();

        // Если есть атакующий игрок
        if (playerAttacker != null) {
            //stringBuilder.append("Original damage: ").append(damage).append('\n');

            double attackerDamage = getDamage(event.getDamager());
            if (attackerDamage > 0) {
                damage += attackerDamage;
            }

            //stringBuilder.append("Bonus damage: ").append(attackerDamage).append('\n');
        }

        // Если есть игрок которого атакуют
        if (playerAttacked != null) {
            double attackedStrong = getStrong(event.getEntity());
            damage = damage - (damage * (attackedStrong / 100));

            //stringBuilder.append("Armor opponent: ").append(attackedStrong).append('\n');
        }

        /*if (playerAttacker != null) {
            stringBuilder.append("Result damage: ").append(damage);
            playerAttacker.sendMessage(stringBuilder.toString());
        }*/

        // Ну мало ли :DD
        if (damage < 0) damage = 0;

        event.setDamage(damage);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        if (player != null) {
            updateLore(player, true, event.getEntity().getType());
        }
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

    // ####################################
    //
    // События свящанные с обновлением вещи
    //
    // ####################################

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }

        // Обновляем описание вещи
        updateLore(event.getPlayer(), false, null);
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEnchant(PrepareItemEnchantEvent event) {
        /*if (event.isCancelled()) {
            event.setCancelled(!WeaponUtil.isEmptyEnchants(event.getItem()));
        }*/
    }

    private void updateLore(Player player, boolean upgrade, EntityType entityType) {
        int indexItem = upgrade ? randInt(1, 5) : 0;

        // Обновляем в руке
        ItemStack item = player.getItemInHand();
        
        // Обновляем броню
        PlayerInventory inventory = player.getInventory();
        ItemStack helmet = inventory.getHelmet();
        ItemStack chestplate = inventory.getChestplate();
        ItemStack leggins = inventory.getLeggings();
        ItemStack boots = inventory.getBoots();

        if (upgrade && (item != null || helmet != null || chestplate != null || leggins != null || boots != null)) {
            boolean hehe;
            do {
                hehe =  (item == null && indexItem == 1) ||
                        (helmet == null && indexItem == 2) ||
                        (chestplate == null && indexItem == 3) ||
                        (leggins == null && indexItem == 4) ||
                        (boots == null && indexItem == 5);
                if (hehe) {
                    indexItem++;
                    if (indexItem > 5) indexItem = 1;
                }
            } while(hehe);
        }
        
        updateLore(item, upgrade && (indexItem == 1), entityType);
        updateLore(helmet, upgrade && (indexItem == 2), entityType);
        updateLore(chestplate, upgrade && (indexItem == 3), entityType);
        updateLore(leggins, upgrade && (indexItem == 4), entityType);
        updateLore(boots, upgrade && (indexItem == 5), entityType);

        inventory.setHelmet(helmet);
        inventory.setChestplate(chestplate);
        inventory.setLeggings(leggins);
        inventory.setBoots(boots);
    }

    private void updateLore(ItemStack itemStack, boolean upgrade, EntityType entityType) {
        if (isAir(itemStack)) {
            return;
        }

        WeaponSet weaponSet = main.getWeaponConfig().getWeaponSet(itemStack.getType());
        ArmorSet armorSet = main.getArmorConfig().getArmorSet(itemStack.getType());
        if (!upgrade && itemStack.getItemMeta().hasLore()) {
            return;
        }

        if (weaponSet != null || armorSet != null) {
            ISet iSet = (weaponSet != null) ? weaponSet : armorSet;
            WeaponUtilBad weaponUtilBad = new WeaponUtilBad(itemStack, iSet);
            if (upgrade && entityType != null) {
                // TODO: Не забыть про лук, который игрок может сменить во время стрельбы
                MobSet mobSet = main.getMobConfig().getMobSet(entityType);
                if (mobSet != null) {
                    int level = weaponUtilBad.getLevel();
                    int exp = weaponUtilBad.getExp() + mobSet.getExp(); // increment exp
                    do {
                        if (exp < iSet.getIExp(level)) {
                            weaponUtilBad.setExp(exp);
                        } else if (level < iSet.getIMaxLevel()) {
                            exp = (int) (WeaponUtilBad.MIN_EXP + (exp - iSet.getIExp(level)));
                            level++;
                            weaponUtilBad.setExp(exp);
                            weaponUtilBad.setLevel(level);
                        } else {
                            weaponUtilBad.setExp((int) iSet.getIExp(level));
                            break;
                        }
                    } while(exp >= iSet.getIExp(level));
                }
            }
            weaponUtilBad.save();
        }
    }

    private int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
}
