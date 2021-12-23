package de.nilsdruyen.mythicplus.di

import android.content.Context
import android.os.Looper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.mythicplus.BuildConfig
import de.nilsdruyen.mythicplus.character.apis.AuthTokenProvider
import de.nilsdruyen.mythicplus.character.apis.BattleNetApi
import de.nilsdruyen.mythicplus.character.apis.BattleNetApiImpl
import de.nilsdruyen.mythicplus.providers.AuthTokenProviderImpl
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
abstract class ApiModule {

  @Binds
  @Singleton
  abstract fun bindsBattleNetApi(api: BattleNetApiImpl): BattleNetApi

  @Binds
  @Singleton
  abstract fun bindsAuthTokenProvider(provider: AuthTokenProviderImpl): AuthTokenProvider
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  private const val CACHE_SIZE = 25L * 1024L * 1024L
  private const val OKHTTP_TIMEOUT_SECONDS = 10L
  private const val CURRENT_CACHE_FOLDER = "http_cache"

  @Provides
  @Singleton
  fun provideHttpCache(@ApplicationContext context: Context): Cache =
    Cache(File(context.cacheDir, CURRENT_CACHE_FOLDER), CACHE_SIZE)

  @Provides
  @Singleton
  fun provideBaseHttpClient(
    cache: Cache,
    loggingInterceptor: HttpLoggingInterceptor,
  ): OkHttpClient {
    if (Looper.myLooper()==Looper.getMainLooper()) Timber.e("Initializing OkHttpClient on main thread.")
    return OkHttpClient.Builder()
//      .addInterceptor(loggingInterceptor)
      .cache(cache)
      .retryOnConnectionFailure(true)
      .connectTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
      .readTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
      .writeTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
      .build()
  }

  @Provides
  @Singleton
  fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor {
      Timber.tag("OkHttp")
      Timber.d(it)
    }.apply { level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE }
  }
}