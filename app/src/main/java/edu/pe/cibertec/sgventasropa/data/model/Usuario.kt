package edu.pe.cibertec.sgventasropa.data.model

data class Usuario(
    val uid: String = "",
    val nombre: String = "",
    val email: String = "",
    val rol: String = "cliente", // "admin" o "cliente"
    val fechaRegistroMillis: Long = System.currentTimeMillis()
)
