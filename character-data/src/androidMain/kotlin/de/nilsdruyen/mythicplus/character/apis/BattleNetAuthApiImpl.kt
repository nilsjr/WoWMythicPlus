package de.nilsdruyen.mythicplus.character.apis

import de.nilsdruyen.mythicplus.character.BuildConfig
import de.nilsdruyen.mythicplus.character.entities.auth.AccessTokenInfo
import io.ktor.client.HttpClient
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.BasicAuthCredentials
import io.ktor.client.features.auth.providers.basic
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import kotlinx.serialization.json.Json
import javax.inject.Inject

class BattleNetAuthApiImpl @Inject constructor() : BattleNetAuthApi {

  private val client = HttpClient {
    install(JsonFeature) {
      serializer = KotlinxSerializer(Json {
        isLenient = true
        ignoreUnknownKeys = true
      })
    }
    install(Auth) {
      basic {
        credentials {
          BasicAuthCredentials(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET)
        }
        sendWithoutRequest {
          it.url.host=="eu.battle.net"
        }
      }
    }
  }

  override suspend fun convertAuthTokenToAccessToken(authToken: String): String {
    return client.post<AccessTokenInfo>("https://eu.battle.net/oauth/token") {
      parameter("code", authToken)
      parameter("grant_type", "authorization_code")
      parameter("scope", BattleNetConst.scopes.joinToString(" "))
      parameter("redirect_uri", BattleNetConst.redirectUri)
    }.accessToken
  }
}