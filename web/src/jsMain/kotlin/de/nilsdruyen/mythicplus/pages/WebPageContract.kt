package de.nilsdruyen.mythicplus.pages

import de.nilsdruyen.mythicplus.character.models.CharacterOverview
import de.nilsdruyen.mythicplus.character.models.InputCharacter

data class WebPageState(
  val isLoading: Boolean,
  val characterOverview: CharacterOverview = CharacterOverview(),
)

sealed class WebPageIntent {

  data class LoadData(val characterList: List<InputCharacter>) : WebPageIntent()
}