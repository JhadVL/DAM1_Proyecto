package edu.pe.cibertec.sgventasropa.data.repository

import com.google.firebase.firestore.ktx.toObject
import edu.pe.cibertec.sgventasropa.data.model.Usuario
import kotlinx.coroutines.tasks.await
import kotlin.collections.mapNotNull

class UsuarioRepository {
    private val ref = FirebaseDB.db.collection("usuarios")

    suspend fun crearUsuario(usuario: Usuario) {
        ref.document(usuario.uid).set(usuario).await()
    }

    suspend fun obtenerUsuario(uid: String): Usuario? {
        val doc = ref.document(uid).get().await()
        return if (doc.exists()) doc.toObject<Usuario>() else null
    }

    suspend fun obtenerTodos(): List<Usuario> {
        val snap = ref.get().await()
        return snap.documents.mapNotNull { it.toObject<Usuario>() }
    }

    suspend fun actualizar(uid: String, datos: Map<String, Any>) {
        ref.document(uid).update(datos).await()
    }

    suspend fun eliminar(uid: String) {
        ref.document(uid).delete().await()
    }
}
