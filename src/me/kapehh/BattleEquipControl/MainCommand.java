package me.kapehh.BattleEquipControl;

import me.kapehh.BattleEquipControl.helpers.WeaponUtil;
import me.kapehh.BattleEquipControl.sets.ISet;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Karen on 26.08.2014.
 */
public class MainCommand implements CommandExecutor {
    Main main;
    boolean hasUnicornMailbox = false;

    public MainCommand(Main main) {
        this.main = main;
    }

    public boolean isHasUnicornMailbox() {
        return hasUnicornMailbox;
    }

    public void setHasUnicornMailbox(boolean hasUnicornMailbox) {
        this.hasUnicornMailbox = hasUnicornMailbox;
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
                    if (hasUnicornMailbox) {
                        me.kapehh.UnicornMailbox.Main unicornMailbox = me.kapehh.UnicornMailbox.Main.instance;
                        if (unicornMailbox != null) {
                            List<ItemStack> converts = main.getConvertConfig().getConvertForItem(itemStack, getAllExp(itemStack, player));
                            ItemStack[] arrConverts = converts.toArray(new ItemStack[converts.size()]);

                            player.setItemInHand(null);

                            unicornMailbox.getMailCore().sendItemsToPlayer(
                                    player.getName(),
                                    "BattleEquipControl::Convert",
                                    arrConverts
                            );

                            player.sendMessage(Main.getNormalMessage("Готово."));
                        } else {
                            main.getLogger().info("Error! UnicornMailbox is null!");
                        }
                    } else {
                        player.sendMessage(Main.getErrorMessage("Плагин почты не найден!"));
                    }
                } else {
                    player.sendMessage(Main.getErrorMessage("Возьмите в руки оружие или броню!"));
                }
            }
        } else {
            sender.sendMessage(Main.getErrorMessage("Некорректные аргументы!"));
            return false;
        }
        return true;
    }
}
