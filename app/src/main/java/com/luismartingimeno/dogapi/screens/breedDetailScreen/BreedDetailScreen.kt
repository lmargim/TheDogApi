package com.luismartingimeno.dogapi.screens.breedDetailScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.luismartingimeno.dogapi.scaffold.TopBar

@Composable
fun BreedDetailScreen(
    breedId: Int,
    viewModel: BreedDetailViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val breedDetail by viewModel.breedDetail.observeAsState()
    val loading by viewModel.loading.observeAsState(false)
    val error by viewModel.error.observeAsState()

    // Obtener detalles de la raza al cargar la pantalla
    LaunchedEffect(breedId) {
        viewModel.getBreedDetailById(breedId)
    }

    Scaffold(
        topBar = {
            breedDetail?.let {
                TopBar(
                    titulo = it.name,
                    navigateToLogin = onBackClick
                )
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFFF5A623), Color(0xFFE7B142)))), // Fondo degradado
            contentAlignment = Alignment.Center
        ) {
            if (loading == true) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(50.dp)
                )
            } else if (error != null) {
                Text(text = error ?: "Error desconocido", color = Color.Red)
            } else {
                breedDetail?.let { breed ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White)
                            .shadow(8.dp, RoundedCornerShape(24.dp)), // Sombras y bordes redondeados
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Mostrar la imagen de la raza
                        Image(
                            painter = rememberAsyncImagePainter("https://cdn2.thedogapi.com/images/${breed.reference_image_id}.jpg"),
                            contentDescription = "Imagen de ${breed.name}",
                            modifier = Modifier
                                .size(250.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .padding(8.dp)
                                .shadow(4.dp, RoundedCornerShape(16.dp)) // Sombras para la imagen
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Información sobre la raza
                        Text(
                            text = "Nombre: ${breed.name}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = "Grupo: ${breed.breed_group ?: "Desconocido"}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = "Temperamento: ${breed.temperament}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = "Esperanza de vida: ${breed.life_span}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = "Historia: ${breed.history}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = "Origen: ${breed.origin}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}
