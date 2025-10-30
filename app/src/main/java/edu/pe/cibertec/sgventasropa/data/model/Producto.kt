package edu.pe.cibertec.sgventasropa.data.model

data class Producto(
    val id: String = "",
    val nombre: String = "",
    val descripcion: String? = null,
    val precio: Double = 0.0,
    val stock: Int = 0,
    val imagenUrl: String? = null
)
