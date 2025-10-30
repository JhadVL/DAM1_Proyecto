package edu.pe.cibertec.sgventasropa.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.provider.MediaStore
import androidx.core.net.toFile
import edu.pe.cibertec.sgventasropa.data.model.Producto
import edu.pe.cibertec.sgventasropa.data.model.Venta
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

import java.io.File
import java.io.FileOutputStream

object ReportUtils {

    suspend fun exportarProductosCsv(context: Context, productos: List<Producto>) {
        val headers = listOf("ID", "Nombre", "Precio", "Stock")
        val rows = productos.map { listOf(it.id, it.nombre, it.precio.toString(), it.stock.toString()) }
        saveCsv(context, "reporte_productos.csv", headers, rows)
    }

    suspend fun exportarVentasPdf(context: Context, ventas: List<Venta>) {
        val title = "Reporte de Ventas"
        val headers = listOf("ID", "Cliente", "Total", "Fecha")
        val rows = ventas.map {
            listOf(it.id, it.clienteId ?: "N/A", it.total.toString(), "Sin fecha")
        }
        savePdf(context, title, headers, rows)
    }

    private fun saveCsv(context: Context, fileName: String, headers: List<String>, rows: List<List<String>>) {
        val csvContent = buildString {
            append(headers.joinToString(","))
            append("\n")
            rows.forEach { row ->
                append(row.joinToString(","))
                append("\n")
            }
        }

        val file = File(context.getExternalFilesDir(null), fileName)
        FileOutputStream(file).use { it.write(csvContent.toByteArray()) }
        println("✅ CSV guardado en: ${file.absolutePath}")
    }

    private fun savePdf(context: Context, title: String, headers: List<String>, rows: List<List<String>>) {
        // Solo genera un PDF simulado (texto plano)
        val content = buildString {
            append("$title\n\n")
            append(headers.joinToString(" | "))
            append("\n-----------------------------------\n")
            rows.forEach { row ->
                append(row.joinToString(" | "))
                append("\n")
            }
        }

        val file = File(context.getExternalFilesDir(null), "reporte_ventas.pdf")
        FileOutputStream(file).use { it.write(content.toByteArray()) }
        println("✅ PDF guardado en: ${file.absolutePath}")
    }
}

