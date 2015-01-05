package me.kapehh.BattleEquipControl;

import me.kapehh.BattleEquipControl.core.NodamageConfig;
import me.kapehh.BattleEquipControl.helpers.WeaponUtil;
import me.kapehh.BattleEquipControl.sets.*;
import me.kapehh.BattleEquipControl.upgrade.UpgradeManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

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
            WeaponUtil weaponUtil = new WeaponUtil(itemStack, weaponSet, player);
            return weaponSet.getDamage(weaponUtil.getLevel());
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
                WeaponUtil weaponUtil = new WeaponUtil(helmet, armorSet, player);
                procents += armorSet.getStrong(weaponUtil.getLevel());
            }
        }

        if (!isAir(chestplate)) {
            ArmorSet armorSet = main.getArmorConfig().getArmorSet(chestplate.getType());
            if (armorSet != null) {
                WeaponUtil weaponUtil = new WeaponUtil(chestplate, armorSet, player);
                procents += armorSet.getStrong(weaponUtil.getLevel());
            }
        }

        if (!isAir(leggins)) {
            ArmorSet armorSet = main.getArmorConfig().getArmorSet(leggins.getType());
            if (armorSet != null) {
                WeaponUtil weaponUtil = new WeaponUtil(leggins, armorSet, player);
                procents += armorSet.getStrong(weaponUtil.getLevel());
            }
        }

        if (!isAir(boots)) {
            ArmorSet armorSet = main.getArmorConfig().getArmorSet(boots.getType());
            if (armorSet != null) {
                WeaponUtil weaponUtil = new WeaponUtil(boots, armorSet, player);
                procents += armorSet.getStrong(weaponUtil.getLevel());
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

        EntityDamageEvent.DamageCause cause = event.getCause();
        Entity attacker = event.getDamager(); // Кто атакует
        Entity attacked = event.getEntity(); // Того кого атакуют

        /*System.out.println(event.getDamager() != null ? attacker.toString() : "Damager null");
        System.out.println(event.getEntity() != null ? attacked.toString() : "Entiter null");
        System.out.println(event.getCause().toString());*/

        Player playerAttacker = getFromEntity(attacker); // Кто атакует
        Player playerAttacked = getFromEntity(attacked); // Того кого атакуют
        double damage = event.getDamage();

        // шипы не действуют если у игрока лук, и на моба скелетона
        if (cause.equals(EntityDamageEvent.DamageCause.THORNS)) {
            if ((attacked instanceof Player) && playerAttacked.getItemInHand().getType().equals(Material.BOW)) {
                event.setDamage(0);
                event.setCancelled(true);
                return;
            } else if (attacked instanceof Skeleton) {
                event.setDamage(0);
                event.setCancelled(true);
                return;
            }
        }

        // Если игрок не бьет игрока и не стреляет луком, то остальной дамаг мы оставляем как есть
        if (!cause.equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)
            && !cause.equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
            return;
        }

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

    // Событие для заточки
    @EventHandler(priority = EventPriority.LOWEST)
    public void onCraftItem(CraftItemEvent event) {
        // если как-то непонятно тыкают, то экшен NOTHING (например с зажатым пробелом тыкают)
        if (event.getAction().equals(InventoryAction.NOTHING)) {
            return;
        }

        UpgradeManager upgradeManager = main.getUpgradeManager();
        if (upgradeManager.isRecipe(event.getRecipe())) {

            // Берем сам предмет item и материал для апгрейда
            CraftingInventory inventory = event.getInventory();
            Player player = (Player) event.getWhoClicked();
            ItemStack itemSource = inventory.getItem(5);
            ItemStack itemUpgrader = inventory.getItem(1);
            UpgradeSet upgradeSet = main.getUpgradeConfig().getUpgradeSet(itemUpgrader.getData());
            ISet iSet = main.getIset(itemSource);
            if (iSet == null || upgradeSet == null) {
                event.setResult(Event.Result.DENY);
                return; // все печально
            }

            int koef_exp;
            ItemStack tmp;
            int min = itemUpgrader.getAmount(); // количество в первой ячейки
            int c;

            // ищем минимальное количество
            for (int i = 2; i <= 9; i++) {
                if (i == 5) continue; // не нужон
                c = inventory.getItem(i).getAmount(); // количество в i-ой ячейки, кроме 5
                if (c < min) min = c;
            }

            if (min < 1) {
                event.setResult(Event.Result.DENY);
                return; // минимальное слишком не ок
            }

            // после того как нашли минимальное
            koef_exp = min; // коэфицент умножения экспы
            min--; // так как в событии и так вычтется один итем, то надо минимальное сократить
            if (min > 0) {
                for (int i = 1; i <= 9; i++) {
                    if (i == 5) continue; // не нужон
                    tmp = inventory.getItem(i);
                    //c = tmp.getAmount();
                    tmp.setAmount(tmp.getAmount() - min); // вычитаем минимально возможную херню
                }
            }

            WeaponUtil weaponUtil = new WeaponUtil(itemSource, iSet, player);
            int fails = 0, success = 0, res_koef = 0;
            double chanceFail = upgradeSet.getChanceFailUpgrade(weaponUtil.getLevel());

            for (int i = 1; i <= koef_exp; i++) {
                if (randDouble(0, 100) > chanceFail) {
                    success++;
                    res_koef++;
                } else {
                    fails++;
                    res_koef = 0;
                }
            }

            int exp = 0;
            if (fails > 0) { // если были неудачные заточки, то обнуляем
                weaponUtil.setExp(0);
                // чарим вещь, за то что обнулили её
                EnchantGroupSet enchantGroupSet = iSet.getEnchantGroupSet();
                if (enchantGroupSet != null) {
                    try {
                        enchantGroupSet.tryEnchant(itemSource, rand.nextInt(50));
                    } catch (EnchantGroupSet.EnchantException e) {
                        player.sendMessage(e.getMessage());
                    }
                    // т.к. в Meta хранится инфа о чарах, если не вызвать, чары после weaponUtil.save() пропадут
                    weaponUtil.updateMeta();
                }
                //itemSource.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 10);
                //System.out.println(itemSource.getEnchantments()); // todo
            }
            if (res_koef > 0) { // если коэфицент после последней последовательности удачных заточек есть, то качаем
                exp = upgradeSet.getExp() * res_koef;
                upgradeWeapon(weaponUtil, iSet, exp, true);
            }
            player.sendMessage(ChatColor.GOLD + "Заточка выполнена! Удачных: " + success + ", неудачных: " + fails + ". Получено опыта: " + exp);

            weaponUtil.save();
            event.setCurrentItem(itemSource);
            //System.out.println(itemSource.getEnchantments()); // todo
        }
    }

    // Событие для обновления описания предмета при простом использовании его
    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }

        // Обновляем описание вещи
        updateLore(event.getPlayer(), false, null);
    }

    // Обновление описания предметов игрока
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

        // Рандомно выбираем вещь у игрока
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

        // Обновляем итемы, а некоторые даже прокачиваем
        updateLore(player, item, upgrade && (indexItem == 1), entityType);
        updateLore(player, helmet, upgrade && (indexItem == 2), entityType);
        updateLore(player, chestplate, upgrade && (indexItem == 3), entityType);
        updateLore(player, leggins, upgrade && (indexItem == 4), entityType);
        updateLore(player, boots, upgrade && (indexItem == 5), entityType);

        // Записываем обновленные вещи
        inventory.setHelmet(helmet);
        inventory.setChestplate(chestplate);
        inventory.setLeggings(leggins);
        inventory.setBoots(boots);
    }

    // Обновляем вещь
    private void updateLore(Player player, ItemStack itemStack, boolean upgrade, EntityType entityType) {
        if (isAir(itemStack)) {
            return;
        }

        WeaponSet weaponSet = main.getWeaponConfig().getWeaponSet(itemStack.getType());
        ArmorSet armorSet = main.getArmorConfig().getArmorSet(itemStack.getType());

        if (weaponSet != null || armorSet != null) {
            ISet iSet = (weaponSet != null) ? weaponSet : armorSet;
            WeaponUtil weaponUtil = new WeaponUtil(itemStack, iSet, player);
            if (upgrade && entityType != null) {
                // TODO: Не забыть про лук, который игрок может сменить во время стрельбы
                MobSet mobSet = main.getMobConfig().getMobSet(entityType);
                if (mobSet != null) {
                    upgradeWeapon(weaponUtil, iSet, mobSet.getExp(), false);
                }
            }
            weaponUtil.save();
        }
    }

    // Низкоуровневое обновление экспы вещи
    private void upgradeWeapon(WeaponUtil weaponUtil, ISet iSet, int addExp, boolean upgrade) {
        int level = weaponUtil.getLevel();
        int exp = weaponUtil.getExp() + addExp; // increment exp
        int maxLevel = (upgrade ? iSet.getIMaxLevelUpgrade() : iSet.getIMaxLevel());
        boolean fe = false;

        do {
            // если уровень максимальный, то бб
            if (level >= maxLevel) {
                return;
            }

            if (exp < iSet.getIExp(level)) {
                weaponUtil.setExp(exp);
            } else if (level < maxLevel) {
                exp = (int) (WeaponUtil.MIN_EXP + (exp - iSet.getIExp(level)));
                level++;
                fe = (exp < iSet.getIExp(level)) && (level < maxLevel);
                weaponUtil.setLevel(level);
                weaponUtil.setExp(fe ? exp : 0);
            }/* else {
                weaponUtil.setExp((int) iSet.getIExp(level));
                break;
            }*/
        } while(exp >= iSet.getIExp(level));
        /*if (exp > 0) {

        }*/
    }

    private static Random rand = new Random();
    private int randInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }
    private double randDouble(double min, double max) {
        return min + rand.nextDouble() * (max - min);
    }
}
