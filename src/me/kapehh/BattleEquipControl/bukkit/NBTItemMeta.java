package me.kapehh.BattleEquipControl.bukkit;

import net.minecraft.server.v1_7_R3.ItemStack;
import net.minecraft.server.v1_7_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;

import java.lang.reflect.Field;

/**
 * Created by Karen on 31.08.2014.
 */
public class NBTItemMeta {

    private static Field fieldHandle;
    private static boolean initialized = false;

    private static void initialize() {
        if (initialized) {
            return;
        }

        try {
            fieldHandle = CraftItemStack.class.getDeclaredField("handle");
            fieldHandle.setAccessible(true);
        } catch (Exception e) {
            fieldHandle = null;
            e.printStackTrace();
        }

        initialized = true;
    }

    private NBTTagCompound tag;

    public NBTItemMeta(org.bukkit.inventory.ItemStack itemStack) {
        initialize();

        try {
            ItemStack handle = (ItemStack) fieldHandle.get(itemStack);
            if (handle == null) {
                return;
            }
            if(handle.tag != null) {
                tag = handle.tag;
            } else {
                tag = new NBTTagCompound();
                handle.tag = tag;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isValid() {
        return tag != null;
    }

    public boolean hasKey(String key) {
        return tag.hasKey(key);
    }

    public void setInt(String key, int val) {
        tag.setInt(key, val);
    }

    public int getInt(String key) {
        return tag.getInt(key);
    }
}
