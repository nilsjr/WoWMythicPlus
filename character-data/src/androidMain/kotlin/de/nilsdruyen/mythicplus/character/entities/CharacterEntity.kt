package de.nilsdruyen.mythicplus.character.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import de.nilsdruyen.mythicplus.character.enums.ItemSlot
import de.nilsdruyen.mythicplus.character.enums.WoWClass
import kotlinx.serialization.Serializable

@Entity(tableName = "character_table")
data class CharacterEntity(
  @PrimaryKey val uuid: String,
  val name: String,
  val realm: String,
  val wowClass: WoWClass,
  val specName: String,
  val profileUrl: String,
  val score: Double,
  val scoreColorHex: String,
  val dungeons: List<DungeonScoreEntity>,
  val gear: GearEntity,
  val completedKeysThisWeek: Int,
)

@Serializable
data class DungeonScoreEntity(
  val shortName: String,
  val tyrannicalScore: ScoreEntity,
  val fortifiedScore: ScoreEntity,
)

@Serializable
data class ScoreEntity(
  val id: Int,
  val score: Double,
  val level: Int,
  val upgrade: Int,
  val cleanTimeMs: Long,
)

@Serializable
data class GearEntity(
  val itemLevel: Int,
  val items: List<ItemEntity>,
)

@Serializable
data class ItemEntity(
  val id: Int,
  val slot: ItemSlot,
  val name: String,
  val level: Int,
  val icon: String,
  val isLegendary: Boolean,
  val dominationShards: List<DominationShardEntity>,
  val gems: List<Int>,
  val bonuses: List<Int>,
)

@Serializable
data class DominationShardEntity(
  val quality: Int,
  val name: String,
  val icon: String,
  val id: Int,
)