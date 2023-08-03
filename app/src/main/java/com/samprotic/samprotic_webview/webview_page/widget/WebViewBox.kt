package com.samprotic.samprotic_webview.webview_page.widget

import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.WebViewNavigator
import com.google.accompanist.web.WebViewState


@Composable
fun WebViewBox(
    isError: MutableState<Boolean>,
    title: MutableState<String>,
    webView1: MutableState<WebView?>,
    state: WebViewState,
    navigator: WebViewNavigator,
    loadingState: LoadingState,
) {

    val webClient = remember {
        object : AccompanistWebViewClient() {
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                super.onReceivedSslError(view, handler, error)
                handler?.proceed()

            }

            override fun onPageStarted(
                view: WebView, url: String?, favicon: Bitmap?
            ) {
                super.onPageStarted(view, url, favicon)
                isError.value = false
                title.value = "Loading....."

            }

            override fun onPageFinished(view: WebView, url: String?) {
                super.onPageFinished(view, url)
                view.evaluateJavascript("(function() { return document.title; })();") { value ->
                    title.value = value.removeSurrounding("\"")

                }
            }

            override fun onReceivedError(
                view: WebView, request: WebResourceRequest?, error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                title.value = "Error loading page"
                isError.value = true
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        if (isError.value) {
            WebViewErrorMessage {
                webView1.value?.reload()
            }

        } else {

            Box(modifier = Modifier.weight(1f)) {


                WebView(
                    modifier = Modifier.fillMaxSize(),
                    state = state,
                    navigator = navigator,
                    onCreated = { webView ->
                        webView1.value = webView
                        webView.settings.javaScriptEnabled = true
                        webView.settings.loadsImagesAutomatically = true
                        webView.settings.domStorageEnabled = true
                        webView.settings.allowContentAccess = true
                        webView.settings.allowFileAccess = true
                        webView.settings.supportZoom()
                        webView.settings.javaScriptCanOpenWindowsAutomatically = true
                        webView.settings.useWideViewPort = true
                        webView.settings.userAgentString =
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3"
                        webView.settings.setSupportMultipleWindows(true)
                        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
                        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        CookieManager.getInstance().removeAllCookies(null)


                    },
                    client = webClient,


                    )

                if (loadingState is LoadingState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

    }
}