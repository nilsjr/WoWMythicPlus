package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.models.CharacterViewModel
import de.nilsdruyen.mythicplus.character.models.InputCharacter

interface CharacterUsecase {

  suspend fun loadCharacterViewModel(inputList: List<InputCharacter>): CharacterViewModel
}