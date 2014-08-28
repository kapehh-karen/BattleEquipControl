package me.kapehh.BattleEquipControl;

import net.minecraft.server.v1_7_R3.NBTTagCompound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;
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

        if (args.length >= 2 && args[0].equals("damage") && itemStack != null) {
            itemStack.setDurability(Short.valueOf(args[1]));
            player.sendMessage("Complete!");
        } else if (args.length >= 1 && args[0].equals("view")) {
            /*net.minecraft.server.v1_7_R3.ItemStack nms = CraftItemStack.asNMSCopy(itemStack);
            NBTTagCompound tag = nms.tag;*/
        }

        return true;
    }
}
