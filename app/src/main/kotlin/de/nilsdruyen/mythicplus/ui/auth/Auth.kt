package de.nilsdruyen.mythicplus.ui.auth

import android.annotation.SuppressLint
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import co.touchlab.kermit.Logger
import de.nilsdruyen.mythicplus.character.apis.BattleNetConst.clientId
import de.nilsdruyen.mythicplus.character.apis.BattleNetConst.redirectUri
import de.nilsdruyen.mythicplus.character.apis.BattleNetConst.scopes
import java.util.UUID

private val uniqueState = UUID.randomUUID().toString()

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun Auth(onCallback: (String) -> Unit = {}) {
  val uri = Uri.parse("https://eu.battle.net/oauth/authorize")
    .buildUpon()
    .appendQueryParameter("client_id", clientId)
    .appendQueryParameter("scope", scopes.joinToString(separator = " "))
    .appendQueryParameter("state", uniqueState)
    .appendQueryParameter("redirect_uri", redirectUri)
    .appendQueryParameter("response_type", "code")
    .build()

  AndroidView(factory = ::WebView, modifier = Modifier.fillMaxSize()) { webView ->
    with(webView) {
      settings.javaScriptEnabled = true
      webViewClient = object : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
          request?.let {
            // Check if this url is our OAuth redirectUrl, otherwise ignore it
            if (request.url.toString().startsWith(redirectUri)) {
              // To prevent CSRF attacks, check that we got the same state value we sent, otherwise ignore it
              val responseState = request.url.getQueryParameter("state")
              if (responseState==uniqueState) {
                request.url.getQueryParameter("code")?.let { code ->
                  Logger.d("code: $code")
                  onCallback(code)
                } ?: run {
                  Logger.w("error")
                }
              }
            }
          }
          return super.shouldOverrideUrlLoading(view, request)
        }
      }

      loadUrl(uri.toString())
    }
  }
}