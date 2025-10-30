package edu.pe.cibertec.sgventasropa.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pe.cibertec.sgventasropa.data.model.Producto
import edu.pe.cibertec.sgventasropa.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductoViewModel : ViewModel() {

    private val repo = ProductoRepository()

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _productos.value = repo.obtenerTodos()
            } catch (e: Exception) {
                _productos.value = emptyList()
                _mensaje.value = "Error al cargar productos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun agregar(producto: Producto, onDone: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                repo.agregar(producto)
                cargar()
                onDone?.invoke()
            } catch (e: Exception) {
                _mensaje.value = "Error al agregar producto: ${e.message}"
            }
        }
    }

    fun actualizar(id: String, datos: Map<String, Any>, onDone: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                repo.actualizar(id, datos)
                cargar()
                onDone?.invoke()
            } catch (e: Exception) {
                _mensaje.value = "Error al actualizar producto: ${e.message}"
            }
        }
    }

    fun eliminar(id: String, onDone: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                repo.eliminar(id)
                cargar()
                onDone?.invoke()
            } catch (e: Exception) {
                _mensaje.value = "Error al eliminar producto: ${e.message}"
            }
        }
    }

    suspend fun obtenerPorId(id: String): Producto? {
        return repo.obtenerPorId(id)
    }

}
