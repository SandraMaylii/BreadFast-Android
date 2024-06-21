package com.upao.panaderia.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.Context
import com.upao.panaderia.MainActivity
import com.upao.panaderia.OpcionesLoginActivity
import com.upao.panaderia.R
import com.upao.panaderia.databinding.FragmentPerfilBinding
import com.upao.panaderia.views.CambiarPassword
import com.upao.panaderia.views.Constantes
import com.upao.panaderia.views.EditarInformacion
import com.upao.panaderia.views.Inicio

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentPerfil.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentPerfil : Fragment() {
    private lateinit var binding: FragmentPerfilBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mContext: android.content.Context


    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPerfilBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        cargarInformacion()

        //Nos dirige a la actividad EditarInformación
        binding.btnEditarCampos.setOnClickListener {
            startActivity(Intent(mContext, EditarInformacion::class.java))
        }

        binding.btnCambiarPass.setOnClickListener {
            startActivity(Intent(mContext, CambiarPassword::class.java))
        }


        binding.btnCerrarSesion.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(mContext, Inicio::class.java))
            activity?.finishAffinity()
        }
    }

    private fun cargarInformacion() {

        //Referencia a la Base de datos
        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child("${firebaseAuth.uid}")
            .addValueEventListener(object : ValueEventListener {
                //Obtener la información de FireBase para las variables
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nombres = "${snapshot.child("nombres").value}"
                    val apellidos = "${snapshot.child("Apellidos").value}"
                    val email = "${snapshot.child("email").value}"
                    val proveedor = "${snapshot.child("proveedor").value}"
                    var t_registro = "${snapshot.child("tiempoR").value}"
                    val imagen = "${snapshot.child("imagen").value}"

                    if (t_registro == null) {
                        t_registro = "0"
                    }

                    val fecha = Constantes.formatoFecha(t_registro.toLong())

                    /*Setear la información en las vistas*/

                    binding.tvNombres.text = nombres
                    binding.tvApellidos.text = apellidos
                    binding.tvEmail.text = email
                    binding.tvProveedor.text = proveedor
                    binding.tvTRegistro.text = fecha

                    /*Setear la imagen en el Iv*/

                    try {

                        Glide.with(mContext)
                            .load(imagen)
                            .placeholder(R.drawable.ic_img_perfil)
                            .into(binding.ivPerfil)

                    } catch (e: Exception) {
                        Toast.makeText(
                            mContext,
                            "${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    if (proveedor == "Email") {
                        binding.btnCambiarPass.visibility = View.VISIBLE

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }


}