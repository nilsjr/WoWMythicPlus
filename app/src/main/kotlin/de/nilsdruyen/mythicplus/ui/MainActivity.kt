package de.nilsdruyen.mythicplus.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dagger.hilt.android.AndroidEntryPoint
import de.nilsdruyen.mythicplus.theme.MythicPlusTheme
import de.nilsdruyen.mythicplus.ui.overview.CharacterOverview

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private val viewModel: MainViewModel by viewModels()

  @OptIn(ExperimentalMaterial3Api::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      App()
    }
  }
}

@ExperimentalMaterial3Api
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
  CenterAlignedTopAppBar(
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

//@ExperimentalMaterial3Api
//@Preview
//@Composable
//fun PreviewApp() {
//  App()
//}