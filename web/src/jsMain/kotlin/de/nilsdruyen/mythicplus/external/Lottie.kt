package de.nilsdruyen.mythicplus.external

import org.w3c.dom.HTMLElement

external val lottie: LottiePlayer

external interface LottiePlayer {

  fun loadAnimation(params: dynamic): AnimationItem
}

external interface AnimationItem

external interface AnimationConfig {
  var container: HTMLElement
  var renderer: String
  var loop: Boolean
  var autoplay: Boolean
  var path: String
}