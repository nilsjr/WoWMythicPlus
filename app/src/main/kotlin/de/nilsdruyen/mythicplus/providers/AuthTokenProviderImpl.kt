package de.nilsdruyen.mythicplus.providers

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import co.touchlab.kermit.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import de.nilsdruyen.mythicplus.character.apis.AuthTokenProvider
import de.nilsdruyen.mythicplus.character.apis.BattleNetAuthApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AuthTokenProviderImpl @Inject constructor(
  @ApplicationContext val context: Context,
  private val authApi: BattleNetAuthApi,
) : AuthTokenProvider {

  private val dataStore = context.dataStore

  private val battleNetAuthTokenKey = stringPreferencesKey("auth_token")
  private val battleNetAccessTokenKey = stringPreferencesKey("access_token")

  override suspend fun getAuthToken(): String =
    dataStore.data.map { preferences -> preferences[battleNetAuthTokenKey] ?: "" }.first()

  override suspend fun setAuthToken(token: String) {
    Logger.d("set auth token: $token")
    dataStore.edit { settings ->
      settings[battleNetAuthTokenKey] = token
    }
  }

  override suspend fun getAccessToken(): String {
    return dataStore.data.map { preferences -> preferences[battleNetAccessTokenKey] ?: "" }.first()
  }

  override suspend fun convertAuthTokenToAccessToken(authToken: String): String {
    val token = authApi.convertAuthTokenToAccessToken(authToken)

    Timber.d("get token $token")

    dataStore.edit { settings ->
      settings[battleNetAccessTokenKey] = token
    }

    return token
  }
}