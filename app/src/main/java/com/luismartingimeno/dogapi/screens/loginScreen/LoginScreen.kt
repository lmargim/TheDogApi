package com.luismartingimeno.dogapi.screens.loginScreen

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.luismartingimeno.dogapi.R
import com.luismartingimeno.dogapi.data.AuthManager
import com.luismartingimeno.dogapi.ui.theme.Purple40
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    auth: AuthManager,
    navigateToHome: () -> Unit,
    navigateToSignUp: () -> Unit,
    navigateToForgotPassword: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var passwd by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val authState by auth.authState.collectAsState()
    val googleSignLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        CoroutineScope(Dispatchers.Main).launch {
            auth.handleGoogleSignInResult(task)
        }
    }


    LaunchedEffect(authState) {
        when (authState) {
            is AuthManager.AuthRes.Success<*> -> {
                Toast.makeText(context, "Inicio de sesion exitoso", Toast.LENGTH_SHORT).show()
                auth.resetAuthState()
                navigateToHome()
            }

            is AuthManager.AuthRes.Error -> {
                Toast.makeText(
                    context,
                    (authState as AuthManager.AuthRes.Error).errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is AuthManager.AuthRes.Idle -> {}
            else -> {}
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = stringResource(id = R.string.no_tienes_cuenta),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 40.dp)
                    .clickable { navigateToSignUp() },
                style = TextStyle(
                    color = Purple40,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Default,
                    textDecoration = TextDecoration.Underline
                )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 40.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dog),
                    contentDescription = "Logo Telares del sur",
                    modifier = Modifier.size(200.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(R.string.title_login),
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(modifier = Modifier.height(50.dp))

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.width(335.dp),
                    leadingIcon = {
                        Icon(Icons.Default.Mail, contentDescription = "Ícono de email")
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = passwd,
                    onValueChange = { passwd = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.width(335.dp),
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = "Ícono de password")
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    modifier = Modifier
                        .width(335.dp)
                        .height(50.dp),
                    onClick = {
                        scope.launch {
                            signIn(auth, email, passwd, context)
                        }
                    },
                ) {
                    if (auth.progressBar.observeAsState().value == true) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(30.dp),
                            strokeWidth = 3.dp
                        )
                    } else {
                        Text(stringResource(R.string.acceder))
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(id = R.string.olvidado_contrasena),
                    modifier = Modifier.clickable { navigateToForgotPassword() },
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Default,
                        textDecoration = TextDecoration.Underline,
                        color = Purple40
                    )
                )

                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    text = stringResource(id = R.string.divisor),
                    style = TextStyle(color = Color.Gray)
                )

                Spacer(modifier = Modifier.height(25.dp))


                BotonesGoogle(
                    onClick = {
                        scope.launch {
                            signAnonimously(auth)
                        }
                    },
                    text = "Continuar como invitado",
                    icon = R.drawable.ic_incognito,
                    color = Color(0xFF363636),
                    cargando = auth.progressBarAnonimous.observeAsState().value ?: false
                )

                Spacer(modifier = Modifier.height(15.dp))

                BotonesGoogle(
                    onClick = {
                        googleSignLauncher.launch(auth.getGoogleSignInIntent())
                    },
                    text = "Continuar con Google",
                    icon = R.drawable.ic_google,
                    color = Color(0xFFF1F1F1),
                    cargando = auth.progressBarGoogle.observeAsState().value ?: false
                )

            }
        }
    }
}

@Composable
fun BotonesGoogle(
    onClick: () -> Unit,
    text: String,
    icon: Int,
    color: Color,
    cargando: Boolean
) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .width(335.dp)
            .height(50.dp),
        border = BorderStroke(
            width = 2.dp,
            color = if (icon == R.drawable.ic_incognito) color else Color.Gray
        ),
        colors = ButtonColors(
            containerColor = color,
            contentColor = if (icon == R.drawable.ic_incognito) Color.White else Color.Black,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.Gray,
        )
    ) {
        if (cargando) {
            if (icon == R.drawable.ic_incognito) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(30.dp),
                    strokeWidth = 3.dp
                )
            } else {
                CircularProgressIndicator(
                    color = Color.Black,
                    modifier = Modifier.size(30.dp),
                    strokeWidth = 3.dp
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .padding(start = 6.dp, end = 8.dp, top = 6.dp, bottom = 6.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    modifier = Modifier.size(24.dp),
                    contentDescription = text,
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = text,
                    color = if (icon == R.drawable.ic_incognito) Color.White else Color.Black
                )
            }
        }
    }
}

suspend fun signIn(auth: AuthManager, email: String, passwd: String, context: Context) {
    if (email.isNotEmpty() && passwd.isNotEmpty()) {
        auth.signInWithEmailAndPassword(email, passwd)
    } else {
        Toast.makeText(context, "Complete los campos", Toast.LENGTH_SHORT).show()
    }
}

suspend fun signAnonimously(auth: AuthManager) {
    // Cerramos la sesion antes de iniciar como anonimos
//     Para evitar posibles intervenciones de la cache del sistema
    auth.signOut()
    auth.signAnonimously()
}