package com.upao.panaderia.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.core.Context
import com.upao.panaderia.MainActivity
import com.upao.panaderia.OpcionesLoginActivity
import com.upao.panaderia.R
import com.upao.panaderia.databinding.FragmentPerfilBinding
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
    private lateinit var binding : FragmentPerfilBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mContext : android.content.Context


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

        binding.btnCerrarSesion.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(mContext,Inicio::class.java))
            activity?.finishAffinity()
        }
    }


}