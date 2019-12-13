package com.ufistudio.ianlin.webviewtest

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.google.androidbrowserhelper.trusted.TwaLauncher
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    //東方紅 "https://nf-qat-frontend-mobile-dfh.yjxyz.xyz/"
    //愛碼 "https://nf-qat-frontend-mobile-demo01_gulp.yjxyz.xyz/"
    //億客隆 "http://52.196.125.188:13046/"

    private val mURL = "https://nf-qat-frontend-mobile-demo01_gulp.yjxyz.xyz/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webview.clearCache(true)
        webview.clearHistory()

        webview.requestFocus()

        var webSettings: WebSettings = webview.settings
        webSettings.databaseEnabled = true

        var appCachePath = applicationContext.cacheDir.absolutePath + "/webcache"

        webSettings.setAppCachePath(appCachePath)
        webSettings.databasePath = appCachePath

        webSettings.domStorageEnabled = true
        webSettings.allowFileAccess = true
        webSettings.setAppCacheEnabled(true)

        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webSettings.defaultTextEncodingName = "utf-8"
        webSettings.loadsImagesAutomatically = true
        webSettings.javaScriptEnabled = true
        webSettings.supportMultipleWindows()
        webSettings.setNeedInitialFocus(true)
        webSettings.javaScriptCanOpenWindowsAutomatically = true


        webview.webViewClient = object : WebViewClient() {


            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.e("Ian","[onPageFinished] url:$url")
            }

            override fun onSafeBrowsingHit(
                view: WebView?,
                request: WebResourceRequest?,
                threatType: Int,
                callback: SafeBrowsingResponse?
            ) {
                super.onSafeBrowsingHit(view, request, threatType, callback)
                Log.e("Ian","[onSafeBrowsingHit] request:$request, threatType$threatType, callback$callback")
            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                Log.e("Ian","[onReceivedError] errorCode:$errorCode, description$description, failingUrl$failingUrl")
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                Log.e("Ian","[onReceivedError] error:$error, request$request")
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
                Log.e("Ian","[onReceivedHttpError] errorResponse:$errorResponse, request$request")
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.e("Ian","[onPageStarted] url:$url")
            }

            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
//                val browserIntent = Intent(Intent.ACTION_VIEW, request.url)
//                startActivity(browserIntent)

                val cookieManager = CookieManager.getInstance()
                cookieManager.setCookie(request.url.toString(), cookieManager.getCookie(mURL)) //如果没有特殊需求，这里只需要将session id以"key=value"形式作为cookie即可

                val newCookie = cookieManager.getCookie(request.url.toString())
                Log.e("Ian","old cookie: ${cookieManager.getCookie(mURL)}")
                Log.e("Ian"," newCookie: $newCookie")
                TwaLauncher(view.context).launch(request.url)

//                view.loadUrl(request.url.toString())

                return true
            }
        }
        webview.webChromeClient = object : WebChromeClient() {

            override fun onJsPrompt(
                view: WebView?,
                url: String?,
                message: String?,
                defaultValue: String?,
                result: JsPromptResult?
            ): Boolean {
                Log.e("Ian","[onJsPrompt] url:$url, message:$message, defaultValue:$defaultValue, result:$result")
                return super.onJsPrompt(view, url, message, defaultValue, result)
            }

            override fun onJsConfirm(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult?
            ): Boolean {
                Log.e("Ian","[onJsConfirm] url:$url, message:$message, result:$result")
                return super.onJsConfirm(view, url, message, result)
            }

            override fun onJsTimeout(): Boolean {
                Log.e("Ian","[onJsTimeout]")
                return super.onJsTimeout()
            }

            override fun onJsAlert(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult?
            ): Boolean {
                Log.e("Ian", "[onJsAlert] url:$url, message:$message")
                view?.goBack()
                return super.onJsAlert(view, url, message, result)
            }
        }

        webview.loadUrl(mURL)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        event?.let {
            if (it.keyCode == KeyEvent.KEYCODE_BACK) {
                Log.e("Ian", "[KeyEvent.KEYCODE_BACK]")
                if (it.action == KeyEvent.ACTION_UP) {
                    Log.e("Ian", "[KeyEvent.KEYCODE_BACK] it.action == KeyEvent.ACTION_UP")
                    return true
                }
                Log.e("Ian", "[KeyEvent.KEYCODE_BACK] webview.canGoBack() = ${webview.canGoBack()}")
//                if(webview.canGoBack()){
                webview.goBack()
//                }else{
//                    return false
//                }
            }
        }
        return super.dispatchKeyEvent(event)
    }
}
