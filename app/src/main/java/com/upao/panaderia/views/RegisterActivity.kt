package com.upao.panaderia.views


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.upao.panaderia.R
import com.upao.panaderia.databinding.ActivityRegisterBinding
import com.upao.panaderia.register.RegisterClientFragment
import com.upao.panaderia.register.RegisterSellerFragment

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            loadFragment(RegisterClientFragment())
        }

        initializeButtons()

        binding.registerClient.setOnClickListener {
            updateButtonStates(isClientSelected = true)
            loadFragment(RegisterClientFragment())
        }

        binding.registerSeller.setOnClickListener {
            updateButtonStates(isClientSelected = false)
            loadFragment(RegisterSellerFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }

    private fun initializeButtons() {
        binding.registerClient.setBackgroundResource(R.drawable.button_state_selected)
        binding.registerSeller.setBackgroundResource(R.drawable.button_state_selected)
        binding.registerClient.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.button)
        binding.registerSeller.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.no_select)
    }

    private fun updateButtonStates(isClientSelected: Boolean) {
        if (isClientSelected) {
            binding.registerClient.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.button)
            binding.registerSeller.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.no_select)
            binding.registerClient.setTextAppearance(R.style.ButtonSelected)
            binding.registerSeller.setTextAppearance(R.style.ButtonNoSelected)
        } else {
            binding.registerSeller.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.button)
            binding.registerClient.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.no_select)
            binding.registerSeller.setTextAppearance(R.style.ButtonSelected)
            binding.registerClient.setTextAppearance(R.style.ButtonNoSelected)
        }
    }



}


