package com.upao.panaderia.ui.cart
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.upao.panaderia.views.NiubizActivity
import com.upao.panaderia.adapters.CartAdapter
import com.upao.panaderia.adapters.ProductsAdapter
import com.upao.panaderia.controllers.PedidoController
import com.upao.panaderia.databinding.FragmentCartBinding
import com.upao.panaderia.helpers.SharedPreferencesManager
import com.upao.panaderia.models.adaptersModel.ProductAdapterModel
import com.upao.panaderia.models.requestModel.PedidosRequest
import java.util.ArrayList

class CartFragment : Fragment(), CartAdapter.OnProductInteraction, ProductsAdapter.OnItemClickListener {

    private var _binding: FragmentCartBinding? = null
    private lateinit var cartAdapter: CartAdapter
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var products: ArrayList<ProductAdapterModel>
    private lateinit var cartProducts: ArrayList<ProductAdapterModel>
    private lateinit var pedidoController: PedidoController
    private var total: Double = 0.0

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pedidoController = PedidoController(requireContext())

        val getUser = SharedPreferencesManager.getUserData(requireContext())
        var userLogged = ""
        if (getUser != null) {
            val user = getUser.split(",")
            userLogged = user[0]
        }
        products = ArrayList()
        cartProducts = ArrayList()
        cartAdapter = CartAdapter(cartProducts, this)
        productsAdapter = ProductsAdapter(products)

        // Establecer el listener del adaptador de productos
        productsAdapter.setOnItemClickListener(this)

        showCartProducts()

        binding.rvProductos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProductos.setHasFixedSize(true)
        binding.rvProductos.adapter = cartAdapter

        updateNextButtonVisibility()

        binding.btnSiguiente.setOnClickListener {
            total = 0.0
            cartProducts.forEach {
                total += it.price * it.quantity
            }
            val pedido = PedidosRequest(total = total, estado = "Pendiente", email = userLogged)

            pedidoController.createPedido(requireContext(), pedido) { idPedido ->
                if (idPedido.isNotEmpty()) {
                    val i = Intent(requireContext(), NiubizActivity::class.java)
                    i.putExtra("idPedido", idPedido)
                    startActivity(i)
                    requireActivity().finish()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showCartProducts() {
        cartProducts.clear()
        val savedProducts = SharedPreferencesManager.getCartProducts(requireContext())
        savedProducts?.forEach {
            val productData = it.split(",")
            if (productData.size == 5) {
                val title = productData[0]
                val description = productData[1]
                val image = productData[2]
                val price = productData[3].toDoubleOrNull() ?: 0.0
                val quantity = productData[4].toIntOrNull() ?: 1
                cartProducts.add(ProductAdapterModel(title, description, image, price, quantity))
            }
        }
        cartAdapter.notifyDataSetChanged()
        updateNextButtonVisibility()
    }

    fun addProductToCart(product: ProductAdapterModel) {
        val existingProduct = cartProducts.find { it.title == product.title && it.description == product.description }
        if (existingProduct != null) {
            existingProduct.quantity += 1 // Incrementa en 1 la cantidad del producto existente
        } else {
            product.quantity = 1 // Asegúrate de que la cantidad sea 1 para el nuevo producto
            cartProducts.add(product)
        }
        saveCartState()
        cartAdapter.notifyDataSetChanged()
        updateNextButtonVisibility()
    }

    override fun onDeleteProduct(position: Int) {
        cartProducts.removeAt(position)
        saveCartState()
        cartAdapter.notifyItemRemoved(position)
        updateNextButtonVisibility()
    }

    override fun onIncreaseQuantity(position: Int) {
        cartProducts[position].quantity++
        saveCartState()
        cartAdapter.notifyItemChanged(position)
        updateNextButtonVisibility()
    }

    override fun onDecreaseQuantity(position: Int) {
        if (cartProducts[position].quantity > 1) {
            cartProducts[position].quantity--
            saveCartState()
            cartAdapter.notifyItemChanged(position)
            updateNextButtonVisibility()
        }
    }

    private fun updateNextButtonVisibility() {
        binding.btnSiguiente.visibility = if (cartProducts.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun saveCartState() {
        val productStrings = cartProducts.map { "${it.title},${it.description},${it.image},${it.price},${it.quantity}" }
        SharedPreferencesManager.saveCartProducts(requireContext(), productStrings)
    }

    override fun onItemClick(product: ProductAdapterModel) {
        addProductToCart(product)
    }
}