package de.nilsdruyen.mythicplus.character.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.mythicplus.character.apis.AuthTokenProvider
import de.nilsdruyen.mythicplus.character.apis.BattleNetAuthApi
import de.nilsdruyen.mythicplus.character.apis.BattleNetAuthApiImpl
import de.nilsdruyen.mythicplus.character.apis.OAuthFeature
import de.nilsdruyen.mythicplus.character.apis.applyDefaultConfig
import de.nilsdruyen.mythicplus.character.utils.NamedConst
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.http.URLProtocol
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [StaticApiModule::class])
@InstallIn(SingletonComponent::class)
abstract class ApiModule {

  @Binds
  @Singleton
  abstract fun bindBattleNetApi(apiImpl: BattleNetAuthApiImpl): BattleNetAuthApi
}

@Module
@InstallIn(SingletonComponent::class)
object StaticApiModule {

  @Provides
  @Singleton
  @Named("tokenClient")
  fun provideTokenClient(): HttpClient = HttpClient {
    install(JsonFeature) {
      serializer = KotlinxSerializer(Json {
        isLenient = true
        ignoreUnknownKeys = true
      })
    }
  }

  @Provides
  @Singleton
  @Named(NamedConst.API_RAIDER_IO)
  fun provideRaiderIoHttpClient(okHttpClient: OkHttpClient): HttpClient = HttpClient(OkHttp) {
    engine {
      config {
        preconfigured = okHttpClient
      }
    }
    applyDefaultConfig("raider.io/api/v1")
  }

  @Provides
  @Singleton
  @Named(NamedConst.API_BATTLE_NET)
  fun provideHttpClient(
    authTokenProvider: AuthTokenProvider,
    okHttpClient: OkHttpClient
  ): HttpClient = HttpClient(OkHttp) {
    engine {
      config {
        preconfigured = okHttpClient
      }
    }
    defaultRequest {
      url {
        protocol = URLProtocol.HTTPS
      }
    }
    install(JsonFeature) {
      serializer = KotlinxSerializer(Json {
        isLenient = true
        ignoreUnknownKeys = true
      })
    }
    install(OAuthFeature) {
      getToken = {
        authTokenProvider.getAccessToken()
      }
      refreshToken = {
        authTokenProvider.convertAuthTokenToAccessToken(authTokenProvider.getAuthToken())
      }
    }
  }
}