package com.upao.panaderia.views
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.upao.panaderia.R
import com.upao.panaderia.controllers.PedidoController

class MostrarQrActivity: AppCompatActivity() {
    private lateinit var pedidoController: PedidoController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_qr)

        // Inicializa el controlador de pedidos
        pedidoController = PedidoController(this)

        // Referencia al ImageView
        val imageView = findViewById<ImageView>(R.id.imageView)
        val textViewPedido = findViewById<TextView>(R.id.textViewPedido)

        // Obtener el ID del pedido del Intent
        val idPedido = intent.getStringExtra("idPedido") ?: ""

        // Obtener los detalles del pedido
        pedidoController.getPedido(this, idPedido.toInt()) { pedido ->
            textViewPedido.text = "ID: ${pedido.id}\nTotal: ${pedido.total}\nEstado: ${pedido.estado}\nUsuario ID: ${pedido.usuario_id}\nDirección: ${pedido.direccion}\nCreado: ${pedido.created_at}\nActualizado: ${pedido.updated_at}"
            showQrCode(imageView, pedido.qrCode)
            }


    }

    private fun showQrCode(imageView: ImageView, qrCode: String) {
        // Generar la URL del archivo QR basado en el ID del pedido
        val qrUrl = "https://firebasestorage.googleapis.com/v0/b/appbreadfast.appspot.com/o/pedido%2F$qrCode.png?alt=media"

        // Cargar la imagen usando Picasso
        Picasso.get()
            .load(qrUrl)
            .into(imageView, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    // Imagen cargada correctamente
                }

                override fun onError(e: Exception?) {
                    // Manejar cualquier error
                    Toast.makeText(imageView.context, "Error al cargar la imagen", Toast.LENGTH_SHORT).show()
                }
            })
    }
}