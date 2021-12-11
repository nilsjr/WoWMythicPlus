package de.nilsdruyen.mythicplus.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.mythicplus.character.apis.AuthTokenProvider
import de.nilsdruyen.mythicplus.character.apis.BattleNetApi
import de.nilsdruyen.mythicplus.character.apis.BattleNetApiImpl
import de.nilsdruyen.mythicplus.character.di.StaticApiModule
import de.nilsdruyen.mythicplus.providers.AuthTokenProviderImpl
import javax.inject.Singleton

@Module(includes = [StaticApiModule::class])
@InstallIn(SingletonComponent::class)
abstract class ApiModule {

  @Binds
  @Singleton
  abstract fun bindsBattleNetApi(api: BattleNetApiImpl): BattleNetApi

  @Binds
  @Singleton
  abstract fun bindsAuthTokenProvider(provider: AuthTokenProviderImpl): AuthTokenProvider
}