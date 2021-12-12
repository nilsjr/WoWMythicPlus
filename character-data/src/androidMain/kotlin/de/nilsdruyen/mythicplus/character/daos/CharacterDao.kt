package de.nilsdruyen.mythicplus.character.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.nilsdruyen.mythicplus.character.entities.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

  @Query("SELECT * FROM character_table WHERE name = :name")
  suspend fun getCharacter(name: String): CharacterEntity

  @Query("SELECT * FROM character_table")
  fun getCharacter(): Flow<List<CharacterEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addCharacterList(list: List<CharacterEntity>)
}