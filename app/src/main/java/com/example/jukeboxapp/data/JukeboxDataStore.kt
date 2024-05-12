package com.example.jukeboxapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map


class JukeboxDataStore(
    private val dataStore: DataStore<Preferences>
) {

    val storedJukeboxName: Flow<String?> = dataStore.data.map {
        it[JUKEBOX_NAME_KEY]
    }.distinctUntilChanged()

    suspend fun saveJukeboxName(name: String) {
        dataStore.edit { preferences ->
            preferences[JUKEBOX_NAME_KEY] = name
        }
    }

    companion object {
        private val JUKEBOX_NAME_KEY = stringPreferencesKey("jukebox_name")
    }
}