BattleEquipControl
==================

Плагин для настройки урона оружия и защиты доспехов.

<b>Конфиг:</b>
<pre>
MOBS:

  . . .
  
  WOLF:
    exp: 1
  
  . . .

ARMOR:
    
  . . .
  
  LEATHER_HELMET:
    max_level: 10
    eval_exp: Math.exp(lvl)
    eval_level_strong: lvl * 0.3
  
  . . .
    
WEAPONS:
  
  . . .
  
  WOOD_SWORD:
    max_level: 10
    eval_exp: Math.exp(lvl)
    eval_level_damage: 5 + lvl * 2
  
  . . .
  
NODAMAGE: [FISHING_ROD, STICK]
</pre>

Блок ARMOR отвечает за доспех. Блок WEAPONS за оружие. Блок MOBS за опыт от убийства мобов
Каждый элемент блока имеет <code>max_level</code> - максимальный уровень предмета для этой вещи, и <code>eval_level_***</code> - вычисляемое выражение в котором lvl это текущий уровень предмета.
Блок NODAMAGE содержит перечисление вещей, которые не будут наносить дамага.

<b>Временно в плагине:</b>
<ul>
<li>Команда <code>/bec level N</code> качающая снаряжение ваше</li>
</ul>
