package edu.pe.cibertec.sgventasropa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import edu.pe.cibertec.sgventasropa.ui.theme.DAM1_ProyectoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DAM1_ProyectoTheme {
                SGVentasRopaApp()
            }
        }
    }
}
