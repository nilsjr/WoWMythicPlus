package de.nilsdruyen.mythicplus.character

import androidx.annotation.WorkerThread
import de.nilsdruyen.mythicplus.character.models.Character
import kotlinx.coroutines.flow.Flow

interface AppUsecase {

  @WorkerThread
  fun observeCharacters(): Flow<List<Character>>

  suspend fun loadCharacters()

  suspend fun getBattleTag(): String
}