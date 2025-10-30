package edu.pe.cibertec.sgventasropa

sealed class Screen(val route: String) {
    object Inicio : Screen("inicio")
    object LoginAdmin : Screen("login_admin")
    object RegistroAdmin : Screen("registro_admin")
    object LoginCliente : Screen("login_cliente")
    object RegistroCliente : Screen("registro_cliente")
    object Productos : Screen("productos")
    object Admin : Screen("admin")
    object Carrito : Screen("carrito")
}
