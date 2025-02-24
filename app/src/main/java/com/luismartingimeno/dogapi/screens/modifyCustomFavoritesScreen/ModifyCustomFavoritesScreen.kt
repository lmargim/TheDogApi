package com.luismartingimeno.dogapi.screens.modifyCustomFavorite

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.luismartingimeno.dogapi.data.EditFavoriteDialog.EditFavoriteDialog
import com.luismartingimeno.dogapi.data.model.DogBreedItem
import com.luismartingimeno.dogapi.data.firebase.FirestoreManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyCustomFavoriteScreen(
    firestoreManager: FirestoreManager,
    onBackClick: () -> Unit
) {
    var customBreeds by remember { mutableStateOf<List<DogBreedItem>>(emptyList()) }
    var selectedBreed by remember { mutableStateOf<DogBreedItem?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Obtener lista de perros personalizados
    LaunchedEffect(Unit) {
        customBreeds = firestoreManager.getFavorites()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Modificar Perro Personalizado") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(customBreeds) { breed ->
                DogBreedCard(breed) { selectedBreed = breed }
            }
        }
    }

    // Mostrar el diálogo de edición cuando hay un perro seleccionado
    selectedBreed?.let { breed ->
        EditFavoriteDialog(
            breed = breed,
            onDismiss = { selectedBreed = null },
            onSave = { updatedBreed ->
                coroutineScope.launch {
                    // Actualizar Firestore y la UI local
                    val updatedList = customBreeds.map { if (it.id == updatedBreed.id) updatedBreed else it }
                    customBreeds = updatedList
                    firestoreManager.updateFavorite(updatedBreed) // Ahora dentro de launch
                    selectedBreed = null
                }
            }
        )
    }

}

@Composable
fun DogBreedCard(breed: DogBreedItem, onEditClick: (DogBreedItem) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = breed.name)
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { onEditClick(breed) }) {
                Text("Editar")
            }
        }
    }
}
