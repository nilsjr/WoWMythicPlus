package de.nilsdruyen.mythicplus.character.models

sealed interface CharacterViewModel {

  object Loading : CharacterViewModel

  class CharacterOverview(
    val characterList: List<Character>,
    val currentAffixIds: List<Int>,
    val dungeons: List<Dungeon>,
    val scoreTiers: List<ScoreTier>,
  ) : CharacterViewModel

  class GearOverview(
    val characterList: List<Character>,
  ) : CharacterViewModel
}
