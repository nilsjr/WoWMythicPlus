package de.nilsdruyen.mythicplus.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import de.nilsdruyen.mythicplus.character.exceptions.InvalidArgumentsException
import de.nilsdruyen.mythicplus.character.utils.convertToCharacterList
import de.nilsdruyen.mythicplus.components.Version
import de.nilsdruyen.mythicplus.states.ArgumentState
import de.nilsdruyen.mythicplus.styles.AppStylesheet
import de.nilsdruyen.mythicplus.utils.PageConst
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.url.URLSearchParams

@Composable
fun MythicPlusWebPage() {
  var state by remember { mutableStateOf<ArgumentState>(ArgumentState.NoArguments) }

  LaunchedEffect(Unit) {
    val urlParams = URLSearchParams(window.location.search)

    if (urlParams.has(PageConst.CHARACTERS)) {
      try {
        val characters = (urlParams.get(PageConst.CHARACTERS) ?: "").convertToCharacterList()
        if (characters.isNotEmpty()) {
          state = ArgumentState.PageArguments(characters)
        }
      } catch (e: InvalidArgumentsException) {
        state = ArgumentState.InvalidArguments
      }
    } else if (urlParams.has(PageConst.REALM) && urlParams.has(PageConst.NAMES)) {
      try {
        val realm = urlParams.get(PageConst.REALM) ?: throw InvalidArgumentsException
        val characters =
          (urlParams.get(PageConst.NAMES) ?: throw InvalidArgumentsException).convertToCharacterList(realm)
        if (characters.isNotEmpty()) {
          state = ArgumentState.PageArguments(characters)
        }
      } catch (e: InvalidArgumentsException) {
        state = ArgumentState.InvalidArguments
      }
    }
  }

  Header()
  Content {
    when (state) {
      ArgumentState.NoArguments -> NoArgumentsPage()
      ArgumentState.InvalidArguments -> InvalidArgumentsPage()
      is ArgumentState.PageArguments -> CharacterPage(state as ArgumentState.PageArguments)
    }
  }
  Footer()
}

@Composable
fun Header() {
  org.jetbrains.compose.web.dom.Header({
    classes(AppStylesheet.pageHeader)
  }) { Text("Mythic+ Overview") }
}

@Composable
fun Content(content: @Composable () -> Unit) {
  Div({
    classes(AppStylesheet.pageContent)
  }) { content() }
}

@Composable
fun Footer() {
  org.jetbrains.compose.web.dom.Footer({
    classes(AppStylesheet.pageFooter)
  }) {
    Version()
  }
}