package me.kapehh.BattleEquipControl.bukkit_old;

import net.minecraft.server.v1_7_R3.Item;
import net.minecraft.server.v1_7_R3.ItemStack;
import net.minecraft.server.v1_7_R3.NBTTagCompound;
import net.minecraft.server.v1_7_R3.NBTTagList;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;

/**
 * Created by Karen on 31.08.2014.
 */
public class NBTAttrItemStack {

    private static Field fieldAttr;
    private static boolean initialized = false;

    private static void initialize() {
        if (initialized) {
            return;
        }

        try {
            Class craftMetaItem = Class.forName("org.bukkit.craftbukkit.v1_7_R3.inventory.CraftMetaItem");
            fieldAttr = craftMetaItem.getDeclaredField("attributes");
            fieldAttr.setAccessible(true);
        } catch (Exception e) {
            fieldAttr = null;
            e.printStackTrace();
        }

        initialized = true;
    }

    private NBTTagList attributes;

    public NBTAttrItemStack(org.bukkit.inventory.ItemStack itemStack) {
        initialize();

        try {
            ItemMeta itemMeta = itemStack.getItemMeta();
            attributes = (NBTTagList) fieldAttr.get(itemMeta);
            if (attributes == null) {
                System.out.println("WTF NAHOOJ! [1]");
                fieldAttr.set(itemMeta, new NBTTagList());
            }
            attributes = (NBTTagList) fieldAttr.get(itemMeta);
            if (attributes == null) {
                System.out.println("WTF NAHOOJ! [2]");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public boolean isValid() {
        return attributes != null;
    }

    public boolean hasKey(String key) {
        return tag.hasKey(key);
    }

    public void setInt(String key, int val) {
        tag.setInt(key, val);
    }

    public int getInt(String key) {
        return tag.getInt(key);
    }*/

    public void test() {
        NBTTagCompound asd = new NBTTagCompound();
        asd.setInt("Why", 1234);
        attributes.add(asd);

        System.out.println("From test: " + attributes.toString());
    }
}
