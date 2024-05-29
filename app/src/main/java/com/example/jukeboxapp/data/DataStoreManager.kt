package com.example.jukeboxapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.File


object DataStoreManager {
    private var dataStore: JukeboxDataStore? = null

    fun initialize(context: Context) {
        if (dataStore == null) {
            dataStore = JukeboxDataStore(context)
        }
    }

    fun getDataStore(): JukeboxDataStore {
        return dataStore ?: throw IllegalStateException("DataStore not initialized")
    }
    /*
    private fun Context.preferencesDataStoreFile(name: String): File {
        return File(filesDir, "datastore/$name.preferences_pb")
    }
     */
}