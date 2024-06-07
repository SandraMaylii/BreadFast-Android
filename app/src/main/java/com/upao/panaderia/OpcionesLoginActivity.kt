package com.upao.panaderia

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.upao.panaderia.databinding.ActivityOpcionesLoginBinding
import com.upao.panaderia.views.LoginEmailActivity

class OpcionesLoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOpcionesLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //Acceso para las vistas
        binding = ActivityOpcionesLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        comprobarSesion()

        binding.opcionEmail.setOnClickListener {
            startActivity(Intent(applicationContext,LoginEmailActivity::class.java))
        }


    }

    private fun comprobarSesion() {
       if (firebaseAuth.currentUser != null) {
           startActivity(Intent(this, MainActivity::class.java))
           finishAffinity()
       }
    }
}