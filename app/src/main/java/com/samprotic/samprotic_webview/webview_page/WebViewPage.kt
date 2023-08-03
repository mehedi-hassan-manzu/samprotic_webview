package com.samprotic.samprotic_webview.webview_page

import android.content.Context
import android.graphics.Bitmap
import android.webkit.CookieManager
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import com.samprotic.samprotic_webview.admob.BannerAd
import com.samprotic.samprotic_webview.utilities.AppData
import com.samprotic.samprotic_webview.webview_page.widget.WebViewErrorMessage


@Composable
fun WebViewPage(
) {
    val context: Context = LocalContext.current

    var state = rememberWebViewState(url = AppData.ziaArchive)
    val navigator = rememberWebViewNavigator()
    val loadingState = state.loadingState
    var title by remember {
        mutableStateOf("Loading....")
    }
    val isError = remember {
        mutableStateOf(false)
    }
    var singleError: WebResourceError? = null
    var webView1: WebView? by remember { mutableStateOf(null) }




    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = title, style = MaterialTheme.typography.subtitle1
            )
        },

            actions = {

                if (navigator.canGoBack) {
                    IconButton(onClick = { navigator.navigateBack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Menu")

                    }
                }
                if (navigator.canGoForward) {
                    IconButton(onClick = { navigator.navigateBack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Menu"
                        )

                    }
                }

                IconButton(onClick = { webView1?.reload() }) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Menu")

                }
            })

    }, bottomBar = {
        BottomAppBar {
            BannerAd(adUnitId = AppData.samprotic_webview_bottom_bar_banner_ad_unit_id)
        }
    }


    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxHeight()
                .padding(paddingValues = paddingValues)
        ) {
            Column() {
                if (loadingState is LoadingState.Loading) {
                    LinearProgressIndicator(
                        progress = loadingState.progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .width(20.dp)
                    )
                }
                // A custom WebViewClient and WebChromeClient can be provided via subclassing
                val webClient = remember {
                    object : AccompanistWebViewClient() {
                        override fun onPageStarted(
                            view: WebView, url: String?, favicon: Bitmap?
                        ) {
                            super.onPageStarted(view, url, favicon)
                            isError.value = false
                            title = "Loading....."

                        }

                        override fun onPageFinished(view: WebView, url: String?) {
                            super.onPageFinished(view, url)
                            view.evaluateJavascript("(function() { return document.title; })();") { value ->
                                title = value.removeSurrounding("\"")

                            }
                        }

                        override fun onReceivedError(
                            view: WebView, request: WebResourceRequest?, error: WebResourceError?
                        ) {
                            super.onReceivedError(view, request, error)
                            title = "Error loading page"
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
                            webView1?.reload()
                        }

                    } else {

                        Box(modifier = Modifier.weight(1f)) {


                            WebView(
                                modifier = Modifier.fillMaxSize(),
                                state = state,
                                navigator = navigator,
                                onCreated = { webView ->
                                    webView1 = webView
                                    webView.settings.javaScriptEnabled = true
                                    webView.settings.loadsImagesAutomatically = true
                                    webView.settings.domStorageEnabled = true
                                    webView.settings.allowContentAccess = true
                                    webView.settings.allowFileAccess = true
                                    webView.settings.supportZoom()
                                    webView.settings.javaScriptCanOpenWindowsAutomatically = true
                                    webView.settings.useWideViewPort = true
                                    webView.settings.userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3"
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

        }
    }
}

