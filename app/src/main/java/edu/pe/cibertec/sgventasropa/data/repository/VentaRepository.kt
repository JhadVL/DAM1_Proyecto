package edu.pe.cibertec.sgventasropa.data.repository

import edu.pe.cibertec.sgventasropa.data.model.ItemVenta
import edu.pe.cibertec.sgventasropa.data.model.Venta
import kotlinx.coroutines.tasks.await
import kotlin.collections.mapNotNull
import kotlin.jvm.java

class VentaRepository {
    private val productosRef = FirebaseDB.db.collection("productos")
    private val ventasRef = FirebaseDB.db.collection("ventas")

    /**
     * Ejecuta una transacción atómica:
     * - verifica stock
     * - descuenta stock
     * - crea la venta
     * Lanza excepción en caso de stock insuficiente u otro error.
     */
    suspend fun realizarVenta(clienteId: String, items: List<ItemVenta>, total: Double): String {
        val db = FirebaseDB.db
        val result = db.runTransaction { tx ->
            // verificar stock
            for (item in items) {
                val prodRef = productosRef.document(item.productoId)
                val prodSnap = tx.get(prodRef)
                if (!prodSnap.exists()) throw kotlin.Exception("Producto no existe: ${item.nombre}")
                val stock = prodSnap.getLong("stock")?.toInt() ?: 0
                if (stock < item.cantidad) throw kotlin.Exception("Stock insuficiente para ${item.nombre}")
                tx.update(prodRef, "stock", stock - item.cantidad)
            }
            // crear venta
            val ventaDoc = ventasRef.document()
            val venta = Venta(
                id = ventaDoc.id,
                clienteId = clienteId,
                fechaMillis = System.currentTimeMillis(),
                total = total,
                items = items
            )
            tx.set(ventaDoc, venta)
            ventaDoc.id
        }.await()
        return result
    }

    suspend fun obtenerVentas(): List<Venta> {
        val snap = ventasRef.get().await()
        return snap.documents.mapNotNull { it.toObject(Venta::class.java)?.copy(id = it.id) }
    }
}
