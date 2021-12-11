package de.nilsdruyen.mythicplus.character.apis

import de.nilsdruyen.mythicplus.character.models.WoWProfile

interface BattleNetApi {

  suspend fun getProfile(): WoWProfile

  suspend fun getUserInfo(): String
}