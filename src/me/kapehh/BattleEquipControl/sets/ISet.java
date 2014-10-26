package me.kapehh.BattleEquipControl.sets;

/**
 * Created by Karen on 01.09.2014.
 */
public interface ISet {
    public int getIMaxLevel();
    public int getIMaxLevelUpgrade();
    public double getIExp(int level);
    public double getIBonus(int level);
    public String getIBonusName();
}
