package me.kapehh.BattleEquipControl;

import me.kapehh.BattleEquipControl.core.NodamageConfig;
import me.kapehh.BattleEquipControl.helpers.WeaponUtil;
import me.kapehh.BattleEquipControl.helpers.WeaponUtilBad;
import me.kapehh.BattleEquipControl.sets.*;
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
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
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
     * TODO: У Лука учитывать силу натяжения (событие EntityShootBowEvent поле getForce)
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
        Player player;
        Material material;
        ItemStack itemStack = null;

        if (entity instanceof Player) { // Если атакующий - игрок
            player = (Player) entity;
            itemStack = player.getItemInHand();

            // Если в руках ничего нет
            if (isAir(itemStack)) {
                return -1;
            }

            material = itemStack.getType();
            if (material.equals(Material.BOW)) {
                return 0; // если игрок бьет луком, а им надо стрелять
            }
        } else if ((entity instanceof Arrow) && (((Arrow) entity).getShooter() instanceof Player)) { // Если атакующий это стрела и стрела выпущена игроком
            player = (Player) ((Arrow) entity).getShooter(); // Лол, удочка тоже идентифицируется как Projectile
            // TODO; Если стрела достанет игрока в то время, когда игрок сменит лук на другое оружие - будет жопа
            material = Material.BOW; // BY ARROW TODO: Возможно придется в будущем заменить на Material.ARROW чтоб игроки не лупили простым луком
        } else {
            return 0; // дамаг наносит не игрок
        }

        NodamageConfig nodamageConfig = main.getNodamageConfig();
        if (nodamageConfig.containsMaterial(material)) {
            return -1; // Вещи которыми бить нельзя
        }

        WeaponSet weaponSet = main.getWeaponConfig().getWeaponSet(material);
        if (weaponSet == null) { // Если такого оружия не найдено в конфиге
            return 0;
        }

        // Возвращаем дамаг в зависимости от уровня вещи
        if (itemStack != null) {
            WeaponUtilBad weaponUtilBad = new WeaponUtilBad(itemStack, weaponSet, player);
            return weaponSet.getDamage(weaponUtilBad.getLevel());
        }
        return weaponSet.getDamage(1);
    }

    private double getStrong(Entity entity) {
        Player player;

        if (entity instanceof Player) {
            player = (Player) entity;
        } else {
            return 0; // атакующий не игрок
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
                WeaponUtilBad weaponUtilBad = new WeaponUtilBad(helmet, armorSet, player);
                procents += armorSet.getStrong(weaponUtilBad.getLevel());
            }
        }

        if (!isAir(chestplate)) {
            ArmorSet armorSet = main.getArmorConfig().getArmorSet(chestplate.getType());
            if (armorSet != null) {
                WeaponUtilBad weaponUtilBad = new WeaponUtilBad(chestplate, armorSet, player);
                procents += armorSet.getStrong(weaponUtilBad.getLevel());
            }
        }

        if (!isAir(leggins)) {
            ArmorSet armorSet = main.getArmorConfig().getArmorSet(leggins.getType());
            if (armorSet != null) {
                WeaponUtilBad weaponUtilBad = new WeaponUtilBad(leggins, armorSet, player);
                procents += armorSet.getStrong(weaponUtilBad.getLevel());
            }
        }

        if (!isAir(boots)) {
            ArmorSet armorSet = main.getArmorConfig().getArmorSet(boots.getType());
            if (armorSet != null) {
                WeaponUtilBad weaponUtilBad = new WeaponUtilBad(boots, armorSet, player);
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

        // Если есть атакующий игрок
        if (playerAttacker != null) {
            double attackerDamage = getDamage(event.getDamager());
            if (attackerDamage > 0) {
                damage += attackerDamage;
            } else if (attackerDamage < 0) {
                damage = 0;
            }
        }

        // Если есть игрок которого атакуют
        if (playerAttacked != null) {
            double attackedStrong = getStrong(event.getEntity());
            damage = damage - (damage * (attackedStrong / 100));
        }

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

    // ####################################
    //
    // События связанные с обновлением вещи
    //
    // ####################################

    /*@EventHandler(priority = EventPriority.LOWEST)
    public void onShootBow(EntityShootBowEvent event) {
        if (event.isCancelled()) {
            return;
        }

        // event.getForce() - натяжение
    }*/

    /*@EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player playerAttacked = getFromEntity(event.getEntity()); // Того кого атакуют
    }*/

    /*@EventHandler(priority = EventPriority.LOWEST)
    public void onCraft(CraftItemEvent event) {

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEnchant(PrepareItemEnchantEvent event) {

    }*/

    /*@EventHandler(priority = EventPriority.LOWEST)
    public void onFurnaceBurnEvent(FurnaceBurnEvent event) {
        String str = "";
        str += event.getEventName() + "\r\n";
        str += event.getBurnTime() + "\r\n";
        str += event.getFuel().toString() + "\r\n";
        str += event.getBlock().toString() + "\r\n";
        System.out.println(str);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onFurnaceExtractEvent(FurnaceExtractEvent event) {
        String str = "";
        str += event.getEventName() + "\r\n";
        str += event.getBlock().toString() + "\r\n";
        str += event.getItemAmount() + "\r\n";
        str += event.getItemType().toString() + "\r\n";
        str += event.getPlayer().toString() + "\r\n";
        System.out.println(str);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onFurnaceSmeltEvent(FurnaceSmeltEvent event) {
        String str = "";
        str += event.getEventName() + "\r\n";
        str += event.getBlock().toString() + "\r\n";
        str += event.getSource().toString() + "\r\n";
        str += event.getResult().toString() + "\r\n";
        System.out.println(str);
    }*/

    @Deprecated
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEvent(InventoryClickEvent event) {
        AnvilInventory inventory;
        if (event.getInventory() instanceof AnvilInventory) {
            inventory = (AnvilInventory) event.getInventory();
        } else {
            // Если не наковальня - сразу нафиг
            return;
        }

        // Если событие - положить. И слот для крафта.
        if (event.getSlotType().equals(InventoryType.SlotType.RESULT) &&
            /*(event.getAction().equals(InventoryAction.PLACE_ALL) || event.getAction().equals(InventoryAction.PLACE_ONE)) &&*/
            (inventory.getItem(0) != null && inventory.getItem(1) != null)) {

            ItemStack currentUpgrader = inventory.getItem(0) == null ? event.getCursor() : inventory.getItem(0);
            ItemStack currentWeapon = inventory.getItem(1) == null ? event.getCursor() : inventory.getItem(1);

            UpgradeSet upgradeSet = main.getUpgradeConfig().getUpgradeSet(currentUpgrader.getType());
            WeaponSet weaponSet = main.getWeaponConfig().getWeaponSet(currentWeapon.getType());
            ArmorSet armorSet = main.getArmorConfig().getArmorSet(currentWeapon.getType());

            if (upgradeSet != null && (weaponSet != null || armorSet != null)) {
                Player player = (Player) event.getWhoClicked();
                ItemStack contents[] = player.getInventory().getContents();
                boolean isFull = true;
                int i = 0;

                for (ItemStack item : contents) {
                    if (item == null || item.getType().equals(Material.AIR)) {
                        isFull = false;
                        break;
                    }
                    i++;
                }

                if (isFull) {
                    player.sendMessage(ChatColor.RED + "Ваш инвентарь заполнен!");
                    return;
                }

                ISet iSet = (weaponSet != null) ? weaponSet : armorSet;
                WeaponUtilBad weaponUtilBad = new WeaponUtilBad(currentWeapon, iSet, player);

                if (randDouble(0, 100) > upgradeSet.getChanceFailUpgrade()) {
                    int exp = upgradeSet.getExp() * currentUpgrader.getAmount();
                    upgradeWeapon(weaponUtilBad, iSet, exp, true);
                    player.sendMessage(ChatColor.GREEN + "Заточка успешно выполнена! Получено опыта: " + exp);
                } else {
                    weaponUtilBad.setExp(0);
                    player.sendMessage(ChatColor.RED + "Заточка не удалась! Опыт был сброшен.");
                }

                weaponUtilBad.save();
                contents[i] = currentWeapon;
                inventory.clear();
                player.getInventory().setContents(contents);
                player.updateInventory();
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }

        // Обновляем описание вещи
        updateLore(event.getPlayer(), false, null);
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
        
        updateLore(player, item, upgrade && (indexItem == 1), entityType);
        updateLore(player, helmet, upgrade && (indexItem == 2), entityType);
        updateLore(player, chestplate, upgrade && (indexItem == 3), entityType);
        updateLore(player, leggins, upgrade && (indexItem == 4), entityType);
        updateLore(player, boots, upgrade && (indexItem == 5), entityType);

        inventory.setHelmet(helmet);
        inventory.setChestplate(chestplate);
        inventory.setLeggings(leggins);
        inventory.setBoots(boots);
    }

    private void updateLore(Player player, ItemStack itemStack, boolean upgrade, EntityType entityType) {
        if (isAir(itemStack)) {
            return;
        }

        WeaponSet weaponSet = main.getWeaponConfig().getWeaponSet(itemStack.getType());
        ArmorSet armorSet = main.getArmorConfig().getArmorSet(itemStack.getType());

        if (weaponSet != null || armorSet != null) {
            ISet iSet = (weaponSet != null) ? weaponSet : armorSet;
            WeaponUtilBad weaponUtilBad = new WeaponUtilBad(itemStack, iSet, player);
            if (upgrade && entityType != null) {
                // TODO: Не забыть про лук, который игрок может сменить во время стрельбы
                MobSet mobSet = main.getMobConfig().getMobSet(entityType);
                if (mobSet != null) {
                    upgradeWeapon(weaponUtilBad, iSet, mobSet.getExp(), false);
                }
            }
            weaponUtilBad.save();
        }
    }

    private void upgradeWeapon(WeaponUtilBad weaponUtilBad, ISet iSet, int addExp, boolean upgrade) {
        int level = weaponUtilBad.getLevel();
        int exp = weaponUtilBad.getExp() + addExp; // increment exp
        do {
            if (exp < iSet.getIExp(level)) {
                weaponUtilBad.setExp(exp);
            } else if (level < (upgrade ? iSet.getIMaxLevelUpgrade() : iSet.getIMaxLevel())) {
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

    private static Random rand = new Random();
    private int randInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }
    private double randDouble(double min, double max) {
        return min + rand.nextDouble() * (max - min);
    }
}
