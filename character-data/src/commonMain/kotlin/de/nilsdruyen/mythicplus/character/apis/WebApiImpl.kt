package de.nilsdruyen.mythicplus.character.apis

import de.nilsdruyen.mythicplus.character.annotations.Inject
import de.nilsdruyen.mythicplus.character.entities.AffixesWebEntity
import de.nilsdruyen.mythicplus.character.entities.PeriodWebEntity
import de.nilsdruyen.mythicplus.character.entities.ProfileWebEntity
import de.nilsdruyen.mythicplus.character.entities.ScoreTierWebEntity
import de.nilsdruyen.mythicplus.character.entities.StaticDataWebEntity
import de.nilsdruyen.mythicplus.character.models.ScoreTier
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class WebApiImpl @Inject constructor(
  private val client: HttpClient
) : WebApi {

  override suspend fun getCharacter(realm: String, name: String): ProfileWebEntity =
    client.get("characters/profile") {
      parameter("region", "eu")
      parameter("realm", realm)
      parameter("name", name)
      parameter(
        "fields",
        "mythic_plus_best_runs:all,mythic_plus_alternate_runs:all," +
          "mythic_plus_scores_by_season:current,gear,mythic_plus_recent_runs"
      )
    }

  override suspend fun getCurrentAffixIds(): List<Int> = client.get<AffixesWebEntity>("mythic-plus/affixes") {
    parameter("region", "eu")
    parameter("locale", "en")
  }.details.map { it.id }

  override suspend fun getStaticData(): StaticDataWebEntity = client.get("mythic-plus/static-data") {
    parameter("expansion_id", "8")
  }

  override suspend fun getScoreTiers(): List<ScoreTier> =
    client.get<List<ScoreTierWebEntity>>("mythic-plus/score-tiers").map {
      ScoreTier(it.score, it.rgbHex)
    }

  override suspend fun getCurrentPeriod(): PeriodWebEntity = client.get("periods")
}