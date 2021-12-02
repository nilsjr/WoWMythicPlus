package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.models.InputCharacter
import de.nilsdruyen.mythicplus.character.models.WebViewModel
import kotlinx.coroutines.flow.Flow

interface CharacterUsecase {

  fun viewModel(): Flow<WebViewModel>

  suspend fun setInput(inputList: List<InputCharacter>)
}