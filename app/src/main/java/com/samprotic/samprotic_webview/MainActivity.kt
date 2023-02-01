package com.samprotic.samprotic_webview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.samprotic.samprotic_webview.ui.theme.Samprotic_webview_newTheme
import com.google.accompanist.web.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Samprotic_webview_newTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MyApp()

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun MyApp(
    modifier: Modifier = Modifier,
    state: WebViewState = rememberWebViewState(url = "https://www.samprotic.com/"),
    navigator: WebViewNavigator = rememberWebViewNavigator()
) {

    navigator.canGoBack
    Scaffold(bottomBar = {
        SamproticBannerAddView()
    },
        topBar = {
            TopAppBar(actions = {
                if (navigator.canGoForward) IconButton(onClick = { navigator.navigateForward() }) {
                    Icon(Icons.Filled.ArrowForward, "Forward")

                }
            }, title = { Text(text = state.pageTitle ?: "") }, navigationIcon = {
                if (navigator.canGoBack) IconButton(onClick = { navigator.navigateBack() }) {
                    Icon(Icons.Filled.ArrowBack, "Back Icon")

                }
            }, backgroundColor = MaterialTheme.colors.background, elevation = 10.dp
            )


        }

    ) { paddingValues ->
        SamproticWebview(
            modifier = modifier.padding(paddingValues),
            state = state, navigator = navigator,
        )

    }
}
@Composable
fun SamproticWebview(
    modifier: Modifier = Modifier,
    state: WebViewState,
    navigator: WebViewNavigator,
) {
    WebView(state = state,
        onCreated = {it.settings.javaScriptEnabled = true},
        navigator = navigator)
}

object obj {
    const val bannerSamprotic = "ca-app-pub-5523890310167714/9764884779"
    const val testBanner = "ca-app-pub-3940256099942544/6300978111"
}



@Composable
fun SamproticBannerAddView(){

    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = {context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = obj.bannerSamprotic
                loadAd(AdRequest.Builder().build())
            }
        })
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Samprotic_webview_newTheme {
        Greeting("Android")
    }
}