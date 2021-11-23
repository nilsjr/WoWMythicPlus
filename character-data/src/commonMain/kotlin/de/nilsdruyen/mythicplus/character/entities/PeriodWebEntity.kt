package de.nilsdruyen.mythicplus.character.entities

import kotlinx.serialization.Serializable

@Serializable
data class PeriodWebEntity(
  val periods: List<PeriodItemWebEntity>
)

@Serializable
data class PeriodItemWebEntity(
  val region: String,
  val current: PeriodDetailWebEntity
)

@Serializable
data class PeriodDetailWebEntity(
  val period: Int,
  val start: String,
  val end: String,
)
