package com.luismartingimeno.dogapi.screens.homeScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.luismartingimeno.dogapi.Screens.HomeScreen.BreedsViewModel
import com.luismartingimeno.dogapi.data.AuthManager
import com.luismartingimeno.dogapi.data.firebase.FirestoreManager
import com.luismartingimeno.dogapi.data.model.DogBreedItem
import com.luismartingimeno.dogapi.scaffold.TopBar

@Composable
fun HomeScreen(
    auth: AuthManager,
    navigateToLogin: () -> Unit,
    navigateToBreedDetail: (DogBreedItem) -> Unit,
    navigateToFavorites: () -> Unit,
    viewModel: BreedsViewModel = viewModel(),
    firestoreManager: FirestoreManager
) {
    val lista by viewModel.breeds.observeAsState(emptyList())
    val progressBar by viewModel.progressBar.observeAsState(false)

    var favoriteBreeds by remember { mutableStateOf<List<DogBreedItem>>(emptyList()) }

    val logout = {
        auth.signOut()
        navigateToLogin()
    }

    val showFavorites = {
        navigateToFavorites()
    }

    Scaffold(
        topBar = {
            TopBar(
                titulo = "TheDogApi",
                navigateToLogin = navigateToLogin,
                onShowFavorites = showFavorites,
                onLogout = logout
            )
        }
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (progressBar) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val breedsToDisplay = if (favoriteBreeds.isEmpty()) lista else favoriteBreeds
                    items(breedsToDisplay) { breed ->
                        DogBreedCard(breed = breed, onClick = { navigateToBreedDetail(breed) })
                    }
                }
            }
        }
    }
}

@Composable
fun DogBreedCard(breed: DogBreedItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter("https://cdn2.thedogapi.com/images/${breed.reference_image_id}.jpg"),
                contentDescription = "Imagen de ${breed.name}",
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
            )
            Text(
                text = breed.name,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
