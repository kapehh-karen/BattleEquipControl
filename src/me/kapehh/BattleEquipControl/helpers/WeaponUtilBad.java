package me.kapehh.BattleEquipControl.helpers;

import me.kapehh.BattleEquipControl.sets.ISet;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karen on 01.09.2014.
 */
public class WeaponUtilBad {
    public static final int CURRENT_VERSION = 8;
    public static final int MIN_LEVEL = 1;
    public static final int MIN_EXP = 0;
    public static final String PREFFIX = ChatColor.RESET + "";

    private ISet iSet;
    private Player player;
    private ItemStack itemStack;
    private ItemMeta itemMeta;

    private int exp;
    private int level;
    private int version;
    private String createdBy;

    /*public WeaponUtilBad(ItemStack itemStack, ISet iSet) {
        this(itemStack, iSet, null);
    }*/

    public WeaponUtilBad(ItemStack itemStack, ISet iSet, Player player) {
        this.iSet = iSet;
        this.player = player;
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
        if (!itemMeta.hasLore()) {
            itemMeta.setLore(new ArrayList<String>());
        }
        load();
    }

    public String getBar() {
        StringBuilder stringBuilder = new StringBuilder().append(PREFFIX).append('[');
        int p = (int) (exp * 100 / iSet.getIExp(level));
        for (int i = 1; i <= 10; i++, p -= 10) {
            if (p > 10) {
                stringBuilder.append(ChatColor.RED).append(ChatColor.BOLD).append('|');
            } else {
                stringBuilder.append(ChatColor.DARK_GRAY).append(ChatColor.BOLD).append('|');
            }
        }
        return stringBuilder.append(PREFFIX).append(']').toString();
    }

    private String getPlayerName() {
        return (player != null) ? player.getName() : "-";
    }

    private void update(String m, String p[]) {
        update(m, p, 0, false);
    }

    private void update(String m, String p[], int v, boolean simple_update) {
        int ver = v;

        if (!simple_update) {

            if (m.matches(".*:\\d+,\\d+,\\d+") && (ver = Integer.parseInt(p[2])) == 6) { // обновление с 6 версии
                exp = Integer.parseInt(p[0]);
                level = Integer.parseInt(p[1]);
            } else if (m.matches(".*:\\d+,\\d+,\\d+,\\w+") && (ver = Integer.parseInt(p[2])) == 7) { // обновление с 7 версии
                exp = Integer.parseInt(p[0]);
                level = Integer.parseInt(p[1]);
                createdBy = p[3];
            }

        }

        // без break, чтоб нормально обновить
        switch (ver) {

            case 6:
                createdBy = getPlayerName();

            case 7:
                if (createdBy.equalsIgnoreCase("unknown")) {
                    createdBy = getPlayerName();
                }

                break; // все, хватит с обновлениями

            default:
                clear();
                return;

        }

        version = CURRENT_VERSION;
    }

    public void load() {
        List<String> lore = itemMeta.getLore();
        if (lore.size() < 1) {
            clear();
            return;
        }

        String m = lore.get(0);
        if (m.indexOf(':') < 0) {
            clear();
            return;
        }
        String p[] = m.substring(m.lastIndexOf(':') + 1).split(",");

        if (!m.matches(".*:\\d+,\\d+,\\d+,\\w+")) {
            update(m, p);
            return;
        }

        exp = Integer.parseInt(p[0]);
        level = Integer.parseInt(p[1]);
        version = Integer.parseInt(p[2]);
        createdBy = p[3];

        if (version != CURRENT_VERSION) {
            update(m, p, version, true);
        }
    }

    public void save() {
        List<String> lore = new ArrayList<String>();
        lore.add(String.format("%s:%d,%d,%d,%s", ChatColor.BLACK, exp, level, version, createdBy));

        double bonus = ((long) (iSet.getIBonus(level) * 100)) / 100.0;
        lore.add(PREFFIX + "Level: " + level);
        lore.add(PREFFIX + "Exp: " + getBar());
        lore.add(PREFFIX + iSet.getIBonusName() + ": " + bonus);
        lore.add(PREFFIX + ChatColor.GRAY + "" + ChatColor.ITALIC + "by " + createdBy);

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }

    public void clear() {
        exp = MIN_EXP;
        level = MIN_LEVEL;
        version = CURRENT_VERSION;
        createdBy = getPlayerName();
        save();
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public String toString() {
        return "WeaponUtil{" +
                "itemStack=" + itemStack +
                ", exp=" + exp +
                ", level=" + level +
                ", version=" + version +
                '}';
    }
}
