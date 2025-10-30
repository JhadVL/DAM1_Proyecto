package edu.pe.cibertec.sgventasropa.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pe.cibertec.sgventasropa.data.model.ItemVenta
import edu.pe.cibertec.sgventasropa.data.repository.VentaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VentaViewModel : ViewModel() {
    private val repo = VentaRepository()
    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    fun realizarVenta(clienteId: String, items: List<ItemVenta>, total: Double, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _isProcessing.value = true
            try {
                val id = repo.realizarVenta(clienteId, items, total)
                onSuccess(id)
            } catch (e: Exception) {
                onError(e.message ?: "Error al realizar venta")
            } finally {
                _isProcessing.value = false
            }
        }
    }
}
