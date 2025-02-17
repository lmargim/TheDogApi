package com.luismartingimeno.dogapi.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object SignUp

@Serializable
object ForgotPassword

@Serializable
object Home

@Serializable
data class BreedDetail(val breedId: Int)

@Serializable
object FavoritesScreen