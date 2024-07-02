package com.upao.panaderia.register

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.upao.panaderia.MainActivity
import com.upao.panaderia.controllers.UserController
import com.upao.panaderia.views.HomeActivity
import com.upao.panaderia.databinding.FragmentRegisterClientBinding
import com.upao.panaderia.helpers.SharedPreferencesManager
import com.upao.panaderia.models.requestModel.RegisterRequest
import com.upao.panaderia.models.requestModel.UserRequest
import com.upao.panaderia.views.Constantes
import java.util.Date
import java.util.regex.Pattern

class RegisterClientFragment : Fragment() {

    private lateinit var userController: UserController
    private var _binding: FragmentRegisterClientBinding? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

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

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        userController = UserController(requireContext())
        binding.btnGuardarClient.setOnClickListener {

            val name = binding.etNombreClient.text.toString()
            val lastName = binding.etLastNameClient.text.toString()
            val email = binding.etEmailClient.text.toString()
            val password = binding.etPasswordClient.text.toString()
            val confirmPassword = binding.etConfirmPasswordClient.text.toString()

            if (!isEmailValid(email)) {
                binding.etEmailClient.error = "Correo electrónico no válido"
                Toast.makeText(requireContext(), "Correo electrónico no válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isPasswordValid(password)) {
                binding.etPasswordClient.error = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un carácter especial"
                Toast.makeText(requireContext(), "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un carácter especial", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                binding.etPasswordClient.error = "Las contraseñas no coinciden"
                binding.etConfirmPasswordClient.error = "Las contraseñas no coinciden"
                Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(name.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = RegisterRequest(name, lastName, email, password)

            registrarUsuario(user)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun isEmailValid(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return Pattern.matches(emailPattern, email)
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$"
        return Pattern.matches(passwordPattern, password)
    }

    private fun registrarUsuario(user: RegisterRequest) {
        progressDialog.setMessage("Creando Cuenta")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnSuccessListener {
                crearUsuario(user)
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    requireContext(),
                    "Fallo la creación de la cuenta debido a ${e.message}",
                    Toast.LENGTH_SHORT

                ).show()
            }
    }

    private fun crearUsuario(user: RegisterRequest) {
        progressDialog.setMessage("Guardando Información")

        val uidU = firebaseAuth.uid
        val nombresU = user.nombres
        val apellidosU = user.apellidos
        val emailU = firebaseAuth.currentUser!!.email
        val tiempoR = Constantes.obtenerTiempoD()

        val datosUsuario = HashMap<String, Any>()
        datosUsuario["uid"] = "$uidU"
        datosUsuario["nombres"] = "$nombresU"
        datosUsuario["Apellidos"] = "$apellidosU"
        datosUsuario["email"] = "$emailU"
        datosUsuario["tiempoR"] = "$tiempoR"
        datosUsuario["proveedor"] = "Email"
        datosUsuario["imagen"] = ""

        val reference = FirebaseDatabase.getInstance().getReference(("Usuarios"))
        reference.child(uidU!!)
            .setValue(datosUsuario)
            .addOnSuccessListener {
                progressDialog.dismiss()

                userController.register(requireContext(), user) { isSuccess ->
                    if (isSuccess) {
                        val saveUser = "${user.email},${user.password}"
                        SharedPreferencesManager.setUserData(requireContext(), saveUser)
                        val intent = Intent(requireContext(), HomeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }
                }
            }.addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    requireContext(),
                    "Fallo la creación de la cuenta debido a ${e.message}",
                    Toast.LENGTH_SHORT

                ).show()

            }

    }
}