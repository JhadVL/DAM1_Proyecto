package edu.pe.cibertec.sgventasropa.data.repository

import com.google.firebase.firestore.ktx.toObject
import edu.pe.cibertec.sgventasropa.data.model.Producto
import kotlinx.coroutines.tasks.await
import kotlin.collections.mapNotNull

class ProductoRepository {
    private val ref = FirebaseDB.db.collection("productos")

    suspend fun obtenerTodos(): List<Producto> {
        val snap = ref.get().await()
        return snap.documents.mapNotNull { it.toObject<Producto>()?.copy(id = it.id) }
    }

    suspend fun agregar(producto: Producto) {
        ref.add(producto).await()
    }

    suspend fun actualizar(id: String, datos: Map<String, Any>) {
        ref.document(id).update(datos).await()
    }

    suspend fun eliminar(id: String) {
        ref.document(id).delete().await()
    }

    suspend fun obtenerPorId(id: String): Producto? {
        val doc = ref.document(id).get().await()
        return if (doc.exists()) doc.toObject<Producto>() else null
    }
}
