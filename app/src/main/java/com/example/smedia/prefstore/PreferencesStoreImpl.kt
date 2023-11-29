package com.example.smedia.prefstore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val STORE_NAME = "learning_data_store"

class PreferencesStoreImpl (private val context: Context) {

    companion object {
        private val Context.dataStore:
                DataStore<Preferences> by preferencesDataStore(name = STORE_NAME)
        val USER_EMAIL = stringPreferencesKey("user_email")
    }

    val getUser : Flow<String> = context.dataStore.data
        .map { preferences -> preferences[USER_EMAIL] ?: ""}

    suspend fun saveUser(email: String) {
        context.dataStore.edit { preferences -> preferences[USER_EMAIL] = email }
    }
}