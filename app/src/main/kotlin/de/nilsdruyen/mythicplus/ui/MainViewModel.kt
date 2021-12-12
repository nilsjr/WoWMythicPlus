package de.nilsdruyen.mythicplus.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    viewModelScope.launch {
      val tag = appUsecase.getBattleTag()
      if (tag.isEmpty()) {
        _screenState.value = Screen.Login
      } else {
        appUsecase.loadCharacters()
        _screenState.value = Screen.Overview
      }
    }
  }

  fun setToken(token: String) {
    viewModelScope.launch {
      authTokenProvider.setToken(token)
      _screenState.value = Screen.Overview
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