package com.upao.panaderia.views

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.upao.panaderia.R
import com.upao.panaderia.databinding.ActivityOlvidePasswordBinding
import java.util.regex.Pattern

class OlvidePassword : AppCompatActivity() {
    private lateinit var binding: ActivityOlvidePasswordBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOlvidePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere Por Favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.IbRegresar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnEnviar.setOnClickListener {
            validarInformacion()
        }

    }

    private var email = ""
    private fun validarInformacion() {
        email = binding.etEmail.text.toString().trim()
        if (email.isEmpty()) {
            binding.etEmail.error = "Ingrese su Correo"
            binding.etEmail.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Correno no Valido"
            binding.etEmail.requestFocus()
        } else {
            enviarInstrucciones()
        }
    }

    private fun enviarInstrucciones() {
        progressDialog.setMessage("Enviando las Instrucciones a:  ${email}")
        progressDialog.show()



        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Instrucciones Enviadas",
                    Toast.LENGTH_SHORT
                ).show()
            }.addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Error en el Envio debio a ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

            }
    }
}