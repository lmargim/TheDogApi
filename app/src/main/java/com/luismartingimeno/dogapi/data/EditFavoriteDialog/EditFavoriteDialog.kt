package com.luismartingimeno.dogapi.data.EditFavoriteDialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.luismartingimeno.dogapi.data.model.DogBreedItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFavoriteDialog(
    breed: DogBreedItem,
    onDismiss: () -> Unit,
    onSave: (DogBreedItem) -> Unit
) {
    var name by remember { mutableStateOf(breed.name) }
    var breedGroup by remember { mutableStateOf(breed.breed_group ?: "") }
    var temperament by remember { mutableStateOf(breed.temperament ?: "") }
    var lifeSpan by remember { mutableStateOf(breed.life_span ?: "") }
    var referenceImageId by remember { mutableStateOf(breed.reference_image_id ?: "") }
    val coroutineScope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Favorito") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = breedGroup,
                    onValueChange = { breedGroup = it },
                    label = { Text("Grupo de Raza") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = temperament,
                    onValueChange = { temperament = it },
                    label = { Text("Temperamento") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = lifeSpan,
                    onValueChange = { lifeSpan = it },
                    label = { Text("Esperanza de Vida") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = referenceImageId,
                    onValueChange = { referenceImageId = it },
                    label = { Text("URL de Imagen") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                coroutineScope.launch {
                    val updatedBreed = breed.copy(
                        name = name,
                        breed_group = breedGroup,
                        temperament = temperament,
                        life_span = lifeSpan,
                        reference_image_id = referenceImageId
                    )
                    onSave(updatedBreed)
                }
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
