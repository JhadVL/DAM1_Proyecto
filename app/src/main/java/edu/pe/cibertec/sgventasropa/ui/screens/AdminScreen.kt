package edu.pe.cibertec.sgventasropa.ui.screens
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.pe.cibertec.sgventasropa.data.repository.ProductoRepository
import edu.pe.cibertec.sgventasropa.data.repository.VentaRepository
import edu.pe.cibertec.sgventasropa.utils.ReportUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

@Composable
fun AdminScreen(navController: NavController, context: Context) {
    val scope = CoroutineScope(Dispatchers.IO)

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Panel del Administrador", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("productos_admin") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gestionar Productos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    val repo = ProductoRepository()
                    val productos = repo.obtenerTodos()
                    ReportUtils.exportarProductosCsv(context, productos)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Exportar Productos (CSV)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    val repo = VentaRepository()
                    val ventas = repo.obtenerVentas()
                    ReportUtils.exportarVentasPdf(context, ventas)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Exportar Ventas (PDF)")
        }
    }
}
