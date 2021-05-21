package com.smartneck.twofive

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.webkit.*
import android.widget.Toast
import com.smartneck.twofive.Member.MemberSelectActivity.admin
import kotlinx.android.synthetic.main.activity_web.*

class WebViewActivity : AppCompatActivity() {
    var url = "http://110.10.147.212/medical/admin/index.php"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        // Get the web view settings instance
        val settings = webView.settings

        // Enable java script in web view
        settings.javaScriptEnabled = true

        // Enable and setup web view cache
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.setAppCachePath(cacheDir.path)


        // Enable zooming in web view
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = true


        // Zoom web view text
        settings.textZoom = 125


        // Enable disable images in web view
        settings.blockNetworkImage = false
        // Whether the WebView should load image resources
        settings.loadsImagesAutomatically = true


        // More web view settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.safeBrowsingEnabled = true  // api 26
        }
        //settings.pluginState = WebSettings.PluginState.ON
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.mediaPlaybackRequiresUserGesture = false


        // More optional settings, you can enable it by yourself
        settings.domStorageEnabled = true
        settings.setSupportMultipleWindows(true)
        settings.loadWithOverviewMode = true
        settings.allowContentAccess = true
        settings.setGeolocationEnabled(true)
        settings.allowUniversalAccessFromFileURLs = true
        settings.allowFileAccess = true

        // WebView settings
        webView.fitsSystemWindows = true


        /*
            if SDK version is greater of 19 then activate hardware acceleration
            otherwise activate software acceleration
        */
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)


        // Set web view client
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                // Page loading started
                // Do something

                // Enable disable back forward button

            }

            override fun onPageFinished(view: WebView, url: String) {
                // Page loading finished
                // Display the loaded page title in a toast message

                // Enable disable back forward button

            }
        }


        // Set web view chrome client
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
            }
        }

        url += "?account=${admin.account}&type=medical"
        webView.loadUrl(url)

    }



    // Handle back button press in web view
    override fun onBackPressed() {
        super.onBackPressed()
    }
}



