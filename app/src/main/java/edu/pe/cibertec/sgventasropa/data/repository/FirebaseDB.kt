package edu.pe.cibertec.sgventasropa.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseDB {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
}
