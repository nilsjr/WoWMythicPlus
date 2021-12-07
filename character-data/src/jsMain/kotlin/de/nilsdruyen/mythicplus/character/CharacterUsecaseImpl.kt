package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.models.InputCharacter
import de.nilsdruyen.mythicplus.character.models.WebViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class CharacterUsecaseImpl(private val repository: RaiderIoRepository) : CharacterUsecase {

  private val state = MutableStateFlow<WebViewModel>(WebViewModel.Loading)

  override fun viewModel(): Flow<WebViewModel> = state

  override suspend fun setInput(inputList: List<InputCharacter>) {
    val dungeons = repository.getDungeons()
    val scoreTiers = repository.getScoreTiers()
    val currentAffixes = repository.getCurrentAffixeIds()
    val characterList = repository.getCharacterList(inputList)
    state.value = WebViewModel.CharacterOverview(characterList, currentAffixes, dungeons, scoreTiers)
  }
}