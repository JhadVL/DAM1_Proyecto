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
import edu.pe.cibertec.sgventasropa.data.model.Usuario
import edu.pe.cibertec.sgventasropa.data.repository.UsuarioRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroAdminScreen(onCreated: () -> Unit) {
    val context = LocalContext.current
    val auth = Firebase.auth
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Scaffold(topBar = { TopAppBar(title = { Text("Registro - Administrador") }) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { authResult ->
                        scope.launch {
                            val uid = authResult.user?.uid ?: ""
                            val usuario = Usuario(uid = uid, nombre = nombre, email = email, rol = "admin")
                            val repo = UsuarioRepository()
                            repo.crearUsuario(usuario)
                            Toast.makeText(context, "Administrador creado", Toast.LENGTH_LONG).show()
                            onCreated()
                        }
                    }
                    .addOnFailureListener { ex -> Toast.makeText(context, "Error: ${ex.message}", Toast.LENGTH_LONG).show() }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Crear Administrador")
            }
        }
    }
}
