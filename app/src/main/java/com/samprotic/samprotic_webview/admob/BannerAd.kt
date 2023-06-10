package com.samprotic.samprotic_webview.admob

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BannerAd(adUnitId: String){
    Box(modifier = Modifier.fillMaxWidth()) {
        AndroidView(modifier = Modifier.fillMaxWidth(),
            factory = {context: Context ->
                AdView(context).apply {
                   this.setAdSize(AdSize.BANNER)
                   this.adUnitId = adUnitId
                    val adRequest = AdRequest.Builder().build()
                    this.loadAd(adRequest)


                }


        })
    }
}