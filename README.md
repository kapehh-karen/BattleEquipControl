BattleEquipControl
==================

Плагин для настройки урона оружия и защиты доспехов.

<b>Конфиг:</b>
<pre>
ARMOR:
    
  . . .
  
  LEATHER_HELMET:
    max_level: 10
    eval_level_strong: lvl * 0.3
  
  . . .
    
WEAPONS:
  
  . . .
  
  WOOD_SWORD:
    max_level: 10
    eval_level_damage: 5 + lvl * 2
  
  . . .
  
</pre>

Блок ARMOR отвечает за доспех. Блок WEAPONS за оружие.
Каждый элемент блока имеет <code>max_level</code> - максимальный уровень предмета для этой вещи, и <code>eval_level_***</code> - вычисляемое выражение в котором lvl это текущий уровень предмета.
