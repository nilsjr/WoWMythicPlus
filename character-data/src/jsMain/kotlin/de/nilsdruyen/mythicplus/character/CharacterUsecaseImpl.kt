package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.models.CharacterOverview
import de.nilsdruyen.mythicplus.character.models.InputCharacter

class CharacterUsecaseImpl(private val repository: RaiderIoRepository) : CharacterUsecase {

  override suspend fun execute(inputList: List<InputCharacter>): CharacterOverview {
    val dungeons = repository.getDungeons()
    val scoreTiers = repository.getScoreTiers()
    val currentAffixes = repository.getCurrentAffixeIds()
    val characterList = repository.getCharacterList(inputList)
    return CharacterOverview(characterList, currentAffixes, dungeons, scoreTiers)
  }
}