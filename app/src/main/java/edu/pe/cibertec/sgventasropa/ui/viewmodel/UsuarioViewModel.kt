package edu.pe.cibertec.sgventasropa.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import edu.pe.cibertec.sgventasropa.data.model.Usuario

class UsuarioViewModel : ViewModel() {
    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuario: StateFlow<Usuario?> = _usuario.asStateFlow()

    fun setUsuario(value: Usuario?) {
        _usuario.value = value
    }
}
