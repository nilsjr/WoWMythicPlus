package de.nilsdruyen.mythicplus.pages.mythicplus

import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.CharacterOverview
import de.nilsdruyen.mythicplus.character.models.Dungeon
import de.nilsdruyen.mythicplus.character.models.ScoreTier

data class MythicPlusState(
  val characterList: List<Character> = emptyList(),
  val dungeons: List<Dungeon> = emptyList(),
  val scoreTiers: List<ScoreTier> = emptyList(),
  val order: ListOrder = ListOrder.Score,
) {

  constructor(characterOverview: CharacterOverview) : this(
    characterList = characterOverview.characterList,
    dungeons = characterOverview.dungeons,
    scoreTiers = characterOverview.scoreTiers,
  )
}

sealed class MythicPlusIntent {

  data class Reorder(val order: ListOrder) : MythicPlusIntent()
}