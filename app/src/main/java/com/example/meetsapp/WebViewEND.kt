package com.example.meetsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.example.meetsapp.databinding.ActivityWebViewEndBinding

class WebViewEND : AppCompatActivity() {
    lateinit var bindingClass: ActivityWebViewEndBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityWebViewEndBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
        webViewSetup()
    }
    private fun webViewSetup(){
        bindingClass.webview.webViewClient = WebViewClient()

        bindingClass.webview.apply {
            loadUrl("https://ankova.ulcraft.com/")
        }
    }
}