package com.upao.panaderia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.upao.panaderia.controllers.PedidoController
import com.upao.panaderia.helpers.SharedPreferencesManager
import com.upao.panaderia.views.HomeActivity
import com.upao.panaderia.views.RegisterActivity

class MainActivity : AppCompatActivity() {

    private val pedidoController = PedidoController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        pedidoController.getPedido(this, 100001) { pedido ->
            println(pedido)
        }

        val user = SharedPreferencesManager.getUserData(this)
        if (user != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        val button: Button = findViewById(R.id.btn_init_register)

        button.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}