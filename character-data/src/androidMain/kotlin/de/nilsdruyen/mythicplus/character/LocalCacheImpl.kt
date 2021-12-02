package de.nilsdruyen.mythicplus.character

import androidx.annotation.WorkerThread
import de.nilsdruyen.mythicplus.character.daos.CharacterDao
import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.transformers.CharacterTransformerImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

actual class LocalCacheImpl @Inject constructor(
  private val characterDao: CharacterDao
) : LocalCache {

  private val transformer = CharacterTransformerImpl()

  @WorkerThread
  override fun observeCharacters(): Flow<List<Character>> =
    characterDao.getCharacter().map { it.map(transformer::toModel) }

  override suspend fun setCharacterList(list: List<Character>) {
    characterDao.addCharacterList(list.map(transformer::toEntity))
  }
}