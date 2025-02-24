package com.luismartingimeno.dogapi.screens.favoritesScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.luismartingimeno.dogapi.data.model.DogBreedItem
import com.luismartingimeno.dogapi.data.firebase.FirestoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    firestoreManager: FirestoreManager,
    navigateBack: () -> Unit,
    navigateToAddCustomFavorite: () -> Unit,
    navigateToModifyCustomFavorites: () -> Unit
) {
    var favoriteBreeds by remember { mutableStateOf<List<DogBreedItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        favoriteBreeds = firestoreManager.getFavorites()
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favoritos") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = navigateToAddCustomFavorite) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Custom Favorite"
                        )
                    }
                    // Botón para modificar perros personalizados
                    IconButton(onClick = navigateToModifyCustomFavorites) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Modify Custom Favorites"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(favoriteBreeds) { breed ->
                    DogBreedCard(
                        breed = breed,
                        firestoreManager = firestoreManager,
                        onRemoveFavorite = { removedBreed ->
                            favoriteBreeds = favoriteBreeds.filter { it.id != removedBreed.id }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DogBreedCard(
    breed: DogBreedItem,
    firestoreManager: FirestoreManager,
    onRemoveFavorite: (DogBreedItem) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Mostrar imagen de la raza del perro
            val imageUrl = breed.reference_image_id

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter("https://cdn2.thedogapi.com/images/${imageUrl}.jpg"),
                    contentDescription = "Imagen de ${breed.name}",
                    modifier = Modifier
                        .size(250.dp)
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = breed.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Grupo de raza: ${breed.breed_group}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Temperamento: ${breed.temperament}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botón para eliminar de favoritos
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        firestoreManager.removeFavorite(breed.id)
                    }
                    onRemoveFavorite(breed) // Actualizar la lista en la UI
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar de favoritos",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Eliminar de favoritos", color = Color.White)
            }

        }
    }
}
