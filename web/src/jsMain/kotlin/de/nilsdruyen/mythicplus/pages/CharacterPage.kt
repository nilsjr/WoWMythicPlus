package de.nilsdruyen.mythicplus.pages

import LocalDataRepository
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import de.nilsdruyen.mythicplus.character.models.CharacterViewModel
import de.nilsdruyen.mythicplus.components.CharacterTable
import de.nilsdruyen.mythicplus.states.ArgumentState

@Composable
fun CharacterPage(arguments: ArgumentState.PageArguments) {
  val dataRepository = LocalDataRepository.current
  var state by remember { mutableStateOf<CharacterViewModel>(CharacterViewModel.Loading) }

  LaunchedEffect(Unit) {
    val dungeons = dataRepository.getDungeons()
    val characterList = dataRepository.getCharacterList(arguments.characterList)
    val currentAffixes = dataRepository.getCurrentAffixeIds()
    state = CharacterViewModel.MythicPlusOverview(characterList, currentAffixes, dungeons)
  }

  when (state) {
    CharacterViewModel.Loading -> LoadingPage()
    is CharacterViewModel.MythicPlusOverview -> {
      CharacterTable(state as CharacterViewModel.MythicPlusOverview)
    }
  }
}