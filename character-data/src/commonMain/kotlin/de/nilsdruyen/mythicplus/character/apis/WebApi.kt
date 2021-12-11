package de.nilsdruyen.mythicplus.character.apis

import de.nilsdruyen.mythicplus.character.entities.PeriodWebEntity
import de.nilsdruyen.mythicplus.character.entities.ProfileWebEntity
import de.nilsdruyen.mythicplus.character.entities.StaticDataWebEntity
import de.nilsdruyen.mythicplus.character.models.ScoreTier

interface WebApi {

  suspend fun getCharacter(realm: String, name: String): ProfileWebEntity

  suspend fun getCurrentAffixIds(): List<Int>

  suspend fun getStaticData(): StaticDataWebEntity

  suspend fun getScoreTiers(): List<ScoreTier>

  suspend fun getCurrentPeriod(): PeriodWebEntity
}