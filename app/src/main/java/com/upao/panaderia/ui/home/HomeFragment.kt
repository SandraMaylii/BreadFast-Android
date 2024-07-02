package com.upao.panaderia.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.upao.panaderia.R
import com.upao.panaderia.adapters.ProductsAdapter
import com.upao.panaderia.controllers.ProductsController
import com.upao.panaderia.databinding.FragmentHomeBinding
import com.upao.panaderia.helpers.SharedPreferencesManager
import com.upao.panaderia.models.adaptersModel.ProductAdapterModel
import java.util.ArrayList

class HomeFragment : Fragment(), ProductsAdapter.OnItemClickListener {
    private var _binding: FragmentHomeBinding? = null
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var products: ArrayList<ProductAdapterModel>
    private lateinit var productsController: ProductsController

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productsController = ProductsController(requireContext())

        val getUser = SharedPreferencesManager.getUserData(requireContext())
        val user = getUser?.split(",")
        val userLogged = user?.get(0)
        binding.tvClientName.text = userLogged

        initializeButtons()
        setButtonListeners()

        products = ArrayList()
        productsAdapter = ProductsAdapter(products)

        uploadProducts(1)

        binding.rvProductos.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProductos.setHasFixedSize(true)
        binding.rvProductos.adapter = productsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeButtons() {
        setButtonState(binding.btnTortas, R.drawable.button_state_selected, R.color.button)
        setButtonState(binding.btnPanes, R.drawable.button_state_selected, R.color.no_select)
        setButtonState(binding.btnBocaditos, R.drawable.button_state_selected, R.color.no_select)
        setButtonState(binding.btnPiononos, R.drawable.button_state_selected, R.color.no_select)
    }

    private fun setButtonState(button: View, backgroundRes: Int, tintRes: Int) {
        button.setBackgroundResource(backgroundRes)
        button.backgroundTintList = ContextCompat.getColorStateList(requireContext(), tintRes)
    }

    private fun setButtonListeners() {
        binding.btnTortas.setOnClickListener {
            updateButtonStates(binding.btnTortas)
            uploadProducts(1)
        }
        binding.btnPanes.setOnClickListener {
            updateButtonStates(binding.btnPanes)
            uploadProducts(2)
        }
        binding.btnBocaditos.setOnClickListener {
            updateButtonStates(binding.btnBocaditos)
            uploadProducts(3)
        }
        binding.btnPiononos.setOnClickListener {
            updateButtonStates(binding.btnPiononos)
            uploadProducts(4)
        }
    }

    private fun updateButtonStates(selectedButton: View) {
        val buttons = listOf(binding.btnTortas, binding.btnPanes, binding.btnBocaditos, binding.btnPiononos)
        buttons.forEach { button ->
            if (button == selectedButton) {
                setButtonState(button, R.drawable.button_state_selected, R.color.button)
            } else {
                setButtonState(button, R.drawable.button_state_selected, R.color.no_select)
            }
        }
    }

    private fun uploadProducts(category: Int) {
        products.clear()
        when (category) {
            1 -> {
                getProducts(1)
            }
            2 -> {
                getProducts(2)
            }
            3 -> {
                getProducts(3)
            }
            4 -> {
                getProducts(4)
            }
            else -> {
                getProducts(1)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getProducts(id: Int) {
        productsController.getProducts(requireContext(), id) { productsList ->
            productsList.forEach {
                products.add(
                    ProductAdapterModel(it.nombre, it.descripcion, it.imagen, it.precio, 1) // Agregar cantidad por defecto
                )
            }
            productsAdapter.notifyDataSetChanged()
        }
    }

    override fun onItemClick(product: ProductAdapterModel) {
        Toast.makeText(requireContext(), "Producto añadido al carrito", Toast.LENGTH_SHORT).show()
        val existingProduct = SharedPreferencesManager.getCartProducts(requireContext())?.find {
            val productData = it.split(",")
            productData[0] == product.title && productData[1] == product.description
        }

        if (existingProduct != null) {
            // Incrementar la cantidad del producto existente
            val productData = existingProduct.split(",")
            val quantity = productData[4].toInt() + 1
            val updatedProductStr = "${product.title},${product.description},${product.image},${product.price},$quantity"
            SharedPreferencesManager.updateProduct(requireContext(), updatedProductStr)
        } else {
            // Agregar el nuevo producto
            val productStr = "${product.title},${product.description},${product.image},${product.price},${product.quantity}"
            SharedPreferencesManager.saveProduct(requireContext(), productStr)
        }
    }
}