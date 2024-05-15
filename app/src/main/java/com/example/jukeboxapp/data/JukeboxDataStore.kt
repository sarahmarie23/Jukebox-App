package com.example.jukeboxapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.jukeboxapp.model.JukeboxState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map



class JukeboxDataStore(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "jukebox_settings")
    private val dataStore: DataStore<Preferences> by lazy { context.dataStore }

    companion object {
        val IS_BLUETOOTH_ENABLED_KEY = stringPreferencesKey("is_bluetooth_enabled")
        val LAST_SONG_SELECTION_KEY = stringPreferencesKey("last_song_selection")
        val JUKEBOX_NAME_KEY = stringPreferencesKey("jukebox_name")
        val IS_CD_MACHINE_KEY = stringPreferencesKey("is_cd_machine")
    }

    val preferenecsFlow: Flow<Preferences> = dataStore.data

    fun getDataStore(): DataStore<Preferences> {
        return dataStore
    }

    val storedJukeboxName: Flow<String?> = dataStore.data.map { preferences->
        preferences[JUKEBOX_NAME_KEY]
    }.distinctUntilChanged()

    suspend fun saveJukeboxName(name: String) {
        dataStore.edit { preferences ->
            preferences[JUKEBOX_NAME_KEY] = name
        }
    }

    suspend fun readJukeboxState(): JukeboxState {
        return dataStore.data.map { preferences ->
            val isBluetoothEnabled = preferences[IS_BLUETOOTH_ENABLED_KEY]?.toBoolean() ?: false
            val lastSongSelection = preferences[LAST_SONG_SELECTION_KEY] ?: ""
            val machineName = preferences[JUKEBOX_NAME_KEY] ?: ""
            val isCdMachine = preferences[IS_CD_MACHINE_KEY]?.toBoolean() ?: false
            JukeboxState(isBluetoothEnabled, lastSongSelection, machineName, isCdMachine)
        }.first()
    }

    suspend fun writeJukeboxState(state: JukeboxState) {
        dataStore.edit { preferences ->
            preferences[IS_BLUETOOTH_ENABLED_KEY] = state.isBluetoothEnabled.toString()
            preferences[LAST_SONG_SELECTION_KEY] = state.lastSongSelection
            preferences[JUKEBOX_NAME_KEY] = state.machineName
            preferences[IS_CD_MACHINE_KEY] = state.isCdMachine.toString()
        }
    }



}