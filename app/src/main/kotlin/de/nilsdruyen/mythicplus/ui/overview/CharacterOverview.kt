package de.nilsdruyen.mythicplus.ui.overview

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.nilsdruyen.mythicplus.ui.MainViewModel

@Composable
fun CharacterOverview() {
  val viewModel = viewModel<MainViewModel>()
  val listState by viewModel.characterList.collectAsState(emptyList())

  Text(
    text = "Chars: ${listState.size}",
    style = MaterialTheme.typography.bodyMedium,
    modifier = Modifier.padding(8.dp)
  )
}