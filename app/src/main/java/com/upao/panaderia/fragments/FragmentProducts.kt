package com.upao.panaderia.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.upao.panaderia.R
import com.upao.panaderia.adapters.ProductsAdapter
import com.upao.panaderia.databinding.FragmentHomeBinding
import com.upao.panaderia.databinding.FragmentProductsBinding
import com.upao.panaderia.models.adaptersModel.ProductAdapterModel
import java.util.Locale.filter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentProducts.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentProducts : Fragment() {
    private var _binding: FragmentProductsBinding? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mContext: android.content.Context
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var products: ArrayList<ProductAdapterModel>
    private var cardsOriginal: ArrayList<ProductAdapterModel> =
        ArrayList() // Copia de la lista original de productos para el filtrado.

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initializeButtons()
        setButtonListeners()
        /**
         * Inicializa la lista de productos, el adaptador y carga los productos,
         * copiando luego estos productos en la lista original para el filtro.
         *
         */
        products = ArrayList()
        productsAdapter = ProductsAdapter(products);
        uploadProducts()
        cardsOriginal.addAll(products)
        binding.rvProductos.layoutManager = GridLayoutManager(context, 2)
        binding.rvProductos.setHasFixedSize(true)
        binding.rvProductos.adapter = productsAdapter

        productsAdapter = ProductsAdapter(products)
        binding.rvProductos.layoutManager = GridLayoutManager(context, 2)
        binding.rvProductos.adapter = productsAdapter


        /**
         *  Se añade TextWatcher al campo de texto de búsqueda
         *  para filtrar los productos en tiempo real según la entrada del usuario.
         */

        binding.etBuscarProducto.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        return root


    }

    /**
     * Filtra los productos basándose en el texto de entrada del usuario.
     * Si el texto está vacío, muestra todos los productos.
     */
    fun filter(text: String) {
        if (text.isEmpty()) {
            productsAdapter.updateList(ArrayList(cardsOriginal))
        }
        val filtered = ArrayList<ProductAdapterModel>()
        for (item in products) {
            if (item.title.toLowerCase().contains(text.lowercase())) {
                filtered.add(item)
            }
        }

        productsAdapter.updateList(filtered)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        cargarInformacion()

    }


    private fun cargarInformacion() {

        //Referencia a la Base de datos
        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child("${firebaseAuth.uid}")
            .addValueEventListener(object : ValueEventListener {
                //Obtener la información de FireBase para las variables
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nombres = "${snapshot.child("nombres").value}"

                    binding.tvNombres.text = nombres
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    /**
     * Carga productos en la lista para ser mostrados.
     */
    private fun uploadProducts() {
        products.add(ProductAdapterModel("Pan de molde", "Pan de molde", R.drawable.pan12, 2.5))
        products.add(
            ProductAdapterModel(
                "Cocada",
                "Cocada Dulce",
                R.drawable.cocada,
                3.0
            )
        )
        products.add(
            ProductAdapterModel(
                "Empanada de Carne",
                "Empanada Rellena de Carne",
                R.drawable.empanada1,
                3.5
            )
        )
        products.add(
            ProductAdapterModel(
                "Alfajor",
                "Alfajor de Maicena con Marnjar Blanco",
                R.drawable.alfajor1,
                4.0
            )
        )
        products.add(
            ProductAdapterModel(
                "Croissant de Chocolate",
                "Croissant Relleno de Chocolate",
                R.drawable.pan10,
                4.5
            )
        )
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
        }
        binding.btnPanes.setOnClickListener {
            updateButtonStates(binding.btnPanes)
        }
        binding.btnBocaditos.setOnClickListener {
            updateButtonStates(binding.btnBocaditos)
        }
        binding.btnPiononos.setOnClickListener {
            updateButtonStates(binding.btnPiononos)
        }
    }

    private fun updateButtonStates(selectedButton: View) {
        val buttons =
            listOf(binding.btnTortas, binding.btnPanes, binding.btnBocaditos, binding.btnPiononos)
        buttons.forEach { button ->
            if (button == selectedButton) {
                setButtonState(button, R.drawable.button_state_selected, R.color.button)
            } else {
                setButtonState(button, R.drawable.button_state_selected, R.color.no_select)
            }
        }
    }


}