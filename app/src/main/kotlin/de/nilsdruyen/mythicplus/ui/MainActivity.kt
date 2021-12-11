package de.nilsdruyen.mythicplus.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dagger.hilt.android.AndroidEntryPoint
import de.nilsdruyen.mythicplus.theme.MythicPlusTheme
import de.nilsdruyen.mythicplus.ui.overview.CharacterOverview

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

//  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      App()
    }
  }
}

@Composable
fun App() {
  MythicPlusTheme {
    Scaffold(
      topBar = {
        AppBar()
      }
    ) {
      CharacterOverview()
    }
  }
}

@Composable
fun AppBar() {
  TopAppBar(
    title = {
      Text(text = "Overview", color = Color.White)
    },
    navigationIcon = {
      IconButton(onClick = { /* doSomething() */ }) {
        Icon(
          imageVector = Icons.Filled.Menu,
          contentDescription = "Localized description"
        )
      }
    },
  )
}