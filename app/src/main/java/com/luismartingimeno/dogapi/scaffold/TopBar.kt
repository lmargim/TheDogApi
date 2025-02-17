package com.luismartingimeno.dogapi.scaffold

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luismartingimeno.dogapi.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    titulo: String,
    navigateToLogin: () -> Unit,
    onShowFavorites: () -> Unit,
    onLogout: () -> Unit,
    showFavoritesAndLogout: Boolean = true // Nuevo parámetro para controlar la visibilidad
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dog),
                    contentDescription = "LogoApi",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.weight(1f)
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = navigateToLogin) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        actions = {
            // Mostrar los iconos de favoritos y logout solo si 'showFavoritesAndLogout' es true
            if (showFavoritesAndLogout) {
                IconButton(onClick = onShowFavorites) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Favorites",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Yellow
                    )
                }
                IconButton(onClick = onLogout) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                }
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

