package com.luismartingimeno.dogapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import com.google.firebase.firestore.FirebaseFirestore
import com.luismartingimeno.dogapi.data.AuthManager
import com.luismartingimeno.dogapi.navigation.Navegacion
import com.luismartingimeno.dogapi.ui.theme.TheDogApiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheDogApiTheme {
                val auth = remember { AuthManager() }
                auth.resetAuthState()
                auth.initializeGoogleSignIn(this)
                auth.signOut()

                // Inicializar Firestore
                val firestore = remember { FirebaseFirestore.getInstance() }

                Navegacion(auth, firestore) // Pasar Firestore a la navegación
            }
        }
    }
}
