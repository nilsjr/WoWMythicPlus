package de.nilsdruyen.mythicplus.character.apis

import de.nilsdruyen.mythicplus.character.entities.battlenet.UserInfoWebEntity
import de.nilsdruyen.mythicplus.character.entities.battlenet.WoWProfileWebEntity
import de.nilsdruyen.mythicplus.character.models.WoWProfile
import de.nilsdruyen.mythicplus.character.utils.NamedConst
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import javax.inject.Inject
import javax.inject.Named

class BattleNetApiImpl @Inject constructor(@Named(NamedConst.API_BATTLE_NET) val client: HttpClient) : BattleNetApi {

  override suspend fun getProfile(): WoWProfile {
    val entity = client.get<WoWProfileWebEntity>("https://eu.api.blizzard.com/profile/user/wow") {
      header("Battlenet-Namespace", "profile-eu")
    }
    return WoWProfile(entity.id, entity.accounts.sumOf { it.characters.size })
  }

  override suspend fun getUserInfo(): String =
    client.get<UserInfoWebEntity>("https://eu.battle.net/oauth/userinfo").battletag
}