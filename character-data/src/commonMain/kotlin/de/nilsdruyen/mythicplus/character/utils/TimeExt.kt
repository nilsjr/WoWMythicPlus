package de.nilsdruyen.mythicplus.character.utils

fun Long.format(): String {
  val inSeconds = this / 1000
  val seconds = (inSeconds % 60).toString().padStart(2, '0')
  val minutes = ((inSeconds % 3600) / 60).toString().padStart(2, '0')
  val hours = (inSeconds / 3600).toString().padStart(2, '0')
  return "$hours:$minutes:$seconds"
}