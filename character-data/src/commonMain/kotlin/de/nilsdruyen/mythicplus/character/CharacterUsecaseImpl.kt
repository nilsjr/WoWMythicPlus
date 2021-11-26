package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.models.CharacterViewModel
import de.nilsdruyen.mythicplus.character.models.InputCharacter

class CharacterUsecaseImpl(
  private val repository: RaiderIoRepository
) : CharacterUsecase {

  override suspend fun loadCharacterViewModel(inputList: List<InputCharacter>): CharacterViewModel {
    val dungeons = repository.getDungeons()
    val scoreTies = repository.getScoreTiers()
    val currentAffixes = repository.getCurrentAffixeIds()
    val characterList = repository.getCharacterList(inputList, scoreTies)
    return CharacterViewModel.CharacterOverview(characterList, currentAffixes, dungeons, scoreTies)
  }
}