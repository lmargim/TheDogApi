package com.luismartingimeno.dogapi.data.model

data class DogImage(
    val id: String,
    val url: String,
    val breeds: List<DogBreedItem>? = null // Puede estar vacío
)