package edu.pe.cibertec.sgventasropa.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.pe.cibertec.sgventasropa.ui.viewmodel.CarritoViewModel
import edu.pe.cibertec.sgventasropa.ui.viewmodel.VentaViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.pe.cibertec.sgventasropa.data.model.ItemVenta
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    carritoVM: CarritoViewModel = viewModel(),
    ventaVM: VentaViewModel = viewModel()
) {
    val context = LocalContext.current
    val items by carritoVM.items.collectAsState()
    val isProcessing by ventaVM.isProcessing.collectAsState()
    val auth = Firebase.auth
    val usuarioUid = auth.currentUser?.uid

    Scaffold(topBar = { TopAppBar(title = { Text("Carrito") }) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            if (items.isEmpty()) {
                Text("No hay productos en el carrito")
            } else {
                items.forEach { item ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${item.nombre} x ${item.cantidad}")
                        Text("S/. ${item.subtotal}")
                    }
                    Divider()
                }

                Spacer(modifier = Modifier.height(12.dp))
                val total = items.sumOf { it.subtotal }
                Text("Total: S/. $total")

                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = {
                    if (usuarioUid == null) {
                        Toast.makeText(context, "Debe registrarse / iniciar sesión para completar la compra", Toast.LENGTH_LONG).show()
                    } else {
                        ventaVM.realizarVenta(usuarioUid, items, total,
                            onSuccess = { ventaId ->
                                carritoVM.limpiar(context)
                                Toast.makeText(context, "Compra realizada (ID: $ventaId)", Toast.LENGTH_LONG).show()
                            },
                            onError = { msg ->
                                Toast.makeText(context, "Error: $msg", Toast.LENGTH_LONG).show()
                            })
                    }
                }, enabled = !isProcessing) {
                    Text(if (isProcessing) "Procesando..." else "Confirmar compra")
                }
            }
        }
    }
}
