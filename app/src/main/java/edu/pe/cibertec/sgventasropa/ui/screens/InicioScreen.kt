package edu.pe.cibertec.sgventasropa.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InicioScreen(onAdminSelected: () -> Unit, onClienteSelected: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Bienvenido a SGVentasRopa")
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onAdminSelected, modifier = Modifier.fillMaxWidth().height(48.dp)) { Text("Administrador") }
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onClienteSelected, modifier = Modifier.fillMaxWidth().height(48.dp)) { Text("Cliente") }
        }
    }
}
