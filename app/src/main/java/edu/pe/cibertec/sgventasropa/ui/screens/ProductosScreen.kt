package edu.pe.cibertec.sgventasropa.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import edu.pe.cibertec.sgventasropa.data.model.Producto
import edu.pe.cibertec.sgventasropa.data.model.ItemVenta
import edu.pe.cibertec.sgventasropa.data.repository.ProductoRepository
import edu.pe.cibertec.sgventasropa.ui.viewmodel.CarritoViewModel
import edu.pe.cibertec.sgventasropa.ui.viewmodel.ProductoViewModel
import kotlinx.coroutines.launch
import edu.pe.cibertec.sgventasropa.Screen

@Composable
fun ProductosScreen(
    navController: NavController, // 👈 agrégalo aquí
    productoVM: ProductoViewModel = viewModel(),
    carritoVM: CarritoViewModel,
    isAdmin: Boolean = false,
    modifier: Modifier = Modifier
)

 {
    val productos by productoVM.productos.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var editarProducto by remember { mutableStateOf<Producto?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        productoVM.cargar()
    }

    Column(modifier = modifier.fillMaxSize().padding(8.dp)) {

        // Botón Agregar solo para admin
        if (isAdmin) {
            Button(
                onClick = {
                    editarProducto = null
                    showDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar Producto")
            }
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            // Botón "Ver Carrito" solo para clientes
            Button(
                onClick = { navController?.navigate(Screen.Carrito.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("🛒 Ver Carrito")
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        LazyColumn {
            items(productos) { producto ->
                val cantidadState = remember(producto.id) { mutableStateOf("1") }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                        Text("Precio: S/. ${producto.precio}")
                        Text("Stock: ${producto.stock}")

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (!isAdmin) {
                                OutlinedTextField(
                                    value = cantidadState.value,
                                    onValueChange = { new ->
                                        val filtered = new.filter { it.isDigit() }
                                        cantidadState.value = if (filtered.isEmpty()) "0" else filtered
                                    },
                                    label = { Text("Cantidad") },
                                    singleLine = true,
                                    modifier = Modifier.width(110.dp)
                                )
                            }

                            if (!isAdmin) {
                                Button(onClick = {
                                    scope.launch {
                                        val cantidad = cantidadState.value.toIntOrNull() ?: 0
                                        if (cantidad <= 0) {
                                            println("Ingrese cantidad válida")
                                            return@launch
                                        }

                                        val p = productoVM.obtenerPorId(producto.id)


                                        if (p == null) {
                                            println("Producto no existe")
                                            return@launch
                                        }
                                        if (p.stock < cantidad) {
                                            println("Stock insuficiente (stock=${p.stock})")
                                            return@launch
                                        }

                                        val item = ItemVenta(
                                            productoId = producto.id,
                                            nombre = producto.nombre,
                                            cantidad = cantidad,
                                            subtotal = producto.precio * cantidad
                                        )
                                        carritoVM.agregar(item)
                                        println("Agregado al carrito")
                                    }
                                }) {
                                    Text("Agregar al carrito")
                                }
                            }

                            if (isAdmin) {
                                Row {
                                    Button(onClick = {
                                        editarProducto = producto
                                        showDialog = true
                                    }) {
                                        Text("Editar")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(onClick = {
                                        productoVM.eliminar(producto.id) {
                                            println("Producto eliminado")
                                        }
                                    }) {
                                        Text("Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        val isEditing = editarProducto != null
        var nombre by remember { mutableStateOf(editarProducto?.nombre ?: "") }
        var descripcion by remember { mutableStateOf(editarProducto?.descripcion ?: "") }
        var precioText by remember { mutableStateOf(editarProducto?.precio?.toString() ?: "") }
        var stockText by remember { mutableStateOf(editarProducto?.stock?.toString() ?: "") }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = {
                    val precio = precioText.toDoubleOrNull() ?: 0.0
                    val stock = stockText.toIntOrNull() ?: 0
                    if (isEditing) {
                        val datos = mapOf<String, Any>(
                            "nombre" to nombre,
                            "descripcion" to descripcion,
                            "precio" to precio,
                            "stock" to stock
                        )
                        productoVM.actualizar(editarProducto!!.id, datos) {
                            showDialog = false
                        }
                    } else {
                        val nuevo = Producto(
                            id = "",
                            nombre = nombre,
                            descripcion = descripcion,
                            precio = precio,
                            stock = stock,
                            imagenUrl = null
                        )
                        productoVM.agregar(nuevo) {
                            showDialog = false
                        }
                    }
                }) { Text(if (isEditing) "Guardar" else "Agregar") }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) { Text("Cancelar") }
            },
            text = {
                Column {
                    OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                    OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
                    OutlinedTextField(value = precioText, onValueChange = { precioText = it }, label = { Text("Precio") })
                    OutlinedTextField(value = stockText, onValueChange = { stockText = it }, label = { Text("Stock") })
                }
            }
        )
    }
}
