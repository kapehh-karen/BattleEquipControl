package me.kapehh.BattleEquipControl;

import me.kapehh.BattleEquipControl.bukkit.EnchantmentManager;
import me.kapehh.BattleEquipControl.helpers.WeaponUtilBad;
import me.kapehh.BattleEquipControl.sets.ArmorSet;
import me.kapehh.BattleEquipControl.sets.ISet;
import me.kapehh.BattleEquipControl.sets.WeaponSet;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Karen on 26.08.2014.
 */
public class MainCommand implements CommandExecutor {

    // TODO: Убрать эту команду вообще нахоой, или сделать что-то типа reload и забить

    Main main;

    public MainCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }


        Player player = (Player) sender;
        //player.sendMessage(String.format("HP: %f from %f", player.getHealth(), player.getMaxHealth()));

        if (!player.isOp()) {
            player.sendMessage("Пацанчик, не в тот район попал.");
            return true;
        }

        ItemStack itemStack = player.getItemInHand();
        /*if (itemStack != null) {
            player.sendMessage(String.format("ITEM: %s : %d", itemStack.getType(), itemStack.getDurability()));
        } else {
            return true;
        }*/

        if (args.length >= 2 && args[0].equals("damage") && (itemStack != null)) {
            itemStack.setDurability(Short.valueOf(args[1]));
            player.sendMessage("Complete!");
        } else if (args.length >= 2 && args[0].equals("level")) {
            updateLore(player, Integer.parseInt(args[1]));
            player.sendMessage("Шалость удалась!");
        }

        return true;
    }

    // TODO: Удалить все что ниже

    private boolean isAir(ItemStack itemStack) {
        return (itemStack == null || itemStack.getType().equals(Material.AIR));
    }

    private void updateLore(Player player, int level) {
        // Обновили в руке
        updateLore(player.getItemInHand(), level);

        // Обновляем броню
        PlayerInventory inventory = player.getInventory();
        ItemStack helmet = inventory.getHelmet();
        ItemStack chestplate = inventory.getChestplate();
        ItemStack leggins = inventory.getLeggings();
        ItemStack boots = inventory.getBoots();

        updateLore(helmet, level);
        updateLore(chestplate, level);
        updateLore(leggins, level);
        updateLore(boots, level);

        inventory.setHelmet(helmet);
        inventory.setChestplate(chestplate);
        inventory.setLeggings(leggins);
        inventory.setBoots(boots);
    }

    private void updateLore(ItemStack itemStack, int lvl) {
        if (isAir(itemStack)) {
            return;
        }

        WeaponSet weaponSet = main.getWeaponConfig().getWeaponSet(itemStack.getType());
        ArmorSet armorSet = main.getArmorConfig().getArmorSet(itemStack.getType());

        if (weaponSet != null || armorSet != null) {
            ISet iSet = (weaponSet != null) ? weaponSet : armorSet;
            WeaponUtilBad weaponUtilBad = new WeaponUtilBad(itemStack, iSet);
            weaponUtilBad.setExp(0);
            weaponUtilBad.setLevel(lvl);
            weaponUtilBad.save();
        }
    }
}
