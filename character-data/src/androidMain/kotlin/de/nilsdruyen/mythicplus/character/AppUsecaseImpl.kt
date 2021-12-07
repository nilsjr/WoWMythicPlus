package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.InputCharacter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppUsecaseImpl @Inject constructor(
  val repository: RaiderIoRepository,
  val localCache: LocalCache
) : AppUsecase {

  override fun observeCharacters(): Flow<List<Character>> {
    return localCache.observeCharacters()
  }

  override suspend fun loadCharacters() {
    val list = repository.getCharacterList(listOf(InputCharacter("Harazz", "thrall")))
    localCache.setCharacterList(list)
  }
}