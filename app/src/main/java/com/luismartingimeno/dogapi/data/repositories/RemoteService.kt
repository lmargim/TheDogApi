package com.luismartingimeno.dogapi.data.repositories

import com.luismartingimeno.dogapi.data.model.DogBreedItem
import com.luismartingimeno.dogapi.data.model.DogImage
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteService {
    // Obtiene la lista de todas las razas de perros
    @GET("breeds")
    suspend fun getBreeds(): Response<List<DogBreedItem>>

    // Obtiene una imagen aleatoria de una raza específica por ID
    @GET("images/search")
    suspend fun getRandomImageByBreed(@Query("breed_id") breedId: String): Response<List<DogImage>>

    // Obtiene una imagen aleatoria de una raza por su nombre (si lo necesitas)
    @GET("breeds/search")
    suspend fun getBreedByName(@Query("q") breedName: String): Response<List<DogBreedItem>>
}
