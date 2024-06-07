package com.upao.panaderia.views

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.upao.panaderia.R
import com.upao.panaderia.databinding.ActivityLoginEmailBinding
import com.upao.panaderia.databinding.ActivityOpcionesLoginBinding

class LoginEmailActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginEmailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvRegistrarme.setOnClickListener {
            startActivity(Intent(applicationContext,RegistroEmailActivity::class.java))
        }

    }
}