package com.upao.panaderia.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.upao.panaderia.R
import com.upao.panaderia.models.adaptersModel.ProductAdapterModel
import com.upao.panaderia.ui.cart.CartFragment

class CartAdapter(
    private val products: ArrayList<ProductAdapterModel>,
    private val onProductInteraction: CartFragment
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    interface OnProductInteraction {
        fun onDeleteProduct(position: Int)
        fun onIncreaseQuantity(position: Int)
        fun onDecreaseQuantity(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position], position)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivImg: ImageView = itemView.findViewById(R.id.ivImagenProducto)
        private val tvNombreProducto: TextView = itemView.findViewById(R.id.tvNombreProducto)
        private val tvDescripcionProducto: TextView = itemView.findViewById(R.id.tvDescripcionProducto)
        private val tvPrecioProducto: TextView = itemView.findViewById(R.id.tvPrecioProducto)
        private val tvCantidadProducto: TextView = itemView.findViewById(R.id.tvCantidadProducto)
        private val tvTotalProducto: TextView = itemView.findViewById(R.id.tvTotalProducto)
        private val btnEliminarProducto: ImageButton = itemView.findViewById(R.id.btnEliminarProducto)
        private val btnAumentarCantidad: ImageButton = itemView.findViewById(R.id.btnAumentarCantidad)
        private val btnDisminuirCantidad: ImageButton = itemView.findViewById(R.id.btnDisminuirCantidad)

        @SuppressLint("SetTextI18n")
        fun bind(product: ProductAdapterModel, position: Int) {
            Picasso.get().load(product.image).into(ivImg)
            tvNombreProducto.text = product.title
            tvDescripcionProducto.text = product.description
            tvPrecioProducto.text = "S/ " + product.price
            tvCantidadProducto.text = product.quantity.toString()
            tvTotalProducto.text = "Total: S/ " + (product.price * product.quantity).toString()

            btnEliminarProducto.setOnClickListener {
                onProductInteraction.onDeleteProduct(position)
            }

            btnAumentarCantidad.setOnClickListener {
                onProductInteraction.onIncreaseQuantity(position)
            }

            btnDisminuirCantidad.setOnClickListener {
                onProductInteraction.onDecreaseQuantity(position)
            }
        }
    }
}