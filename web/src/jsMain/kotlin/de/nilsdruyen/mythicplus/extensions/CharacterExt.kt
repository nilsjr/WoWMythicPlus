package de.nilsdruyen.mythicplus.extensions

import de.nilsdruyen.mythicplus.character.models.Item
import de.nilsdruyen.mythicplus.styles.ColorConst

fun Item.colorForItemLvl(): String = when {
  isLegendary -> ColorConst.Gear.ORANGE
  level > 184 -> if (level == 230) ColorConst.Gear.BLUE else ColorConst.Gear.PURPLE
  level > 130 -> ColorConst.Gear.BLUE
  else -> ColorConst.Gear.GREEN
}