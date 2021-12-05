package de.nilsdruyen.mythicplus.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.nilsdruyen.mythicplus.character.AppUsecase
import de.nilsdruyen.mythicplus.character.models.Character
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val appUsecase: AppUsecase
) : ViewModel() {

  val characterList: Flow<List<Character>> = appUsecase.observeCharacters()


}