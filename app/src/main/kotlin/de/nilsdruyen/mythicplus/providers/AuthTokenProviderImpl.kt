package de.nilsdruyen.mythicplus.providers

import de.nilsdruyen.mythicplus.character.apis.AuthTokenProvider
import javax.inject.Inject

class AuthTokenProviderImpl @Inject constructor() : AuthTokenProvider {

  private var token = ""

  override fun getAuthToken(): String = token

  override fun setToken(token: String) {
    this.token = token
  }
}