package com.luismartingimeno.dogapi.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.luismartingimeno.dogapi.data.model.DogBreedItem
import kotlinx.coroutines.tasks.await

class FirestoreManager {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun getUserId(): String? = auth.currentUser?.uid

    suspend fun addFavorite(dog: DogBreedItem) {
        val userId = getUserId() ?: return
        firestore.collection("users")
            .document(userId)
            .collection("favorites")
            .document(dog.id.toString()) // Guardamos con su ID
            .set(dog)
            .await()
    }

    suspend fun getFavorites(): List<DogBreedItem> {
        val userId = getUserId() ?: return emptyList()
        val snapshot = firestore.collection("users")
            .document(userId)
            .collection("favorites")
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.toObject(DogBreedItem::class.java) }
    }

    suspend fun removeFavorite(breedId: Int) {
        val userId = getUserId() ?: return
        firestore.collection("users")
            .document(userId)
            .collection("favorites")
            .document(breedId.toString())
            .delete()
            .await()
    }
}
