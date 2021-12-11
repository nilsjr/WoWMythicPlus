package de.nilsdruyen.mythicplus.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.nilsdruyen.mythicplus.character.AppUsecase
import de.nilsdruyen.mythicplus.character.apis.AuthTokenProvider
import de.nilsdruyen.mythicplus.character.apis.BattleNetApi
import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.WoWProfile
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val appUsecase: AppUsecase,
  private val authTokenProvider: AuthTokenProvider,
  private val api: BattleNetApi,
) : ViewModel() {

  val characterList: Flow<List<Character>> = appUsecase.observeCharacters()

  fun setToken(token: String) {
    authTokenProvider.setToken(token)
  }

  suspend fun getProfile(): WoWProfile {
    return api.getProfile()
  }
}