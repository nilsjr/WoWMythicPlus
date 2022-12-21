package de.nilsdruyen.mythicplus.extensions

import de.nilsdruyen.mythicplus.character.enums.WoWClass
import de.nilsdruyen.mythicplus.styles.ColorConst

fun WoWClass.color() = when (this) {
  WoWClass.DEATH_KNIGHT -> ColorConst.Classes.DK
  WoWClass.DEMON_HUNTER -> ColorConst.Classes.DH
  WoWClass.DRUID -> ColorConst.Classes.DR
  WoWClass.EVOKER -> ColorConst.Classes.EV
  WoWClass.HUNTER -> ColorConst.Classes.HU
  WoWClass.MAGE -> ColorConst.Classes.MA
  WoWClass.MONK -> ColorConst.Classes.MO
  WoWClass.PALADIN -> ColorConst.Classes.PA
  WoWClass.PRIEST -> ColorConst.Classes.PR
  WoWClass.ROGUE -> ColorConst.Classes.RO
  WoWClass.SHAMAN -> ColorConst.Classes.SH
  WoWClass.WARLOCK -> ColorConst.Classes.WL
  WoWClass.WARRIOR -> ColorConst.Classes.WR
  WoWClass.UNKNOWN -> ColorConst.Classes.WR
}