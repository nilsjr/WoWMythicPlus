package de.nilsdruyen.mythicplus.character.converters

import androidx.room.TypeConverter
import de.nilsdruyen.mythicplus.character.entities.DominationShardEntity
import de.nilsdruyen.mythicplus.character.entities.DungeonScoreEntity
import de.nilsdruyen.mythicplus.character.entities.GearEntity
import de.nilsdruyen.mythicplus.character.entities.ItemEntity
import de.nilsdruyen.mythicplus.character.enums.ItemSlot
import de.nilsdruyen.mythicplus.character.enums.Role
import de.nilsdruyen.mythicplus.character.enums.WoWClass
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object Converters {

  @TypeConverter
  fun toIntList(value: String): List<Int> = Json.decodeFromString(value)

  @TypeConverter
  fun fromIntList(value: List<Int>): String = Json.encodeToString(value)

  @TypeConverter
  fun toGear(value: String): GearEntity = Json.decodeFromString(value)

  @TypeConverter
  fun fromGear(value: GearEntity): String = Json.encodeToString(value)

  @TypeConverter
  fun toDungeonScore(value: String): List<DungeonScoreEntity> = Json.decodeFromString(value)

  @TypeConverter
  fun fromDungeonScore(value: List<DungeonScoreEntity>): String = Json.encodeToString(value)

  @TypeConverter
  fun toRole(value: String) = enumValueOf<Role>(value)

  @TypeConverter
  fun fromRole(value: Role) = value.name

  @TypeConverter
  fun toWoWClass(value: String) = enumValueOf<WoWClass>(value)

  @TypeConverter
  fun fromWoWClass(value: WoWClass) = value.name

  @TypeConverter
  fun toItemSlot(value: String) = enumValueOf<ItemSlot>(value)

  @TypeConverter
  fun fromItemSlot(value: ItemSlot) = value.name

  @TypeConverter
  fun toItemEntity(value: String): List<ItemEntity> = Json.decodeFromString(value)

  @TypeConverter
  fun fromItemEntity(value: List<ItemEntity>): String = Json.encodeToString(value)

  @TypeConverter
  fun toDominationShard(value: String): List<DominationShardEntity> = Json.decodeFromString(value)

  @TypeConverter
  fun fromDominationShard(value: List<DominationShardEntity>): String = Json.encodeToString(value)
}