package com.example.pokemonlist.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pokemonlist.R
import com.example.pokemonlist.data.Prefs
import com.example.pokemonlist.data.UserApplicationShared.Companion.prefs
import com.example.pokemonlist.databinding.ActivityMainBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAnalytics = Firebase.analytics

        //sett
        registerBasic()
        loginBasic()

        // Comprueba si hay un usuario registrado
        checkUserValues()

        // Configuración del inicio de sesión con Google
        val googleSign = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSign)
        auth = FirebaseAuth.getInstance()


        // Botón de inicio de sesión con Google
        binding.ibtnGmail.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)

        }
        // Configuración de Facebook Login
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(application)
        callbackManager = CallbackManager.Factory.create()

        binding.ibtnFacebook.setOnClickListener {
            LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("email", "public_profile"))
            LoginManager.getInstance()
                .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

                    override fun onSuccess(result: LoginResult) {
                        // Loguearse con Facebook
                        val token = result?.accessToken
                        val credential = FacebookAuthProvider.getCredential(token?.token!!)
                        auth.signInWithCredential(credential)
                            .addOnCompleteListener(this@MainActivity) { task ->
                                if (task.isSuccessful) {
                                    val user = auth.currentUser
                                    updateUI(user)
                                } else {
                                    Toast.makeText(
                                        applicationContext,
                                        "Authentication failed.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }

                    override fun onCancel() {
                        Toast.makeText(
                            this@MainActivity,
                            "Inicio de sesión cancelado",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onError(error: FacebookException) {
                        Toast.makeText(
                            this@MainActivity,
                            "Error al iniciar sesión: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }


    }

    private fun loginBasic() {
        binding.btnLogin.setOnClickListener {
            if (binding.etEmail.text.toString().isEmpty() || binding.etPassword.text.toString().isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            // inicio se sesion con firebase
            auth.signInWithEmailAndPassword(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    // Guardar el correo en las preferencias compartidas
                    prefs.saveEmail(binding.etEmail.text.toString())
                    // usuario a la nueva pantalla
                    val intent = Intent(this, PokemonListView::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Usuario no resgistrado o contraseña incorrecta", Toast.LENGTH_SHORT)
                }
            }
        }
    }

    private fun registerBasic() {
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterButtonView::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        callbackManager.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.result
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(
                            this,
                            "Inicio de sesión exitoso: ${user?.displayName}",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Guardar el correo en las preferencias compartidas
                        prefs.saveEmail(user?.email ?: "")
                        goToPokemonList()
                    } else {
                        Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }

    private fun goToPokemonList(){
        val intent = Intent(this, PokemonListView::class.java)
        startActivity(intent)
    }


    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val userEmail = user.email
            Toast.makeText(this, "Welcome, $userEmail", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PokemonListView::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "User is null", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private fun checkUserValues(){
        if (prefs.getEmail().isNotEmpty()){
            goToPokemonList()
        }
    }

}