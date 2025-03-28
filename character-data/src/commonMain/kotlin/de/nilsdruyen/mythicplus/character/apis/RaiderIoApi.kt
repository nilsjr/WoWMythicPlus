package de.nilsdruyen.mythicplus.character.apis

import de.nilsdruyen.mythicplus.character.entities.PeriodWebEntity
import de.nilsdruyen.mythicplus.character.entities.ProfileWebEntity
import de.nilsdruyen.mythicplus.character.entities.RaidStaticDataWebEntity
import de.nilsdruyen.mythicplus.character.entities.StaticDataWebEntity
import de.nilsdruyen.mythicplus.character.models.ScoreTier

interface RaiderIoApi {

  suspend fun getCharacter(realm: String, name: String): ProfileWebEntity

  suspend fun getCurrentPeriod(): PeriodWebEntity

  suspend fun getStaticData(): StaticDataWebEntity

  suspend fun getScoreTiers(): List<ScoreTier>

  suspend fun getRaidStaticData(): RaidStaticDataWebEntity
}