package com.example.jukeboxapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
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
    private val dataStore = context.dataStore

    companion object {
        val IS_BLUETOOTH_CONNECTED_KEY = booleanPreferencesKey("is_bluetooth_enabled")
        val LAST_SONG_SELECTION_KEY = stringPreferencesKey("last_song_selection")
        val JUKEBOX_NAME_KEY = stringPreferencesKey("jukebox_name")
        val MACHINE_TYPE_KEY = stringPreferencesKey("machine_type")
        val IS_PAIRED_TO_MACHINE_KEY = booleanPreferencesKey("is_connected_to_machine")
    }

    // Functions to update individual state values
    suspend fun updateBluetoothState(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_BLUETOOTH_CONNECTED_KEY] = isEnabled
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
            preferences[MACHINE_TYPE_KEY] = machineType
        }
    }
    suspend fun updateIsPairedState(isConnected: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_PAIRED_TO_MACHINE_KEY] = isConnected
        }
    }

    // Generic function to update all state values
    private suspend fun updateState(state: JukeboxState) {
        dataStore.edit { preferences ->
            preferences[IS_BLUETOOTH_CONNECTED_KEY] = state.isBluetoothConnected
            preferences[LAST_SONG_SELECTION_KEY] = state.lastSongSelection
            preferences[JUKEBOX_NAME_KEY] = state.machineName
            preferences[MACHINE_TYPE_KEY] = state.machineType
            preferences[IS_PAIRED_TO_MACHINE_KEY] = state.isPairedToMachine
        }
    }
    suspend fun updateJukeboxState(newState: JukeboxState) {
        updateState(newState)
    }

    private fun <T> withDefault(preferences: Preferences, key: Preferences.Key<T>, defaultValue: T): T{
        return try {
            preferences[key]?: defaultValue;
        } catch (e: ClassCastException) {
            defaultValue;
        }
    }
    // Flow to observe state changes
    val jukeboxStateFlow: Flow<JukeboxState> = dataStore.data.map { preferences ->
        val isBluetoothEnabled = withDefault(preferences, IS_BLUETOOTH_CONNECTED_KEY, false)
        val lastSongSelection = withDefault(preferences, LAST_SONG_SELECTION_KEY, "")
        val machineName = withDefault(preferences, JUKEBOX_NAME_KEY, "")
        val machineType = withDefault(preferences, MACHINE_TYPE_KEY, "")
        JukeboxState(isBluetoothEnabled, lastSongSelection, machineName, machineType)
    }.distinctUntilChanged()

    // Function to read state
    suspend fun readJukeboxState(): JukeboxState {
        return jukeboxStateFlow.first()
    }
/*
    // Flow to observe state changes
    val jukeboxStateFlow: Flow<JukeboxState> = dataStore.data.map { preferences ->
        JukeboxState(
            isBluetoothConnected = preferences[IS_BLUETOOTH_CONNECTED_KEY] ?: false,
            lastSongSelection = preferences[LAST_SONG_SELECTION_KEY] ?: "",
            machineName = preferences[JUKEBOX_NAME_KEY] ?: "My Jukebox",
            machineType = preferences[MACHINE_TYPE_KEY] ?: "N/A",
            isPairedToMachine = preferences[IS_PAIRED_TO_MACHINE_KEY] ?: false
        )
    }.distinctUntilChanged()

    // Function to read state
    suspend fun readJukeboxState(): JukeboxState {
        return dataStore.data.map { preferences ->
            JukeboxState(
                isBluetoothConnected = preferences[IS_BLUETOOTH_CONNECTED_KEY]?: false,
                lastSongSelection = preferences[LAST_SONG_SELECTION_KEY] ?: "",
                machineName = preferences[JUKEBOX_NAME_KEY] ?: "My Jukebox",
                machineType = preferences[MACHINE_TYPE_KEY] ?: "N/A",
                isPairedToMachine = preferences[IS_PAIRED_TO_MACHINE_KEY] ?: false
            )
        }.first()
    }

    // Probably won't need but keeping it just in case
    suspend fun updateBluetoothStateFlow(isEnabled: Boolean) {
        updateState(jukeboxStateFlow.first().copy(isPairedToMachine = isEnabled))
    }
     */
}