package de.nilsdruyen.mythicplus.di

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.mythicplus.character.AppUsecase
import de.nilsdruyen.mythicplus.character.AppUsecaseImpl
import de.nilsdruyen.mythicplus.character.LocalCache
import de.nilsdruyen.mythicplus.character.LocalCacheImpl
import de.nilsdruyen.mythicplus.database.LocalDatabase
import de.nilsdruyen.mythicplus.character.RaiderIoRepository
import de.nilsdruyen.mythicplus.character.RaiderIoRepositoryImpl
import de.nilsdruyen.mythicplus.character.daos.CharacterDao
import javax.inject.Singleton

@Module(includes = [StateDataModule::class])
@InstallIn(SingletonComponent::class)
abstract class DataModule {

  @Binds
  @Singleton
  abstract fun bindsAppUsecase(appUsecase: AppUsecaseImpl): AppUsecase

  @Binds
  @Singleton
  abstract fun bindsLocalCache(cache: LocalCacheImpl): LocalCache

  @Binds
  @Singleton
  abstract fun bindsRaiderIoRepository(cache: RaiderIoRepositoryImpl): RaiderIoRepository
}

@Module
@InstallIn(SingletonComponent::class)
object StateDataModule {

  @Provides
  @Singleton
  fun provideAppDatabase(application: Application): LocalDatabase = LocalDatabase.create(application)

  @Provides
  @Singleton
  fun providePosterDao(database: LocalDatabase): CharacterDao = database.characterDao()
}