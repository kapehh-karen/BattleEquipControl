package me.kapehh.BattleEquipControl;

import me.kapehh.BattleEquipControl.bukkit.NBTItemMeta;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

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
        } else {
            return true;
        }

        if (args.length >= 2 && args[0].equals("damage")) {
            itemStack.setDurability(Short.valueOf(args[1]));
            player.sendMessage("Complete!");
        } else if (args.length >= 1 && args[0].equals("meta")) {
            /*net.minecraft.server.v1_7_R3.ItemStack nms = CraftItemStack.asNMSCopy(itemStack);
            NBTTagCompound tag = nms.tag;*/

            NBTItemMeta nbtItemMeta = new NBTItemMeta(itemStack);
            if (nbtItemMeta.isValid()) {
                if (nbtItemMeta.hasKey("BecTestLevel")) {
                    player.sendMessage("Has BecTestLevel: " + nbtItemMeta.getInt("BecTestLevel"));
                } else {
                    Random random = new Random();
                    int r = random.nextInt(1000);
                    nbtItemMeta.setInt("BecTestLevel", r);
                    player.sendMessage("Set BecTestLevel: " + r);
                }
            }

            /*CraftItemStack craftItemStack = (CraftItemStack) itemStack;

            try {
                Class c = craftItemStack.getClass();
                Field nameField = c.getDeclaredField("handle");
                nameField.setAccessible(true);
                net.minecraft.server.v1_7_R3.ItemStack handle = (net.minecraft.server.v1_7_R3.ItemStack) nameField.get(craftItemStack);

                NBTTagCompound tag;
                if(handle.tag != null)
                    tag = handle.tag;
                else
                {
                    handle.tag = new NBTTagCompound();
                    tag = handle.tag;
                }

                if (tag.hasKey("MySuperLevel")) {
                    player.sendMessage("HAS KEY: " + tag.getInt("MySuperLevel"));
                }
                tag.setInt("MySuperLevel", 228);
                player.sendMessage(craftItemStack.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }*/

        }

        return true;
    }
}
