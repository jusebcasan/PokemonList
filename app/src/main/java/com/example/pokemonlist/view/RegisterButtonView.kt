package com.example.pokemonlist.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pokemonlist.databinding.ActivityRegisterButtonBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterButtonView : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterButtonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterButtonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerBasic()
        binding.tvTermsAndConditions.setOnClickListener {
            val intent = Intent(this, TermsAndConditions::class.java)
            startActivity(intent)
        }
    }

    private fun registerBasic() {
        title = "Autenticacion"

        binding.btnRegister.setOnClickListener {
            if (binding.etPasswordOne.text.toString() != binding.etPasswordTwo.text.toString()) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (binding.etEmail.text.toString()
                    .isNotEmpty() && binding.etPasswordOne.text.toString()
                    .isNotEmpty() && binding.etPasswordTwo.text.toString().isNotEmpty() && binding.cbTermsAndConditions.isChecked
            ) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.etEmail.text.toString(),
                    binding.etPasswordOne.text.toString()
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this, PokemonListView::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Error al autenticar el usuario",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            } else {
                Toast.makeText(this, "Campos vacios o terminos no aceptados", Toast.LENGTH_SHORT).show()
            }
        }
    }
}