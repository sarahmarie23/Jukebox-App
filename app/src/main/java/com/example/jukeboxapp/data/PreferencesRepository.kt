package com.example.jukeboxapp.data

import androidx.datastore.core.DataStore
import java.util.prefs.Preferences

class PreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
}