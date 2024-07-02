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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.upao.panaderia.R
import com.upao.panaderia.adapters.ProductsAdapter
import com.upao.panaderia.databinding.FragmentProductsBinding
import com.upao.panaderia.models.adaptersModel.ProductAdapterModel

class FragmentProducts : Fragment(), ProductsAdapter.OnItemClickListener {
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var products: ArrayList<ProductAdapterModel>
    private var cardsOriginal: ArrayList<ProductAdapterModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initializeButtons()
        setButtonListeners()

        products = ArrayList()
        productsAdapter = ProductsAdapter(products)
        productsAdapter.setOnItemClickListener(this)
        binding.rvProductos.layoutManager = GridLayoutManager(context, 2)
        binding.rvProductos.setHasFixedSize(true)
        binding.rvProductos.adapter = productsAdapter

        cardsOriginal.addAll(products)

        binding.etBuscarProducto.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        return root
    }

    private fun filter(text: String) {
        val filtered = if (text.isEmpty()) {
            ArrayList(cardsOriginal)
        } else {
            val filteredList = ArrayList<ProductAdapterModel>()
            for (item in cardsOriginal) {
                if (item.title.lowercase().contains(text.lowercase())) {
                    filteredList.add(item)
                }
            }
            filteredList
        }
        productsAdapter.updateList(filtered)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        cargarInformacion()
    }

    private fun cargarInformacion() {
        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child("${firebaseAuth.uid}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nombres = "${snapshot.child("nombres").value}"
                    binding.tvNombres.text = nombres
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
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
        binding.btnTortas.setOnClickListener { updateButtonStates(binding.btnTortas) }
        binding.btnPanes.setOnClickListener { updateButtonStates(binding.btnPanes) }
        binding.btnBocaditos.setOnClickListener { updateButtonStates(binding.btnBocaditos) }
        binding.btnPiononos.setOnClickListener { updateButtonStates(binding.btnPiononos) }
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

    override fun onItemClick(product: ProductAdapterModel) {
        // Implementar la acción al hacer clic en un producto
    }
}