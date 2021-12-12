package de.nilsdruyen.mythicplus.character.apis

object BattleNetConst {

  const val redirectUri = "https://oauth.pstmn.io/v1/callback"
  const val CACHE_SIZE = 25L * 1024L * 1024L
  val scopes = listOf("wow.profile")
}
