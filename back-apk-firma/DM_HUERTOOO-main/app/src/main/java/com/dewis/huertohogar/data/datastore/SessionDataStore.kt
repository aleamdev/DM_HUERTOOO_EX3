package com.dewis.dm_huertohogar_ex3.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "session")

object Keys {
    val EMAIL = stringPreferencesKey("email")
    val USERS = stringPreferencesKey("users")
    val CART  = stringPreferencesKey("cart")
    val TOKEN = stringPreferencesKey("token")   // ⬅️ JWT
}

class SessionDataStore(private val context: Context) {

    val email: Flow<String?> = context.dataStore.data.map { it[Keys.EMAIL] }
    val usersRaw: Flow<String?> = context.dataStore.data.map { it[Keys.USERS] }
    val token: Flow<String?> = context.dataStore.data.map { it[Keys.TOKEN] }

    suspend fun setEmail(v: String?) = context.dataStore.edit {
        if (v == null) it.remove(Keys.EMAIL) else it[Keys.EMAIL] = v
    }

    suspend fun setToken(v: String?) = context.dataStore.edit {
        if (v == null) it.remove(Keys.TOKEN) else it[Keys.TOKEN] = v
    }

    suspend fun appendUser(email: String, pass: String) = context.dataStore.edit { prefs ->
        val cur = prefs[Keys.USERS].orEmpty()
        if (!cur.split(";").any { it.startsWith("$email|") }) {
            prefs[Keys.USERS] = if (cur.isBlank()) "$email|$pass" else "$cur;$email|$pass"
        }
    }

    suspend fun userExists(email: String, pass: String): Boolean {
        val cur = context.dataStore.data.map { it[Keys.USERS].orEmpty() }
        var ok = false
        cur.collect { raw ->
            ok = raw.split(";").any { it == "$email|$pass" }
        }
        return ok
    }
}
