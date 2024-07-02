package com.upao.panaderia.adapters
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.upao.panaderia.R
import com.upao.panaderia.models.adaptersModel.ProductAdapterModel

class ProductsAdapter(private var cards: ArrayList<ProductAdapterModel>) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(product: ProductAdapterModel)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun updateList(newList: ArrayList<ProductAdapterModel>) {
        cards = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_products, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listener?.let { holder.bind(cards[position], it) }
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivButton: ImageView = itemView.findViewById(R.id.imageButtonPlus)
        private val ivImg: ImageView = itemView.findViewById(R.id.imageView)

        @SuppressLint("SetTextI18n")
        fun bind(productAdapterModel: ProductAdapterModel, listener: OnItemClickListener) {
            Picasso.get().load(productAdapterModel.image).into(ivImg)
            itemView.findViewById<TextView>(R.id.titleTextView).text = productAdapterModel.title
            itemView.findViewById<TextView>(R.id.subtitleTextView).text = productAdapterModel.description
            itemView.findViewById<TextView>(R.id.priceTextView).text = "S/ ${productAdapterModel.price}"
            ivButton.setOnClickListener {
                listener.onItemClick(productAdapterModel)
            }
        }
    }
}