package com.upao.panaderia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.upao.panaderia.databinding.ActivityMainBinding
import com.upao.panaderia.fragments.FragmentPedidos
import com.upao.panaderia.fragments.FragmentPerfil
import com.upao.panaderia.fragments.FragmentProducts
import com.upao.panaderia.fragments.FragmentPuntos
import com.upao.panaderia.views.HomeActivity
import com.upao.panaderia.views.Inicio
import com.upao.panaderia.views.Login
import com.upao.panaderia.views.RegisterActivity

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializa la propiedad binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()



        if(firebaseAuth.currentUser == null){
            irOpcionesLogin()

        }

        //Por Defecto
        verInicio()

        binding.bottomNV.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_inicio -> {
                    verInicio()
                    true

                }

                R.id.item_pedidos -> {
                    verPedidos()
                    true

                }

                R.id.item_puntos -> {
                    verPuntos()
                    true

                }

                R.id.item_configuracion -> {
                    verPerfil()
                    true

                }

                else -> {
                    false
                }
            }
        }


    }

    private fun irOpcionesLogin() {
        startActivity(Intent(applicationContext,Inicio::class.java))
        finishAffinity()
    }

    private fun verPerfil() {
        binding.tvTitulo.text = "Perfil"
        val fragment = FragmentPerfil()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentoFL.id, fragment, "Fragment Perfil")
        fragmentTransaction.commit()

    }

    private fun verPedidos() {
        binding.tvTitulo.text = "Pedidos"
        val fragment = FragmentPedidos()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentoFL.id, fragment, "Fragment Pedidos")
        fragmentTransaction.commit()
    }

    private fun verPuntos() {
        binding.tvTitulo.text = "Puntos"
        val fragment = FragmentPuntos()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentoFL.id, fragment, "Fragment Puntos")
        fragmentTransaction.commit()

    }

    private fun verInicio() {
        binding.tvTitulo.text = "Inicio"
        val fragment = FragmentProducts()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentoFL.id, fragment, "Fragment Inicio")
        fragmentTransaction.commit()

    }

    /*val button: Button = findViewById(R.id.btn_init_register)

    button.setOnClickListener {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    val buttonLogin = findViewById<Button>(R.id.btn_init_session)
    buttonLogin.setOnClickListener {
        intent = Intent(this, Login::class.java)
        startActivity(intent)
    } */


}