package com.upao.panaderia.views

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.upao.panaderia.databinding.ActivityNiubizBinding
import com.upao.panaderia.helpers.SharedPreferencesManager

class NiubizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNiubizBinding
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        binding = ActivityNiubizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idPedido = intent.getStringExtra("idPedido")
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        binding.webview.loadUrl("https://api-panaderia.strategyec.com/public_html/niubiz/$idPedido")

        binding.webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (url == "https://api-panaderia.strategyec.com/public_html/payment-success") {
                    SharedPreferencesManager.removeCartProducts(this@NiubizActivity)
                    val intent = Intent(this@NiubizActivity, MostrarQrActivity::class.java)
                    intent.putExtra("idPedido", idPedido)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}