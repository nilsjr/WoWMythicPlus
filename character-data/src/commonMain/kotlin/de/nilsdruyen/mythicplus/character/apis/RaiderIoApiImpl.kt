package de.nilsdruyen.mythicplus.character.apis

import de.nilsdruyen.mythicplus.character.entities.GearWebEntity
import de.nilsdruyen.mythicplus.character.entities.PeriodWebEntity
import de.nilsdruyen.mythicplus.character.entities.ProfileRequest
import de.nilsdruyen.mythicplus.character.entities.ProfileWebEntity
import de.nilsdruyen.mythicplus.character.entities.RaidStaticDataWebEntity
import de.nilsdruyen.mythicplus.character.entities.ScoreTierWebEntity
import de.nilsdruyen.mythicplus.character.entities.StaticDataWebEntity
import de.nilsdruyen.mythicplus.character.enums.Role
import de.nilsdruyen.mythicplus.character.models.ScoreTier
import de.nilsdruyen.mythicplus.character.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.JsonConvertException
import io.ktor.client.plugins.resources.get as resGet

class RaiderIoApiImpl(
  private val client: HttpClient
) : RaiderIoApi {

  override suspend fun getCharacter(realm: String, name: String): ProfileWebEntity {
    return try {
      client.resGet(ProfileRequest(realm, name, Constants.Api.Fields.all())).body()
    } catch (e: JsonConvertException) {
      ProfileWebEntity(
        name = name,
        clazz = "",
        race = "",
        realm = realm,
        region = "eu",
        spec = "",
        role = Role.TANK,
        thumbnailUrl = "https://duckduckgo.com/?q=non",
        profileUrl = "http://www.bing.com/search?q=reprehendunt",
        bestRuns = listOf(),
        scoreBySeason = listOf(),
        recentRuns = listOf(),
        gear = GearWebEntity(iLvlEquipped = -1.0, items = mapOf())
      )
    }
  }

  override suspend fun getCurrentPeriod(): PeriodWebEntity = client.get("periods").body()

  override suspend fun getStaticData(): StaticDataWebEntity = client.get("mythic-plus/static-data") {
    parameter("expansion_id", Constants.EXPANSION)
  }.body()

  override suspend fun getScoreTiers(): List<ScoreTier> = client.get("mythic-plus/score-tiers")
    .body<List<ScoreTierWebEntity>>()
    .map {
      ScoreTier(it.score, it.rgbHex)
    }

  override suspend fun getRaidStaticData(): RaidStaticDataWebEntity = client.get("raiding/static-data") {
    parameter("expansion_id", Constants.EXPANSION)
  }.body()
}