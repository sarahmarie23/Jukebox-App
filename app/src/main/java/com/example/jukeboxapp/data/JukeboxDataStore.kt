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
        val IS_CONNECTED_TO_MACHINE_KEY = stringPreferencesKey("is_connected_to_machine")
    }

    // Functions to update individual state values
    suspend fun updateBluetoothState(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_BLUETOOTH_ENABLED_KEY] = isEnabled.toString()
        }
    }

    suspend fun updateLastSongSelection(selection: String) {
        dataStore.edit { preferences ->
            preferences[LAST_SONG_SELECTION_KEY] = selection
        }
    }

    suspend fun updateJukeboxName(name: String) {
        dataStore.edit { preferences ->
            preferences[JUKEBOX_NAME_KEY] = name
        }
    }

    suspend fun updateMachineType(machineType: String) {
        dataStore.edit { preferences ->
            preferences[IS_CD_MACHINE_KEY] = machineType
        }
    }
    suspend fun updateIsConnectedState(isConnected: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_CONNECTED_TO_MACHINE_KEY] = isConnected.toString()
        }
    }

    // Generic function to update all state values
    private suspend fun updateState(state: JukeboxState) {
        dataStore.edit { preferences ->
            preferences[IS_BLUETOOTH_ENABLED_KEY] = state.isBluetoothConnected.toString()
            preferences[LAST_SONG_SELECTION_KEY] = state.lastSongSelection
            preferences[JUKEBOX_NAME_KEY] = state.machineName
            preferences[IS_CD_MACHINE_KEY] = state.machineType
            preferences[IS_CONNECTED_TO_MACHINE_KEY] = state.isConnectedToMachine.toString()
        }
    }
    suspend fun updateJukeboxState(newState: JukeboxState) {
        updateState(newState)
    }

    // Flow to observe state changes
    val jukeboxStateFlow: Flow<JukeboxState> = dataStore.data.map { preferences ->
        val isBluetoothEnabled = preferences[IS_BLUETOOTH_ENABLED_KEY]?.toBoolean() ?: false
        val lastSongSelection = preferences[LAST_SONG_SELECTION_KEY] ?: ""
        val machineName = preferences[JUKEBOX_NAME_KEY] ?: ""
        val machineType = preferences[IS_CD_MACHINE_KEY]?: ""
        JukeboxState(isBluetoothEnabled, lastSongSelection, machineName, machineType)
    }.distinctUntilChanged()

    // Function to read state
    suspend fun readJukeboxState(): JukeboxState {
        return dataStore.data.map { preferences ->
            val isBluetoothEnabled = preferences[IS_BLUETOOTH_ENABLED_KEY]?.toBoolean() ?: false
            val lastSongSelection = preferences[LAST_SONG_SELECTION_KEY] ?: ""
            val machineName = preferences[JUKEBOX_NAME_KEY] ?: ""
            val machineType = preferences[IS_CD_MACHINE_KEY]?: ""
            JukeboxState(isBluetoothEnabled, lastSongSelection, machineName, machineType)
        }.first()
    }

    // Probably won't need but keeping it just in case
    suspend fun updateBluetoothStateFlow(isEnabled: Boolean) {
        updateState(jukeboxStateFlow.first().copy(isConnectedToMachine = isEnabled))
    }
}