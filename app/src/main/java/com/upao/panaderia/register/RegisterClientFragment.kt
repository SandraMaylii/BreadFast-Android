package com.upao.panaderia.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.upao.panaderia.controllers.UserController
import com.upao.panaderia.databinding.FragmentRegisterClientBinding
import com.upao.panaderia.helpers.SharedPreferencesManager
import com.upao.panaderia.models.UserRequest
import com.upao.panaderia.views.HomeActivity
import java.util.Date

class RegisterClientFragment : Fragment() {

    private lateinit var userController: UserController
    private var _binding: FragmentRegisterClientBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterClientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userController = UserController(requireContext())

        // Añade oyentes de foco para el desplazamiento suave
        setFocusListener(binding.etNombreClient)
        setFocusListener(binding.etLastNameClient)
        setFocusListener(binding.etEmailClient)
        setFocusListener(binding.etPasswordClient)
        setFocusListener(binding.etConfirmPasswordClient)

        binding.btnGuardarClient.setOnClickListener {
            val name = binding.etNombreClient.text.toString()
            val lastName = binding.etLastNameClient.text.toString()
            val email = binding.etEmailClient.text.toString()
            val password = binding.etPasswordClient.text.toString()
            val confirmPassword = binding.etConfirmPasswordClient.text.toString()

            if (password != confirmPassword) {
                binding.etPasswordClient.error = "Las contraseñas no coinciden"
                binding.etConfirmPasswordClient.error = "Las contraseñas no coinciden"
                Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Complete todos los campos", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val createdAt = Date()
            val updatedAt = Date()

            val user = UserRequest(
                name,
                lastName,
                email,
                password,
                "client",
                1,
                0,
                createdAt.toString(),
                updatedAt.toString()
            )

            val success = userController.register(user)

            if (!success) {
                Toast.makeText(requireContext(), "El usuario ya existe", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                Toast.makeText(requireContext(), "Usuario registrado", Toast.LENGTH_SHORT).show()
                cleanFields()
                val saveUser = "$name,$lastName,$email"
                SharedPreferencesManager.setUserData(requireContext(), saveUser)
                val i = Intent(activity, HomeActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(i)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun cleanFields() {
        binding.etNombreClient.text.clear()
        binding.etLastNameClient.text.clear()
        binding.etEmailClient.text.clear()
        binding.etPasswordClient.text.clear()
        binding.etConfirmPasswordClient.text.clear()
    }

    private fun setFocusListener(editText: EditText) {
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.scrollView.post {
                    val extraScroll = 1500
                    binding.scrollView.smoothScrollTo(0, editText.top - extraScroll)
                }
            }
        }
    }
}
