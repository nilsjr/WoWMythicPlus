package de.nilsdruyen.mythicplus.character.apis

interface AuthTokenProvider {

  suspend fun getAuthToken(): String

  suspend fun setAuthToken(token: String)

  suspend fun getAccessToken(): String

  suspend fun convertAuthTokenToAccessToken(authToken: String): String
}