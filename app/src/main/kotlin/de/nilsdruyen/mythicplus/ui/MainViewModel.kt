package de.nilsdruyen.mythicplus.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import de.nilsdruyen.mythicplus.character.AppUsecase
import de.nilsdruyen.mythicplus.character.apis.AuthTokenProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val appUsecase: AppUsecase,
  private val authTokenProvider: AuthTokenProvider,
) : ViewModel() {

  val overviewState: Flow<Overview> = appUsecase.observeCharacters().map {
    Overview(appUsecase.getBattleTag(), it.size)
  }

  private val _screenState = mutableStateOf(Screen.Loading)
  val screenState: State<Screen> get() = _screenState

  init {
    tryLogin()
  }

  fun setToken(authToken: String) {
    viewModelScope.launch {
      _screenState.value = Screen.Loading
      authTokenProvider.setAuthToken(authToken)
      val accessToken = authTokenProvider.convertAuthTokenToAccessToken(authToken)
      Logger.d("token: $accessToken")
//      tryLogin()
    }
  }

  private fun tryLogin() {
    viewModelScope.launch {
      val token = authTokenProvider.getAccessToken()
      val authToken = authTokenProvider.getAuthToken()
      Logger.d("token: $token - $authToken")

      if (token.isNotEmpty()) {
        val tag = appUsecase.getBattleTag()
        if (tag.isEmpty()) {
          _screenState.value = Screen.Login
        } else {
          appUsecase.loadCharacters()
          _screenState.value = Screen.Overview
        }
      } else {
        _screenState.value = Screen.Login
      }
    }
  }
}

enum class Screen {
  Overview, Login, Loading
}

data class Overview(
  val battleTag: String,
  val chars: Int
)