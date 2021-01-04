package com.app.festivalpost.activity

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.app.festivalpost.R

class WebBrowserActivity : AppCompatActivity() {
    var webView: WebView? = null

    var url: String? = null
    var title: String? = null
    var pd: ProgressDialog? = null
    var screen_name: String? = "Recharge Screen"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_browser)
        val b = intent.extras
        if (b != null) {
            url = b.getString("url") as String?
            title = b.getString("title")
        }
        pd = ProgressDialog(this@WebBrowserActivity)
        pd!!.setMessage("Please wait.")
        pd!!.setCancelable(false)
        setActionbar()
        init()
    }

    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        tvtitle.text = title
    }

    private fun init() {
        webView = findViewById<View>(R.id.webView) as WebView
        webView!!.settings.domStorageEnabled = true
        webView!!.settings.javaScriptEnabled = true
        webView!!.settings.builtInZoomControls = true
        //        webView.getSettings().set
        webView!!.setInitialScale(0)
        if (url!!.startsWith("http://") || url!!.startsWith("https://")
        ) {
            webView!!.loadUrl(url!!)
        } else if (url!!.startsWith("www")) {
            webView!!.loadUrl("http://$url")
        } else if (url!!.startsWith("file:///")) {
            webView!!.loadUrl(url!!)
        } else {
            webView!!.settings.minimumFontSize = 22
            webView!!.loadDataWithBaseURL(null, url!!, "text/html", null, null)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // TODO Auto-generated method stub
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        screen_name = title
        webView = findViewById<View>(R.id.webView) as WebView
        init()
    }

    fun refreshClick(view: View?) {
        webView!!.reload()
    }

    fun doneClick(view: View?) {
        onBackPressed()
    }

    override fun onBackPressed() {
        finish()
    }

    inner class MyWebClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            Log.d("Page loaded", "loaded - $url")
            return super.shouldOverrideUrlLoading(view, url)
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
            super.onPageStarted(view, url, favicon)
            Log.d("Page started", "started")

//			Global.showProgress(WebBrowserActivity.this, getResources().getString(
//					R.string.loading_message));
            if (pd != null) {
                pd!!.dismiss()
            }
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            Log.d("Page finished", "finished")
            if (pd != null && pd!!.isShowing) {
                pd!!.dismiss()
            }
            //			Global.dismissProgress(WebBrowserActivity.this);
        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            super.onReceivedSslError(view, handler, error)
            Log.d("SslErrorHandler Error", error.toString())
            handler.proceed()
        }

        override fun onLoadResource(view: WebView, url: String) {
            // TODO Auto-generated method stub
            super.onLoadResource(view, url)
            Log.d("received ", "load resource")
        }

        override fun onReceivedError(
            view: WebView, errorCode: Int,
            description: String, failingUrl: String
        ) {
            // TODO Auto-generated method stub
            super.onReceivedError(view, errorCode, description, failingUrl)
            Log.d("description", description)
            if (pd != null && pd!!.isShowing) {
                pd!!.dismiss()
            }


//			Global.dismissProgress(WebBrowserActivity.this);
        }
    }
}