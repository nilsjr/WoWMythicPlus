package de.nilsdruyen.mythicplus.ui.overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.nilsdruyen.mythicplus.character.models.WoWProfile
import de.nilsdruyen.mythicplus.ui.MainViewModel
import de.nilsdruyen.mythicplus.ui.auth.Auth

@Composable
fun CharacterOverview() {
  val viewModel = viewModel<MainViewModel>()
  val listState by viewModel.characterList.collectAsState(emptyList())
  var screenState by remember { mutableStateOf(Screen.Overview) }
  var authCode by remember { mutableStateOf("") }

  val userState by produceState<State>(State.NoAccess, authCode) {
    value = if (authCode.isNotEmpty()) {
      State.LoggedIn(viewModel.getProfile())
    } else {
      State.NoAccess
    }
  }

  when (screenState) {
    Screen.Overview -> {
      Column {
        Text(
          text = "Chars: ${listState.size}",
          style = MaterialTheme.typography.body2,
          modifier = Modifier.padding(8.dp)
        )
        Button(
          modifier = Modifier.padding(8.dp),
          onClick = {
            screenState = Screen.Login
          }
        ) {
          Text("login")
        }
        if (userState is State.LoggedIn) {
          val id = (userState as State.LoggedIn).profile.id
          Text(
            text = id.toString(),
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(8.dp))
        }
      }
    }
    Screen.Login -> {
      Auth {
        viewModel.setToken(it)
        authCode = it
        screenState = Screen.Overview
      }
    }
    Screen.Loading -> {
      CircularProgressIndicator()
    }
  }
}

enum class Screen {
  Overview, Login, Loading
}

sealed class State {

  object NoAccess : State()

  class LoggedIn(val profile: WoWProfile) : State()
}