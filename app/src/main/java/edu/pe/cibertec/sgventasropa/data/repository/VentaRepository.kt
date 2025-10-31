package edu.pe.cibertec.sgventasropa.data.repository

import edu.pe.cibertec.sgventasropa.data.model.ItemVenta
import edu.pe.cibertec.sgventasropa.data.model.Venta
import kotlinx.coroutines.tasks.await

class VentaRepository {
    private val productosRef = FirebaseDB.db.collection("productos")
    private val ventasRef = FirebaseDB.db.collection("ventas")

    suspend fun realizarVenta(clienteId: String, items: List<ItemVenta>, total: Double): String {
        //Obtener todos los productos existentes
        val productosSnapshot = productosRef.get().await()
        val productosMap = productosSnapshot.documents.associateBy { it.id }

        //Validar stock de cada producto
        for (item in items) {
            val prodDoc = productosMap[item.productoId]
                ?: throw Exception("Producto no existe: ${item.nombre}")

            val stockActual = prodDoc.getLong("stock")?.toInt() ?: 0
            if (stockActual < item.cantidad) {
                throw Exception("Stock insuficiente para ${item.nombre}")
            }
        }

        //Actualizar el stock de los productos vendidos
        for (item in items) {
            val prodDoc = productosMap[item.productoId]!!
            val stockActual = prodDoc.getLong("stock")?.toInt() ?: 0
            val nuevoStock = stockActual - item.cantidad
            productosRef.document(item.productoId).update("stock", nuevoStock).await()
        }

        //Registrar la venta en Firestore
        val ventaDoc = ventasRef.document()
        val venta = Venta(
            id = ventaDoc.id,
            clienteId = clienteId,
            fechaMillis = System.currentTimeMillis(),
            total = total,
            items = items
        )

        ventaDoc.set(venta).await()

        return ventaDoc.id
    }

    suspend fun obtenerVentas(): List<Venta> {
        val snap = ventasRef.get().await()
        return snap.documents.mapNotNull { it.toObject(Venta::class.java)?.copy(id = it.id) }
    }
}
