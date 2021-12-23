@file:Suppress("unused")

package de.nilsdruyen.mythicplus.initializer

import android.content.Context
import androidx.startup.Initializer
import de.nilsdruyen.mythicplus.BuildConfig
import timber.log.Timber

class LoggingInitializer : Initializer<Unit> {

  override fun create(context: Context) {
    if (BuildConfig.DEBUG) {
      Timber.plant(DebugTree())
      Timber.d("TimberInitializer is initialized.")
    }
  }

  override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}

class DebugTree : Timber.DebugTree() {

  override fun createStackElementTag(element: StackTraceElement): String =
    String.format(
      null,
      "%s %s:%s",
      super.createStackElementTag(element),
      element.methodName,
      element.lineNumber
    )
}