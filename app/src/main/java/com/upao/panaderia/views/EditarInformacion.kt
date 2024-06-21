package com.upao.panaderia.views

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.upao.panaderia.R
import com.upao.panaderia.databinding.ActivityEditarInformacionBinding
import com.upao.panaderia.databinding.ActivityLoginEmailBinding
import com.upao.panaderia.databinding.ActivityRegistroEmailBinding
import com.upao.panaderia.databinding.FragmentPerfilBinding
import com.upao.panaderia.fragments.FragmentPerfil

class EditarInformacion : AppCompatActivity() {

    private lateinit var binding: ActivityEditarInformacionBinding
    private lateinit var fireBaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    //Almacenar la img seleccionada de la Galeria
    private var imagenUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarInformacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fireBaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)


        cargarInformacion()

        //Volver
        binding.IbRegresar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.IvEditarImg.setOnClickListener {
            //Verificar la versión de Android
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                abrirGaleria()
            } else {
                solicitarPermisoAlmacenamiento.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }

        }

        binding.btnActualizar.setOnClickListener {
            validarInformacion()
        }

    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galeriaARL.launch(intent)

    }

    private val galeriaARL =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == Activity.RESULT_OK) {
                val data = resultado.data
                imagenUri = data!!.data
                subirImagenStorage(imagenUri)
            } else {
                Toast.makeText(
                    this,
                    "Cancelado",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun subirImagenStorage(imagenUri: Uri?) {
        progressDialog.setMessage("Subiendo Imágen")
        progressDialog.show()
        val rutaImagen = "ImagenesPerfil/" + fireBaseAuth.uid
        val ref = FirebaseStorage.getInstance().getReference(rutaImagen)
        ref.putFile(imagenUri!!)
            .addOnSuccessListener { taskSnapShot ->
                val uriTask = taskSnapShot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val urlImagenCargada = uriTask.result.toString()
                if (uriTask.isSuccessful) {
                    actualziarInformacionBaseDatods(urlImagenCargada)
                }
            }
            .addOnFailureListener { e ->
            }
    }

    private fun actualziarInformacionBaseDatods(urlImagenCargada: String) {
        progressDialog.setMessage("Actualizando Imágen")
        progressDialog.show()

        val hasMap: HashMap<String, Any> = HashMap()
        if (imagenUri != null) {
            hasMap["imagen"] = urlImagenCargada
        }

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(fireBaseAuth.uid!!)
            .updateChildren(hasMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Su Imágen de Perfil se ha Actualizado",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()

            }
    }

    private val solicitarPermisoAlmacenamiento =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { esConcedido ->
            if (esConcedido) {
                abrirGaleria()
            } else {
                Toast.makeText(
                    this,
                    "El permiso de Almacenamiento ha sido denegado",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private var nombres = ""
    private var apellidos = ""
    private var emails = ""
    val pattern = Regex("^[A-Za-zÁÉÍÓÚáéíóúÑñ]+(?:\\s+[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$")


    private fun validarInformacion() {
        nombres = binding.etNombres.text.toString().trim()
        apellidos = binding.etApellidos.text.toString().trim()
        emails = binding.email.text.toString().trim()

        if (nombres.isEmpty()) {
            binding.etNombres.error = "Ingrese Nombres"
            binding.etNombres.requestFocus()
        } else if (!pattern.matches(nombres)) {
            binding.etNombres.error = "Nombre Inválido. Solo letras permitidas."
            binding.etNombres.requestFocus()
        } else if (!pattern.matches(apellidos)) {
            binding.etApellidos.error = "Apelldio Inválido. Solo letras permitidas."
            binding.etApellidos.requestFocus()
        } else if (apellidos.isEmpty()) {
            binding.etApellidos.error = "Ingrese Apellidos"
            binding.etApellidos.requestFocus()
        } else if (emails.isEmpty()) {
            binding.email.error = "Ingrese Correo"
            binding.email.requestFocus()

        } else if (!Patterns.EMAIL_ADDRESS.matcher(emails).matches()) {

            binding.email.error = "Correo Inválido"
            binding.email.requestFocus()
        } else {
            actualizarInformacion()
        }
    }

    private fun actualizarInformacion() {
        progressDialog.setMessage("Actualizando Información")
        progressDialog.show()

        val hasMap: HashMap<String, Any> = HashMap()

        hasMap["nombres"] = nombres
        hasMap["Apellidos"] = apellidos
        hasMap["email"] = emails

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(fireBaseAuth.uid!!)
            .updateChildren(hasMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(
                    applicationContext,
                    "Se actualizó su información",
                    Toast.LENGTH_SHORT
                ).show()
            }

            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    applicationContext,
                    "${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

            }
    }

    private fun cargarInformacion() {

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child("${fireBaseAuth.uid}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    // Obtener los datos del usuario desde el snapshot
                    val nombres = "${snapshot.child("nombres").value}"
                    val apellidos = "${snapshot.child("Apellidos").value}"
                    val imagen = "${snapshot.child("imagen").value}"
                    val email = "${snapshot.child("email").value}"


                    // Actualizar los campos de texto con los nombres y apellidos del usuario
                    binding.etNombres.setText(nombres)
                    binding.etApellidos.setText(apellidos)
                    binding.email.setText(email)


                    try {
                        // Cargar la imagen de perfil del usuario usando Glide
                        Glide.with(applicationContext)
                            .load(imagen)
                            .placeholder(R.drawable.ic_img_perfil)
                            .into(binding.ivPerfil)
                    } catch (e: Exception) {
                        Toast.makeText(
                            applicationContext,
                            "${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

    }


}