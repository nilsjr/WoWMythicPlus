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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AuthTokenProviderImpl @Inject constructor(@ApplicationContext val context: Context) : AuthTokenProvider {

  private val dataStore = context.dataStore
  private val battleNetAccessTokenKey = stringPreferencesKey("access_token")
  private var token = ""

  override suspend fun getAuthToken(): String =
    dataStore.data.map { preferences -> preferences[battleNetAccessTokenKey] ?: token }.first().also {
      Logger.d("get token: $it")
    }

  override suspend fun setToken(token: String) {
    this.token = token
    Logger.d("set token: $token")
    dataStore.edit { settings ->
      settings[battleNetAccessTokenKey] = token
    }
  }
}