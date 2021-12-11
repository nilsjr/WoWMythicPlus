package de.nilsdruyen.mythicplus.character.apis

interface AuthTokenProvider {

  fun getAuthToken(): String

  fun setToken(token: String)
}