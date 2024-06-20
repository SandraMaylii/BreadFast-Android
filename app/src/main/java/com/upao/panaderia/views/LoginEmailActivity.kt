package com.upao.panaderia.views

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.upao.panaderia.MainActivity
import com.upao.panaderia.R
import com.upao.panaderia.databinding.ActivityLoginEmailBinding
import com.upao.panaderia.databinding.ActivityOpcionesLoginBinding

class LoginEmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginEmailBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progessDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        progessDialog = ProgressDialog(this)
        progessDialog.setTitle("Espere por favor")
        progessDialog.setCanceledOnTouchOutside(false)

        binding.btnIngresar.setOnClickListener {
            validarInformacion()
        }



        binding.tvRegistrarme.setOnClickListener {
            startActivity(Intent(applicationContext, RegistroEmailActivity::class.java))
        }

        binding.tvRecuperarCuenta.setOnClickListener {
            startActivity(Intent(applicationContext, OlvidePassword::class.java))
        }
    }

    private var email = ""
    private var password = ""
    private fun validarInformacion() {

        email = binding.etEmail.text.toString().trim()
        password = binding.etPassword.text.toString().trim()

        if (email.isEmpty()) {
            binding.etEmail.error = "Ingrese Email"
            binding.etEmail.requestFocus()

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Email no válido"
            binding.etEmail.requestFocus()

        } else if (password.isEmpty()) {
            binding.etPassword.error = "Ingrese Contraseña"
            binding.etPassword.requestFocus()
        } else {
            logearUsuario()
        }
    }

    private fun logearUsuario() {
        progessDialog.setMessage("Ingresando")
        progessDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progessDialog.dismiss()
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            }
            .addOnFailureListener { e ->
                progessDialog.dismiss()
                Toast.makeText(
                    this,
                    "No se realizó el Inicio de Sesión debido a ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

            }

    }
}