package edu.pe.cibertec.sgventasropa.data.model

data class Venta(
    val id: String = "",
    val clienteId: String = "",
    val fechaMillis: Long = 0L,
    val total: Double = 0.0,
    val items: List<ItemVenta> = emptyList()
)
