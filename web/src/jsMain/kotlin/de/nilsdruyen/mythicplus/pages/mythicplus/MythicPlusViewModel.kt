package de.nilsdruyen.mythicplus.pages.mythicplus

import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.CharacterOverview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class MythicPlusViewModel constructor(
  initalState: CharacterOverview,
  private val coroutineScope: CoroutineScope,
) {

  private val _state = MutableStateFlow(MythicPlusState(initalState))
  val state: StateFlow<MythicPlusState> = _state

  val intent = Channel<MythicPlusIntent>(Channel.CONFLATED)

  init {
    observe()
  }

  private fun observe() {
    coroutineScope.launch {
      intent.consumeAsFlow().collect {
        when (it) {
          is MythicPlusIntent.Reorder -> sortCharacters(it.order)
        }
      }
    }
  }

  private fun sortCharacters(sortBy: ListOrder) {
    _state.value = state.value.copy(
        characterList = when (sortBy) {
      ListOrder.Name -> state.value.characterList.sortedBy(Character::name)
      ListOrder.Runs -> state.value.characterList.sortedByDescending(Character::completedKeysThisWeek)
      ListOrder.Class -> state.value.characterList.sortedBy { it.specialization.wowClass.name }
      ListOrder.Score -> state.value.characterList.sortedByDescending(Character::score)
    }
    )
  }
}

enum class ListOrder(val textValue: String) {
  Name("name"),
  Runs("runs this week"),
  Class("class/spec"),
  Score("score")
}