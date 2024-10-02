package de.nilsdruyen.mythicplus.character.apis

import de.nilsdruyen.mythicplus.character.entities.AffixesWebEntity
import de.nilsdruyen.mythicplus.character.entities.PeriodWebEntity
import de.nilsdruyen.mythicplus.character.entities.ProfileRequest
import de.nilsdruyen.mythicplus.character.entities.ProfileWebEntity
import de.nilsdruyen.mythicplus.character.entities.RaidStaticDataWebEntity
import de.nilsdruyen.mythicplus.character.entities.ScoreTierWebEntity
import de.nilsdruyen.mythicplus.character.entities.StaticDataWebEntity
import de.nilsdruyen.mythicplus.character.models.ScoreTier
import de.nilsdruyen.mythicplus.character.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.plugins.resources.get as resGet

class RaiderIoApiImpl(
  private val client: HttpClient
) : RaiderIoApi {

  override suspend fun getCharacter(realm: String, name: String): ProfileWebEntity {
    return client.resGet(ProfileRequest(realm, name, Constants.Api.Fields.all())).body()
  }

  override suspend fun getCurrentPeriod(): PeriodWebEntity = client.get("periods").body()

  override suspend fun getCurrentAffixIds(): List<Int> = client.get("mythic-plus/affixes") {
    parameter("region", "eu")
    parameter("locale", "en")
  }.body<AffixesWebEntity>().details.map { it.id }

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