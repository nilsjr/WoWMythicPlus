package de.nilsdruyen.mythicplus

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import de.nilsdruyen.mythicplus.character.CharacterRepository
import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.components.CharacterRow
import de.nilsdruyen.mythicplus.components.TableHeader
import kotlinx.browser.window
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Table
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.url.URLSearchParams

@Composable
fun Dashboard(characterRepository: CharacterRepository) {
  val localCharacters = remember { mutableStateListOf<String>() }
  var localRealm by remember { mutableStateOf("") }
  var characters by remember { mutableStateOf(emptyList<Character>()) }
  var currentAffixes by remember { mutableStateOf(emptyList<Int>()) }

  LaunchedEffect(Unit) {
    val urlParams = URLSearchParams(window.location.search)
    val realm = urlParams.get("realm") ?: "Thrall"
    val charNames = urlParams.get("chars") ?: ""

    val list = if (charNames.isNotEmpty()) {
      if (charNames.contains(",")) charNames.split(",") else listOf(charNames)
    } else emptyList()

    localRealm = realm
    localCharacters.addAll(list)
  }

  if (localCharacters.isNotEmpty()) {
    LaunchedEffect(localCharacters) {
      currentAffixes = characterRepository.getCurrentAffixeIds()
      characters = characterRepository.getCharacterList(localRealm, localCharacters)
    }

    Table({
      style {
        property("border-spacing", "8px")
      }
    }) {
      TableHeader()
      if (characters.isNotEmpty()) {
        characters.forEach {
          CharacterRow(it, currentAffixes)
        }
      }
    }
  } else {
    Div({
      style {
        property("margin", "0 auto")
        padding(30.px)
      }
    }) {
      Text("no input")
    }
  }
}