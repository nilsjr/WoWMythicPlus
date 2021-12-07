package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.models.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

actual class LocalCacheImpl : LocalCache {

  private val _currentState = MutableStateFlow(emptyList<Character>())
  private val state: StateFlow<List<Character>> = _currentState

  override fun observeCharacters(): Flow<List<Character>> = state

  override suspend fun setCharacterList(list: List<Character>) {
    _currentState.value = list
  }
}