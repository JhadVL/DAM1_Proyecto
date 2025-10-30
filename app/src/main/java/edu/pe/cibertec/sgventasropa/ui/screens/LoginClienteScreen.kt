package edu.pe.cibertec.sgventasropa.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginClienteScreen(onLoginSuccess: () -> Unit, onRegister: () -> Unit) {
    val context = LocalContext.current
    val auth = Firebase.auth
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(topBar = { TopAppBar(title = { Text("Login - Cliente") }) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { _ -> onLoginSuccess() }
                    .addOnFailureListener { ex -> Toast.makeText(context, "Error login: ${ex.message}", Toast.LENGTH_LONG).show() }
            }, modifier = Modifier.fillMaxWidth()) { Text("Ingresar") }

            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onRegister) { Text("Registrarme como cliente") }
        }
    }
}
