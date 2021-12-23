package de.nilsdruyen.mythicplus.character.apis

interface BattleNetAuthApi {

  suspend fun convertAuthTokenToAccessToken(authToken: String): String
}