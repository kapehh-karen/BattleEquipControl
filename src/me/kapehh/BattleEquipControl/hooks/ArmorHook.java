package me.kapehh.BattleEquipControl.hooks;

import net.minecraft.server.v1_7_R4.Item;
import net.minecraft.server.v1_7_R4.ItemArmor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * Created by Karen on 25.06.2015.
 */
public class ArmorHook {
    // Имя поля в котором содержится коэффициент защиты брони
    private static final String armorFieldName = "c";

    private HashMap<ItemArmor, Integer> oldValues = new HashMap<ItemArmor, Integer>();

    private Field armorValueField;

    public ArmorHook() throws SecurityException, NoSuchFieldException, IllegalAccessException{
        Class<ItemArmor> itemArmor = ItemArmor.class;
        armorValueField = itemArmor.getField(armorFieldName);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(armorValueField, armorValueField.getModifiers() & ~Modifier.FINAL);
    }

    public void offAll() {
        // leather
        modifyArmorValue(298, 0);
        modifyArmorValue(299, 0);
        modifyArmorValue(300, 0);
        modifyArmorValue(301, 0);

        // chainmail
        modifyArmorValue(302, 0);
        modifyArmorValue(303, 0);
        modifyArmorValue(304, 0);
        modifyArmorValue(305, 0);

        // iron
        modifyArmorValue(306, 0);
        modifyArmorValue(307, 0);
        modifyArmorValue(308, 0);
        modifyArmorValue(309, 0);

        // diamond
        modifyArmorValue(310, 0);
        modifyArmorValue(311, 0);
        modifyArmorValue(312, 0);
        modifyArmorValue(313, 0);

        // golden
        modifyArmorValue(314, 0);
        modifyArmorValue(315, 0);
        modifyArmorValue(316, 0);
        modifyArmorValue(317, 0);
    }

    public void modifyArmorValue(int id, int value){
        Item i = Item.getById(id);
        if(i instanceof ItemArmor){
            int val;
            try {
                if (oldValues.containsKey(i)) {
                    oldValues.remove(i);
                }
                val = armorValueField.getInt(i);
                armorValueField.set(i, value);
                oldValues.put((ItemArmor) i, val);
                // TODO: Remove
                System.out.printf("Set armor def " + i.getName() + " to " + value + " (old value: " + val + ")");
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("Invalid armor ID: " + id);
        }
    }

    public void restore(){
        for(ItemArmor i : oldValues.keySet()){
            try {
                // TODO: Remove
                System.out.println("Restoring " + i.getName() + " to " + oldValues.get(i));
                armorValueField.set(i, oldValues.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        oldValues.clear();
    }
}
