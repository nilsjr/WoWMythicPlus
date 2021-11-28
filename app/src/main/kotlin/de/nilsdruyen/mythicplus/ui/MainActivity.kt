package de.nilsdruyen.mythicplus.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import de.nilsdruyen.mythicplus.theme.MythicPlusTheme

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
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
  }
}

@Composable
fun AppBar() {
  CenterAlignedTopAppBar(
    title = {
      Text(text = "Overview")
    }
  )
}