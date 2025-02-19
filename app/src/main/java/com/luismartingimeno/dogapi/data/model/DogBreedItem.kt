package com.luismartingimeno.dogapi.data.model

data class DogBreedItem(
    var firestoreId: Int? = null, // Cambiar a nullable
    val bred_for: String = "",
    val breed_group: String = "",
    val country_code: String = "",
    val description: String = "",
    val height: Height = Height(),
    val history: String = "",
    val id: Int = 0,
    val life_span: String = "",
    val name: String = "",
    val origin: String = "",
    val reference_image_id: String = "",
    val temperament: String = "",
    val weight: Weight = Weight(),
)