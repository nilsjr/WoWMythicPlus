package de.nilsdruyen.mythicplus.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.nilsdruyen.mythicplus.character.converters.Converters
import de.nilsdruyen.mythicplus.character.daos.CharacterDao
import de.nilsdruyen.mythicplus.character.entities.CharacterEntity

@Database(entities = [CharacterEntity::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {

  abstract fun characterDao(): CharacterDao

  companion object {

    const val NAME = "mythicplus_db"

    fun create(context: Context): LocalDatabase =
      Room.databaseBuilder(context, LocalDatabase::class.java, NAME)
        .fallbackToDestructiveMigration()
        .build()
  }
}