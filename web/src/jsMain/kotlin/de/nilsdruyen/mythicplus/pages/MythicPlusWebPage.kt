package de.nilsdruyen.mythicplus.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import de.nilsdruyen.mythicplus.character.exceptions.InvalidArgumentsException
import de.nilsdruyen.mythicplus.character.utils.convertToCharacterList
import de.nilsdruyen.mythicplus.states.ArgumentState
import de.nilsdruyen.mythicplus.utils.PageConst
import kotlinx.browser.window
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

  when (state) {
    ArgumentState.NoArguments -> NoArgumentsPage()
    ArgumentState.InvalidArguments -> InvalidArgumentsPage()
    is ArgumentState.PageArguments -> CharacterPage(state as ArgumentState.PageArguments)
  }
}