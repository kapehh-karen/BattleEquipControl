BattleEquipControl
==================

Плагин для настройки урона оружия и защиты доспехов.

<b>Конфиг:</b>
<pre>
# В формулах, переменная lvl заменяется на уровень оружия или брони. Диапазон [1, 2, ... N - 1, N]
MAIN:
    max_level: 10 # максимальный уровень при килах
    max_level_upgrade: 15 # максимальный уровень при заточке
    eval_exp: Math.exp(lvl) # вычисляемый опыт который надо заработать
    chance_fail_upgrade: 10 # шанс сброса опыта при заточке
UPGRADE: # опыт даваемый за заточку
    EMERALD:
        exp: 1
    DIAMOND:
        exp: 1
    IRON_INGOT:
        exp: 1
    GOLD_INGOT:
        exp: 1
    INK_SACK: # лазурит
        exp: 1
    REDSTONE: # редстоун
        exp: 1
MOBS:
    PLAYER:
        exp: 5
    WOLF:
        exp: 1
    MAGMA_CUBE:
        exp: 5
    BLAZE:
        exp: 5
    WITCH:
        exp: 5
    CAVE_SPIDER:
        exp: 5
    ENDERMAN:
        exp: 20
    PIG_ZOMBIE:
        exp: 10
    GHAST:
        exp: 20
    SLIME:
        exp: 3
    ZOMBIE:
        exp: 2
    SPIDER:
        exp: 3
    SKELETON:
        exp: 2
    CREEPER:
        exp: 3
    ENDER_DRAGON:
        exp: 100
ARMOR:
    LEATHER_HELMET:
        eval_level_strong: lvl * 0.1
    IRON_HELMET:
        eval_level_strong: lvl * 0.2
    GOLD_HELMET:
        eval_level_strong: lvl * 0.4
    DIAMOND_HELMET:
        eval_level_strong: lvl * 0.5
    CHAINMAIL_HELMET:
        eval_level_strong: lvl * 0.3
    LEATHER_CHESTPLATE:
        eval_level_strong: lvl * 0.1
    IRON_CHESTPLATE:
        eval_level_strong: lvl * 0.2
    GOLD_CHESTPLATE:
        eval_level_strong: lvl * 0.4
    DIAMOND_CHESTPLATE:
        eval_level_strong: lvl * 0.5
    CHAINMAIL_CHESTPLATE:
        eval_level_strong: lvl * 0.3
    LEATHER_LEGGINGS:
        eval_level_strong: lvl * 0.1
    IRON_LEGGINGS:
        eval_level_strong: lvl * 0.2
    GOLD_LEGGINGS:
        eval_level_strong: lvl * 0.4
    DIAMOND_LEGGINGS:
        eval_level_strong: lvl * 0.5
    CHAINMAIL_LEGGINGS:
        eval_level_strong: lvl * 0.3
    LEATHER_BOOTS:
        eval_level_strong: lvl * 0.1
    IRON_BOOTS:
        eval_level_strong: lvl * 0.2
    GOLD_BOOTS:
        eval_level_strong: lvl * 0.4
    DIAMOND_BOOTS:
        eval_level_strong: lvl * 0.5
    CHAINMAIL_BOOTS:
        eval_level_strong: lvl * 0.3
WEAPONS:
    BOW:
        eval_level_damage: 5 + lvl * 2
    WOOD_SWORD:
        eval_level_damage: 1 + lvl * 2
    STONE_SWORD:
        eval_level_damage: 2 + lvl * 2
    IRON_SWORD:
        eval_level_damage: 4 + lvl * 2
    GOLD_SWORD:
        eval_level_damage: 7 + lvl * 2
    DIAMOND_SWORD:
        eval_level_damage: 9 + lvl * 2
    WOOD_SPADE:
        eval_level_damage: 1 + lvl * 2
    STONE_SPADE:
        eval_level_damage: 2 + lvl * 2
    IRON_SPADE:
        eval_level_damage: 4 + lvl * 2
    GOLD_SPADE:
        eval_level_damage: 7 + lvl * 2
    DIAMOND_SPADE:
        eval_level_damage: 9 + lvl * 2
    WOOD_AXE:
        eval_level_damage: 2 + lvl * 2
    STONE_AXE:
        eval_level_damage: 4 + lvl * 2
    IRON_AXE:
        eval_level_damage: 6 + lvl * 2
    GOLD_AXE:
        eval_level_damage: 8 + lvl * 2
    DIAMOND_AXE:
        eval_level_damage: 10 + lvl * 2
NODAMAGE: [FISHING_ROD, STICK]
</pre>

Блок ARMOR отвечает за доспех. Блок WEAPONS за оружие. Блок MOBS за опыт от убийства мобов
Каждый элемент блока имеет <code>max_level</code> - максимальный уровень предмета для этой вещи, и <code>eval_level_***</code> - вычисляемое выражение в котором lvl это текущий уровень предмета.
Блок NODAMAGE содержит перечисление вещей, которые не будут наносить дамага.

<b>Временно в плагине:</b>
<ul>
<li>Команда <code>/bec level N</code> качающая снаряжение ваше</li>
</ul>
