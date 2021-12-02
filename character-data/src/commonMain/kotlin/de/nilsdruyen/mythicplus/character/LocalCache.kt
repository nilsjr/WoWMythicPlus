package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.models.Character
import kotlinx.coroutines.flow.Flow

interface LocalCache {

  fun observeCharacters(): Flow<List<Character>>

  suspend fun setCharacterList(list: List<Character>)
}

expect class LocalCacheImpl : LocalCache