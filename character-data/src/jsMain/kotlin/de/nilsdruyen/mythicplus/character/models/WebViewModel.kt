package de.nilsdruyen.mythicplus.character.models

sealed interface WebViewModel {

  object Loading : WebViewModel

  class CharacterOverview(
    val characterList: List<Character>,
    val currentAffixIds: List<Int>,
    val dungeons: List<Dungeon>,
    val scoreTiers: List<ScoreTier>,
  ) : WebViewModel

  class GearOverview(
    val characterList: List<Character>,
  ) : WebViewModel
}
