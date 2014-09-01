package me.kapehh.BattleEquipControl;

import me.kapehh.BattleEquipControl.bukkit.EnchantmentManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Karen on 26.08.2014.
 */
public class MainCommand implements CommandExecutor {

    // TODO: Убрать эту команду вообще нахоой, или сделать что-то типа reload и забить

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
        } else if (args.length >= 1 && args[0].equals("nbt")) {

            itemStack.addUnsafeEnchantment(EnchantmentManager.ENCHANT_EXP, 1);
            itemStack.addUnsafeEnchantment(EnchantmentManager.ENCHANT_LEVEL, 1);

            /*NBTItemMeta nbtItemMeta = new NBTItemMeta(itemStack);
            if (nbtItemMeta.isValid()) {
                if (nbtItemMeta.hasKey("BecTestLevel")) {
                    player.sendMessage("Has BecTestLevel: " + nbtItemMeta.getInt("BecTestLevel"));
                } else {
                    Random random = new Random();
                    int r = random.nextInt(1000);
                    nbtItemMeta.setInt("BecTestLevel", r);
                    player.sendMessage("Set BecTestLevel: " + r);
                }
            }*/

        } else if (args.length >= 1 && args[0].equals("meta")) {

            ItemMeta itemMeta = itemStack.getItemMeta();
            player.sendMessage(itemMeta.toString());
            itemStack.setItemMeta(itemMeta);

            //NBTItemMeta nbtAttrItemStack = new NBTItemMeta(itemStack);
            //nbtAttrItemStack.view();


        }

        return true;
    }
}
