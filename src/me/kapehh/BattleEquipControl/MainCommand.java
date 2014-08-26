package me.kapehh.BattleEquipControl;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Karen on 26.08.2014.
 */
public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            sender.sendMessage(String.format("HP: %f from %f", player.getHealth(), player.getMaxHealth()));
        }
        return true;
    }
}
