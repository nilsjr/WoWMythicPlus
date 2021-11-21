package de.nilsdruyen.mythicplus.pages

sealed class Page {

  object MythicPlus : Page()
  object Gear : Page()
}

fun String?.jumpTo(): Page {
  return when (this) {
    "gear" -> Page.Gear
    "m+" -> Page.MythicPlus
    else -> Page.MythicPlus
  }
}