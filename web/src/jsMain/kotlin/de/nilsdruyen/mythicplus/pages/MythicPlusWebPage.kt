package de.nilsdruyen.mythicplus.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import de.nilsdruyen.mythicplus.character.utils.convertToList
import de.nilsdruyen.mythicplus.states.ArgumentState
import kotlinx.browser.window
import org.w3c.dom.url.URLSearchParams

@Composable
fun MythicPlusWebPage() {
  var state by remember { mutableStateOf<ArgumentState>(ArgumentState.NoArguments) }

  LaunchedEffect(Unit) {
    val urlParams = URLSearchParams(window.location.search)
    val realm = urlParams.get("realm") ?: ""
    val charNames = urlParams.get("chars") ?: ""

    if (realm.isNotEmpty() && charNames.isNotEmpty()) {
      state = ArgumentState.PageArguments(realm, charNames.convertToList())
    }
  }

  when (state) {
    ArgumentState.NoArguments -> NoArgumentsPage()
    is ArgumentState.PageArguments -> CharacterPage(state as ArgumentState.PageArguments)
  }
}