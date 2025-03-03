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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.luismartingimeno.dogapi.data.model.DogBreedItem
import com.luismartingimeno.dogapi.screens.EditFavoriteDialog.EditFavoriteDialog
import com.luismartingimeno.dogapi.ui.FirestoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: FirestoreViewModel,
    navigateBack: () -> Unit,
    navigateToAddCustomFavorite: () -> Unit,
    navigateToModifyCustomFavorites: () -> Unit,

    ) {
    var favoriteBreeds by remember { mutableStateOf<List<DogBreedItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Observar la lista de favoritos desde el ViewModel
    val favorites by viewModel.favorites.observeAsState(emptyList())

    // Cargar los favoritos cuando la pantalla se inicie
    LaunchedEffect(Unit) {
        viewModel.getFavorites()
    }

    // Actualizar favoriteBreeds cuando la lista de favoritos cambie
    LaunchedEffect(favorites) {
        favoriteBreeds = favorites
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
                        viewModel = viewModel,
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
    viewModel: FirestoreViewModel,
    onRemoveFavorite: (DogBreedItem) -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) } // Estado para controlar el diálogo

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
            val imageUrl = if (breed.reference_image_id.startsWith("http")) {
                breed.reference_image_id
            } else {
                "https://cdn2.thedogapi.com/images/${breed.reference_image_id}.jpg"
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Imagen de ${breed.name}",
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
                    viewModel.removeFavorite(breed.id)
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

            Spacer(modifier = Modifier.height(8.dp))

            // Botón para editar el perro
            Button(
                onClick = { showEditDialog = true }, // Abrir el diálogo de edición
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar perro",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Editar", color = Color.White)
            }
        }
    }

    // Mostrar el diálogo de edición cuando showEditDialog es true
    if (showEditDialog) {
        EditFavoriteDialog(
            breed = breed,
            onDismiss = { showEditDialog = false }, // Cerrar el diálogo
            onSave = { updatedBreed ->
                // Actualizar el favorito en Firestore
                viewModel.updateFavorite(updatedBreed)
                showEditDialog = false // Cerrar el diálogo después de guardar
            }
        )
    }
}