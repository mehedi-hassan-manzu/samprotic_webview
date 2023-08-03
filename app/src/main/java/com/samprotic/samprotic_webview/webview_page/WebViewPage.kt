package com.samprotic.samprotic_webview.webview_page

import android.content.Context
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
import androidx.compose.runtime.MutableState
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
import com.google.accompanist.web.WebViewNavigator
import com.google.accompanist.web.WebViewState
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import com.samprotic.samprotic_webview.admob.BannerAd
import com.samprotic.samprotic_webview.utilities.AppData
import com.samprotic.samprotic_webview.webview_page.widget.WebViewBox
import com.samprotic.samprotic_webview.webview_page.widget.WebViewErrorMessage


@Composable
fun WebViewPage(
) {
    val context: Context = LocalContext.current

    var state = rememberWebViewState(url = AppData.ziaArchive)


    val navigator = rememberWebViewNavigator()

    val loadingState = state.loadingState

    var title = remember {
        mutableStateOf("Loading....")
    }

    val isError = remember {
        mutableStateOf(false)
    }
    var singleError: WebResourceError? = null
    var webView1: MutableState<WebView?> = remember { mutableStateOf(null) }




    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = title.value, style = MaterialTheme.typography.subtitle1
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

                IconButton(onClick = { webView1.value?.reload() }) {
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
                WebViewBox(
                    isError = isError,
                    title = title,
                    webView1 = webView1,
                    state = state,
                    navigator = navigator,
                    loadingState = loadingState
                )
            }

        }
    }
}




