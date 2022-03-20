package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.models.CharacterOverview
import de.nilsdruyen.mythicplus.character.models.InputCharacter

interface CharacterUsecase {

  suspend fun execute(inputList: List<InputCharacter>): CharacterOverview
}