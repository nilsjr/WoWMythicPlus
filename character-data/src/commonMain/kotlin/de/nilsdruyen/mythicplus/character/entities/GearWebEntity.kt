package de.nilsdruyen.mythicplus.character.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GearWebEntity(
  @SerialName("item_level_equipped") val iLvlEquipped: Double,
  val items: Map<String, ItemWebEntity>,
)

@Serializable
data class ItemWebEntity(
  @SerialName("item_id") val id: Int,
  @SerialName("item_level") val itemLevel: Int,
  val icon: String,
  val name: String,
  @SerialName("is_legendary") val isLegendary: Boolean,
  @SerialName("domination_shards") val dominationShards: List<DominationShardWebEntity>,
  val gems: List<Int>,
  val bonuses: List<Int>,
)

@Serializable
data class DominationShardWebEntity(
  val quality: Int,
  val name: String,
  val icon: String,
  @SerialName("item_id") val itemId: Int,
)