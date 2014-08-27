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
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        player.sendMessage(String.format("HP: %f from %f", player.getHealth(), player.getMaxHealth()));

        ItemStack itemStack = player.getItemInHand();
        if (itemStack != null) {
            player.sendMessage(String.format("ITEM: %s : %d", itemStack.getType(), itemStack.getDurability()));
        }

        if (args.length >= 2 && args[0].equals("test") && itemStack != null) {
            itemStack.setDurability(Short.valueOf(args[1]));
            player.sendMessage("Complete!");
        }

        return true;
    }
}
