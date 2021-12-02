package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.models.WebViewModel
import de.nilsdruyen.mythicplus.character.models.InputCharacter
import kotlinx.coroutines.flow.Flow

interface CharacterUsecase {

  fun viewModel(): Flow<WebViewModel>

  suspend fun setInput(inputList: List<InputCharacter>)
}