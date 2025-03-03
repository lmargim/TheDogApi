package com.luismartingimeno.dogapi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.luismartingimeno.dogapi.data.AuthManager
import com.luismartingimeno.dogapi.data.firebase.FirestoreManager
import com.luismartingimeno.dogapi.screens.addCustomFavorite.AddCustomFavoriteScreen
import com.luismartingimeno.dogapi.screens.homeScreen.HomeScreen
import com.luismartingimeno.dogapi.screens.loginScreen.LoginScreen
import com.luismartingimeno.dogapi.screens.breedDetailScreen.BreedDetailScreen
import com.luismartingimeno.dogapi.screens.forgotPasswordScreen.ForgotPasswordScreen
import com.luismartingimeno.dogapi.screens.singUpScreen.SignUpScreen
import com.luismartingimeno.dogapi.screens.favoritesScreen.FavoritesScreen
import com.luismartingimeno.dogapi.ui.FirestoreViewModel

@Composable
fun Navegacion(auth: AuthManager, viewModel: FirestoreViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Login) {

        // Pantalla de Login
        composable<Login> {
            LoginScreen(auth,
                {
                    navController.navigate(Home) {
                        popUpTo(Login) {
                            inclusive = true
                        }
                    }
                },
                {
                    navController.navigate(SignUp)
                },
                {
                    navController.navigate(ForgotPassword)
                }
            )
        }

        // Pantalla de Registro
        composable<SignUp> {
            SignUpScreen(auth) {
                navController.navigate(Login) {
                    popUpTo(Login) { inclusive = true }
                }
            }
        }

        // Pantalla de Recuperación de Contraseña
        composable<ForgotPassword> {
            ForgotPasswordScreen(auth) {
                navController.navigate(Login) {
                    popUpTo(Login) { inclusive = true }
                }
            }
        }

        // Pantalla de Home
        composable<Home> { backStackEntry ->
            // Crear una instancia de FirestoreManager usando firestore
            val firestoreManager = FirestoreManager()

            HomeScreen(
                auth = auth,
                navigateToLogin = {
                    navController.navigate(Login) {
                        popUpTo(Login) { inclusive = true }
                    }
                },
                navigateToBreedDetail = { breed ->
                    navController.navigate(BreedDetail(breed.id))
                },
                navigateToFavorites = {
                    navController.navigate(FavoritesScreen)
                },
                firestoreManager = firestoreManager  // Pasamos la instancia de FirestoreManager aquí
            )
        }


        // Pantalla de Detalle de la Raza
        composable<BreedDetail> { backStackEntry ->
            val breedId = backStackEntry.toRoute<BreedDetail>().breedId
            BreedDetailScreen(
                viewModel,
                breedId = breedId,
            ) {
                navController.popBackStack()
            }
        }

        // Pantalla de Favoritos
        composable<FavoritesScreen> {
            val firestoreManager = FirestoreManager()

            FavoritesScreen(
                viewModel,
                navigateBack = { navController.popBackStack() },
                navigateToAddCustomFavorite = { navController.navigate(AddCustomFavoriteScreen) },
                navigateToModifyCustomFavorites = {
                    navController.navigate(
                        ModifyCustomFavoriteScreen
                    )
                }
            )
        }

        // Pantalla de Añadir Favorito Personalizado
        composable<AddCustomFavoriteScreen> {
            val firestoreManager = FirestoreManager()
            AddCustomFavoriteScreen(
                viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
