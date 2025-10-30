package edu.pe.cibertec.sgventasropa

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.pe.cibertec.sgventasropa.ui.screens.*
import edu.pe.cibertec.sgventasropa.ui.viewmodel.CarritoViewModel

@Composable
fun SGVentasRopaApp() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // 🔹 Crear UNA SOLA instancia del CarritoViewModel (compartido)
    val carritoVM: CarritoViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.Inicio.route) {

        composable(Screen.Inicio.route) {
            InicioScreen(
                onAdminSelected = { navController.navigate(Screen.LoginAdmin.route) },
                onClienteSelected = { navController.navigate(Screen.Productos.route) }
            )
        }

        composable(Screen.LoginAdmin.route) {
            LoginAdminScreen(
                onLoginSuccess = { navController.navigate(Screen.Admin.route) },
                onRegister = { navController.navigate(Screen.RegistroAdmin.route) }
            )
        }

        composable(Screen.RegistroAdmin.route) {
            RegistroAdminScreen(onCreated = { navController.popBackStack() })
        }

        composable(Screen.LoginCliente.route) {
            LoginClienteScreen(
                onLoginSuccess = { navController.navigate(Screen.Productos.route) },
                onRegister = { navController.navigate(Screen.RegistroCliente.route) }
            )
        }

        composable(Screen.RegistroCliente.route) {
            RegistroClienteScreen(onRegistered = { navController.navigate(Screen.Productos.route) })
        }

        // 🔹 Pasamos carritoVM al ProductosScreen
        composable(Screen.Productos.route) {
            ProductosScreen(
                navController = navController,
                carritoVM = carritoVM,
                isAdmin = false
            )
        }

        // 🔹 Productos para administrador
        composable("productos_admin") {
            ProductosScreen(
                navController = navController,
                carritoVM = carritoVM,
                isAdmin = true
            )
        }

        composable(Screen.Admin.route) {
            AdminScreen(navController = navController, context = context)
        }

        // 🔹 Pasamos carritoVM al CarritoScreen
        composable(Screen.Carrito.route) {
            CarritoScreen(carritoVM = carritoVM)
        }
    }
}
