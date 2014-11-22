package me.kapehh.BattleEquipControl;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Karen on 26.08.2014.
 */
public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        ItemStack itemStack = ((Player) sender).getItemInHand();
        if (itemStack != null) {
            sender.sendMessage(itemStack.getType().toString());
            sender.sendMessage(itemStack.getData().toString());
        }
        return true;
    }
}
