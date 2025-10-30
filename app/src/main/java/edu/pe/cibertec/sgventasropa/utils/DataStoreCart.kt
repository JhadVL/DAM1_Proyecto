package edu.pe.cibertec.sgventasropa.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore by preferencesDataStore(name = "sgventas_cart")

object CartDataStore {
    private val KEY_CART = stringPreferencesKey("cart_json")

    suspend fun saveCart(context: Context, cartJson: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_CART] = cartJson
        }
    }

    suspend fun readCart(context: Context): String? {
        val prefs = context.dataStore.data.map { it[KEY_CART] }.first()
        return prefs
    }
}
