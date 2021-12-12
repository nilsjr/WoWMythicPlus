package de.nilsdruyen.mythicplus.character

import de.nilsdruyen.mythicplus.character.apis.BattleNetApi
import de.nilsdruyen.mythicplus.character.models.Character
import de.nilsdruyen.mythicplus.character.models.InputCharacter
import io.ktor.client.features.ClientRequestException
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppUsecaseImpl @Inject constructor(
  val repository: RaiderIoRepository,
  val localCache: LocalCache,
  val battleNetApi: BattleNetApi,
) : AppUsecase {

  override fun observeCharacters(): Flow<List<Character>> {
    return localCache.observeCharacters()
  }

  override suspend fun loadCharacters() {
    val list = repository.getCharacterList(listOf(InputCharacter("Harazz", "thrall")))
    localCache.setCharacterList(list)
  }

  override suspend fun getBattleTag(): String {
    return try {
      battleNetApi.getUserInfo()
    } catch (e: ClientRequestException) {
      ""
    }
  }
}