package de.nilsdruyen.mythicplus.pages

import de.nilsdruyen.mythicplus.character.models.InputCharacter

sealed class PageState {

  object NoArguments : PageState()

  object InvalidArguments : PageState()

  class PageArguments(
    val characterList: List<InputCharacter>,
  ) : PageState()
}

enum class Page {
  MythicPlus,
  Raid,
  Gear,
}

fun String?.jumpTo(): Page {
  return when (this) {
    "gear" -> Page.Gear
    "m+" -> Page.MythicPlus
    "raid" -> Page.Raid
    else -> Page.MythicPlus
  }
}