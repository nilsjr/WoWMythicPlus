package de.nilsdruyen.mythicplus.character.apis

interface AuthTokenProvider {

  suspend fun getAuthToken(): String

  suspend fun setToken(token: String)
}