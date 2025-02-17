package com.luismartingimeno.dogapi.screens.favoriteButton

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.luismartingimeno.dogapi.data.firebase.FirestoreManager
import com.luismartingimeno.dogapi.data.model.DogBreedItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun FavoriteButton(
    breed: DogBreedItem,
    firestoreManager: FirestoreManager
) {
    var isFavorite by remember { mutableStateOf(false) }

    // Comprobar si la raza está en favoritos
    LaunchedEffect(Unit) {
        val favorites = firestoreManager.getFavorites()
        isFavorite = favorites.any { it.id == breed.id }
    }

    IconButton(
        onClick = {
            isFavorite = !isFavorite
            CoroutineScope(Dispatchers.IO).launch {
                if (isFavorite) {
                    firestoreManager.addFavorite(breed)
                } else {
                    breed.id?.let { firestoreManager.removeFavorite(it) } // Cambiado para enviar el objeto completo
                }
            }
        }
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Favorito",
            tint = if (isFavorite) Color.Red else Color.Gray
        )
    }
}

