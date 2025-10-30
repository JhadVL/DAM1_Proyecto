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
import edu.pe.cibertec.sgventasropa.data.repository.UsuarioRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginAdminScreen(onLoginSuccess: () -> Unit, onRegister: () -> Unit) {
    val context = LocalContext.current
    val auth = Firebase.auth
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Scaffold(topBar = { TopAppBar(title = { Text("Login - Administrador") }) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { result ->
                        scope.launch {
                            val uid = result.user?.uid ?: ""
                            val repo = UsuarioRepository()
                            try {
                                val usuario = repo.obtenerUsuario(uid)
                                if (usuario?.rol == "admin") {
                                    onLoginSuccess()
                                } else {
                                    Toast.makeText(context, "No tiene permisos de administrador", Toast.LENGTH_LONG).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    .addOnFailureListener { ex ->
                        Toast.makeText(context, "Error login: ${ex.message}", Toast.LENGTH_LONG).show()
                    }
            }, modifier = Modifier.fillMaxWidth()) { Text("Ingresar como Admin") }

            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onRegister) { Text("Crear cuenta de administrador") }
        }
    }
}
