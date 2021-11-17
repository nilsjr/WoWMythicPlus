package de.nilsdruyen.mythicplus

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import de.nilsdruyen.mythicplus.character.CharacterRepository
import de.nilsdruyen.mythicplus.character.RaiderIoApi
import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.Dungeon
import de.nilsdruyen.mythicplus.character.utils.Constants
import de.nilsdruyen.mythicplus.screens.CharacterOverview
import de.nilsdruyen.mythicplus.screens.Loading
import de.nilsdruyen.mythicplus.screens.NoParameters
import kotlinx.browser.window
import org.w3c.dom.url.URLSearchParams

@Composable
fun Dashboard(characterRepository: CharacterRepository) {
  val localCharacters = remember { mutableStateListOf<String>() }
  val characters = remember { mutableStateListOf<Character>() }
  val dungeons = remember { mutableStateListOf<Dungeon>() }

  var localRealm by remember { mutableStateOf("") }
  var currentAffixes by remember { mutableStateOf(emptyList<Int>()) }
  var isLoading by remember { mutableStateOf(false) }

  LaunchedEffect(Unit) {
    val urlParams = URLSearchParams(window.location.search)
    val realm = urlParams.get("realm") ?: "Thrall"
    val charNames = urlParams.get("chars") ?: ""

    val list = if (charNames.isNotEmpty()) {
      if (charNames.contains(",")) charNames.split(",") else listOf(charNames)
    } else emptyList()

    localRealm = realm
    localCharacters.clear()
    localCharacters.addAll(list)
  }

  if (localCharacters.isNotEmpty()) {
    LaunchedEffect(localCharacters) {
      isLoading = true
      currentAffixes = characterRepository.getCurrentAffixeIds()

      val sortedDungeons = RaiderIoApi.getStaticData().dungeons.map { Dungeon(it.id, it.shortName) }.sortedBy {
        Constants.Dungeons.indexOf(it.shortName)
      }

      dungeons.clear()
      dungeons.addAll(sortedDungeons)

      println("d: $sortedDungeons")

      characters.clear()
      characters.addAll(characterRepository.getCharacterList(localRealm, localCharacters))
      isLoading = false
    }

    if (isLoading) {
      Loading()
    } else {
      CharacterOverview(characters, currentAffixes, dungeons)
    }
  } else {
    NoParameters()
  }
}