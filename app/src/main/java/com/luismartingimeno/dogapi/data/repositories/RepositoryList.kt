package com.luismartingimeno.dogapi.data.repositories

import com.luismartingimeno.dogapi.data.repositories.RemoteConnection.remoteService
import com.luismartingimeno.dogapi.data.model.DogBreedItem

object RepositoryList {
    suspend fun getBreeds(): List<DogBreedItem> {
        return try {
            // Realizamos la llamada a la API
            val response = remoteService.getBreeds()
            if (response.isSuccessful) {
                // Si la respuesta es exitosa, devolvemos la lista de razas
                response.body() ?: emptyList()
            } else {
                // Si la respuesta no fue exitosa, devolvemos una lista vacía
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
