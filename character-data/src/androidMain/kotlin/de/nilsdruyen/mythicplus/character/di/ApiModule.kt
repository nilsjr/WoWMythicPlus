package de.nilsdruyen.mythicplus.character.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.mythicplus.character.apis.applyDefaultConfig
import io.ktor.client.HttpClient
import io.ktor.client.features.BrowserUserAgent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StaticApiModule {

  @Provides
  @Singleton
  fun provideHttpClient(): HttpClient = HttpClient {
    applyDefaultConfig("raider.io/api/v1")
    BrowserUserAgent()
  }
}