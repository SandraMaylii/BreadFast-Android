package com.upao.panaderia.views

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.upao.panaderia.OpcionesLoginActivity
import com.upao.panaderia.R
import com.upao.panaderia.databinding.ActivityCambiarPasswordBinding
import java.util.zip.Inflater

class CambiarPassword : AppCompatActivity() {
    private lateinit var binding: ActivityCambiarPasswordBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fireBaseUser: FirebaseUser
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityCambiarPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        fireBaseUser = firebaseAuth.currentUser!!
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.IbRegresar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnCambiarPassword.setOnClickListener {
            validarInformacion()
        }

    }


    private var passActual = ""
    private var nuevaPass = ""
    private var r_NuevaPass = ""

    private fun validarInformacion() {

        passActual = binding.etPassActual.text.toString().trim()
        nuevaPass = binding.etPassNueva.text.toString().trim()
        r_NuevaPass = binding.etRPassword.text.toString().trim()

        if (passActual.isEmpty()) {
            binding.etPassActual.error = "Ingrese Contraseña Actual"
            binding.etPassActual.requestFocus()
        } else if (nuevaPass.isEmpty()) {
            binding.etPassNueva.error = "Ingrese Nueva Contraseña"
            binding.etPassNueva.requestFocus()
        } else if (r_NuevaPass.isEmpty()) {
            binding.etRPassword.error = "Repita Nueva Contraseña"
            binding.etRPassword.requestFocus()
        } else if (passActual == nuevaPass) {
            binding.etPassNueva.error = "La contraseña es la misma que la Actual"
            binding.etPassNueva.requestFocus()

        } else if (nuevaPass != r_NuevaPass) {
            binding.etRPassword.error = "No coinciden las Contraseñas"
            binding.etRPassword.requestFocus()
        } else {
            autenticarUsuario()
        }

    }

    private fun autenticarUsuario() {
        progressDialog.setMessage("Autenticando Usuario")
        progressDialog.show()

        val authCredential =
            EmailAuthProvider.getCredential(fireBaseUser.email.toString(), passActual)
        fireBaseUser.reauthenticate(authCredential)
            .addOnSuccessListener {
                actualizarPassword()

            }.addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Falló la Autenticación debido a: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

            }
    }

    private fun actualizarPassword() {


        progressDialog.setMessage("Cambiando Contraseña")
        progressDialog.show()

        fireBaseUser.updatePassword(nuevaPass)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Se actualizó la Contraseña",
                    Toast.LENGTH_SHORT
                ).show()

                cerrarSesion()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Falló la Autenticación debido a: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

            }
    }

    private fun cerrarSesion() {
        firebaseAuth.signOut()
        startActivity(Intent(applicationContext, OpcionesLoginActivity::class.java))
        finishAffinity()

    }
}