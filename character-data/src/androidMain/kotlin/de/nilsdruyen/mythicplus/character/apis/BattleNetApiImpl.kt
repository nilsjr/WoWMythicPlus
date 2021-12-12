package de.nilsdruyen.mythicplus.character.apis

import de.nilsdruyen.mythicplus.character.BuildConfig
import de.nilsdruyen.mythicplus.character.apis.BattleNetConst.redirectUri
import de.nilsdruyen.mythicplus.character.entities.auth.AccessTokenInfo
import de.nilsdruyen.mythicplus.character.entities.battlenet.UserInfoWebEntity
import de.nilsdruyen.mythicplus.character.entities.battlenet.WoWProfileWebEntity
import de.nilsdruyen.mythicplus.character.models.WoWProfile
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.BearerTokens
import io.ktor.client.features.auth.providers.bearer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.Parameters
import kotlinx.serialization.json.Json
import javax.inject.Inject

class BattleNetApiImpl @Inject constructor(
  val authTokenProvider: AuthTokenProvider
) : BattleNetApi {

  private val tokenClient = HttpClient {
    install(Logging) {
      logger = Logger.DEFAULT
      level = LogLevel.INFO
    }
    install(JsonFeature) {
      serializer = KotlinxSerializer(Json {
        isLenient = true
        ignoreUnknownKeys = true
      })
    }
  }

  private val client: HttpClient = HttpClient(OkHttp) {
    engine {
      config {
        followRedirects(true)
//      cache(Cache(File("", ""), CACHE_SIZE))
      }
    }
    install(Logging) {
      logger = Logger.DEFAULT
      level = LogLevel.INFO
    }
    install(JsonFeature) {
      serializer = KotlinxSerializer(Json {
        isLenient = true
        ignoreUnknownKeys = true
      })
    }
    // oauth2 battlenet
    install(Auth) {
      lateinit var accessTokenInfo: AccessTokenInfo

      bearer {
        loadTokens {
          accessTokenInfo = tokenClient.submitForm(
            url = "https://eu.battle.net/oauth/token",
            formParameters = Parameters.build {
              append("grant_type", "authorization_code")
              append("code", authTokenProvider.getAuthToken())
              append("client_id", BuildConfig.CLIENT_ID)
              append("client_secret", BuildConfig.CLIENT_SECRET)
              append("redirect_uri", redirectUri)
            }
          )
          BearerTokens(
            accessToken = accessTokenInfo.accessToken,
            refreshToken = ""
          )
        }
      }
    }
  }

  override suspend fun getProfile(): WoWProfile {
    val entity = client.get<WoWProfileWebEntity>("https://eu.api.blizzard.com/profile/user/wow") {
      header("Battlenet-Namespace", "profile-eu")
//      parameter("locale", "en_US")
    }
    return WoWProfile(entity.id, entity.accounts.sumOf { it.characters.size })
  }

  override suspend fun getUserInfo(): String =
    client.get<UserInfoWebEntity>("https://eu.battle.net/oauth/userinfo").battletag
}