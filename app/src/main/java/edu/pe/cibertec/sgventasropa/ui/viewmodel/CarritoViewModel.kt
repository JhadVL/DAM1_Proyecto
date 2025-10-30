package edu.pe.cibertec.sgventasropa.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pe.cibertec.sgventasropa.data.model.ItemVenta
import edu.pe.cibertec.sgventasropa.data.repository.ProductoRepository
import edu.pe.cibertec.sgventasropa.utils.CartDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class CarritoViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<ItemVenta>>(emptyList())
    val items: StateFlow<List<ItemVenta>> = _items.asStateFlow()

    fun agregar(item: ItemVenta, context: Context? = null) {
        _items.value = _items.value + item
        persist(context)
    }

    fun quitar(productoId: String, context: Context? = null) {
        _items.value = _items.value.filter { it.productoId != productoId }
        persist(context)
    }

    fun limpiar(context: Context? = null) {
        _items.value = emptyList()
        persist(context)
    }

    fun cargarDesdeLocal(context: Context) {
        viewModelScope.launch {
            val json = CartDataStore.readCart(context)
            if (!json.isNullOrBlank()) {
                try {
                    val lista = Json.decodeFromString<List<ItemVenta>>(json)
                    _items.value = lista
                } catch (_: Exception) {}
            }
        }
    }

    private fun persist(context: Context?) {
        context?.let { ctx ->
            viewModelScope.launch {
                val json = Json.encodeToString(_items.value)
                CartDataStore.saveCart(ctx, json)
            }
        }
    }
    fun agregarConVerificacion(
        item: ItemVenta,
        onResult: (success: Boolean, message: String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val repo = ProductoRepository()
                val producto = repo.obtenerPorId(item.productoId)
                if (producto == null) {
                    onResult(false, "Producto no existe")
                    return@launch
                }
                if (producto.stock < item.cantidad) {
                    onResult(false, "Stock insuficiente (${producto.stock})")
                    return@launch
                }
                agregar(item)
                onResult(true, "Agregado al carrito")
            } catch (e: Exception) {
                onResult(false, e.message ?: "Error")
            }
        }
    }



}
