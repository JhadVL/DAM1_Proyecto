package edu.pe.cibertec.sgventasropa.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ItemVenta(
    val productoId: String = "",
    val nombre: String = "",
    val cantidad: Int = 0,
    val subtotal: Double = 0.0
)
