package de.nilsdruyen.mythicplus.character.models

data class CharacterOverview(
  val characterList: List<Character> = emptyList(),
  val dungeons: List<Dungeon> = emptyList(),
  val scoreTiers: List<ScoreTier> = emptyList(),
  val raid: Raid? = null,
)