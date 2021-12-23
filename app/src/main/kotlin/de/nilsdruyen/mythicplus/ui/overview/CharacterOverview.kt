package de.nilsdruyen.mythicplus.ui.overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.nilsdruyen.mythicplus.ui.MainViewModel
import de.nilsdruyen.mythicplus.ui.Overview
import de.nilsdruyen.mythicplus.ui.Screen
import de.nilsdruyen.mythicplus.ui.auth.Auth

@Composable
fun CharacterOverview(viewModel: MainViewModel) {
  val screenState by viewModel.screenState

  when (screenState) {
    Screen.Overview -> {
      val overviewState by viewModel.overviewState.collectAsState(Overview("", 0))
      Column {
        Text(
          text = "BattleTag: ${overviewState.battleTag}",
          style = MaterialTheme.typography.body2,
          modifier = Modifier.padding(8.dp)
        )
        Text(
          text = "Chars: ${overviewState.chars}",
          style = MaterialTheme.typography.body2,
          modifier = Modifier.padding(8.dp)
        )
      }
    }
    Screen.Login -> {
      Column {
        Text("Not logged in", modifier = Modifier.padding(8.dp))
        Auth {
          viewModel.setToken(it)
        }
      }
    }
    Screen.Loading -> CircularProgressIndicator()
  }
}