package com.luismartingimeno.dogapi.screens.modifyCustomFavorite

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.luismartingimeno.dogapi.data.model.DogBreedItem
import com.luismartingimeno.dogapi.data.firebase.FirestoreManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyCustomFavoriteScreen(
    firestoreManager: FirestoreManager,
    onBackClick: () -> Unit
) {
    var customBreeds by remember { mutableStateOf<List<DogBreedItem>>(emptyList()) }

    // Función para obtener perros personalizados
    LaunchedEffect(Unit) {
        customBreeds = firestoreManager.getFavorites()  // Obtener lista de perros personalizados
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
                // Aquí podrías agregar un botón para modificar el perro
                DogBreedCard(breed, firestoreManager)
            }
        }
    }
}

@Composable
fun DogBreedCard(breed: DogBreedItem, firestoreManager: FirestoreManager) {
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
            // Agregar más información si lo deseas, como editar, eliminar, etc.
        }
    }
}
