package com.upao.panaderia.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.upao.panaderia.R
import com.upao.panaderia.models.adaptersModel.ProductAdapterModel

class ProductsAdapter(private val cards: ArrayList<ProductAdapterModel>) :
    RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(productAdapterModel: ProductAdapterModel) {
            itemView.findViewById<TextView>(R.id.titleTextView).text = productAdapterModel.title
            itemView.findViewById<TextView>(R.id.subtitleTextView).text =
                productAdapterModel.description
            itemView.findViewById<ImageView>(R.id.imageView)
                .setImageResource(productAdapterModel.image)
            itemView.findViewById<TextView>(R.id.priceTextView).text =
                "S/ " + productAdapterModel.price

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cards[position])
    }

    fun updateList(filteredProducts: ArrayList<ProductAdapterModel>) {
        cards.clear()
        cards.addAll(filteredProducts)
        notifyDataSetChanged()
    }


}