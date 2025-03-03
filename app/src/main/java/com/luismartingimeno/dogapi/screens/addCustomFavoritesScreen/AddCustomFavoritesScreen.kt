package com.luismartingimeno.dogapi.screens.addCustomFavorite

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.luismartingimeno.dogapi.data.model.DogBreedItem
import com.luismartingimeno.dogapi.ui.FirestoreViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCustomFavoriteScreen(
    viewModel: FirestoreViewModel,
    onBackClick: () -> Unit

) {
    var name by remember { mutableStateOf("") }
    var breedGroup by remember { mutableStateOf("") }
    var temperament by remember { mutableStateOf("") }
    var lifeSpan by remember { mutableStateOf("") }
    var reference_image_id by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    // Función para guardar el perro personalizado
    fun saveCustomFavorite() {
        coroutineScope.launch {
            val dogBreed = DogBreedItem(
                name = name,
                breed_group = breedGroup,
                temperament = temperament,
                life_span = lifeSpan,
                reference_image_id = reference_image_id
            )
            viewModel.addCustomFavorite(dogBreed)
            onBackClick()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Perro Personalizado") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = breedGroup,
                onValueChange = { breedGroup = it },
                label = { Text("Grupo de raza") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = temperament,
                onValueChange = { temperament = it },
                label = { Text("Temperamento") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = lifeSpan,
                onValueChange = { lifeSpan = it },
                label = { Text("Esperanza de vida") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = reference_image_id,
                onValueChange = { reference_image_id = it },
                label = { Text("url imagen") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = { saveCustomFavorite() }) {
                Text("Guardar Perro Personalizado")
            }
        }
    }
}