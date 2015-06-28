package me.kapehh.BattleEquipControl;

import me.kapehh.BattleEquipControl.helpers.WeaponUtil;
import me.kapehh.BattleEquipControl.sets.ISet;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Karen on 26.08.2014.
 */
public class MainCommand implements CommandExecutor {
    Main main;

    public MainCommand(Main main) {
        this.main = main;
    }

    public int getAllExp(ItemStack itemStack, Player player) {
        ISet iSet = main.getIset(itemStack);
        WeaponUtil weaponUtil = new WeaponUtil(itemStack, iSet, player);
        int i, res = 0;
        for (i = 1; i < weaponUtil.getLevel(); i++) res += iSet.getIExp(i);
        return res + weaponUtil.getExp();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("convert")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only for players!");
                return true;
            }

            Player player = (Player) sender;
            ItemStack itemStack = player.getItemInHand();
            if (itemStack != null) {
                if (main.getIset(itemStack) != null) {
                    sender.sendMessage(main.getConvertConfig().getConvertForItem(itemStack, getAllExp(itemStack, (Player) sender)).toString());
                    //player.setItemInHand(null);
                } else {
                    player.sendMessage(ChatColor.RED + "Возьмите в руки оружие или броню!");
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Некорректные аргументы!");
        }
        return true;
    }
}
