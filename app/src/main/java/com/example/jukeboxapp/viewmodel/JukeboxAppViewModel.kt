package com.example.jukeboxapp.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jukeboxapp.data.JukeboxDataStore
import com.example.jukeboxapp.model.JukeboxState
import com.example.jukeboxapp.ui.BluetoothManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch




class JukeboxAppViewModel(initialState: JukeboxState, context: Context) : ViewModel() {
    private val bluetoothManager = BluetoothManager(this, context)
    private val dataStore = JukeboxDataStore(context)

    val currentJukeboxState = viewModelScope.launch {
        dataStore.readJukeboxState()
    }

    val jukeboxState: Flow<JukeboxState> = dataStore.storedJukeboxName.map { savedName ->
        initialState.copy(machineName = savedName ?: "My Jukebox")
    }


    val isBluetoothEnabled = mutableStateOf(initialState.isBluetoothEnabled)
    var connectionStatus: MutableState<String> = mutableStateOf("Disconnected")
    val jukeboxName: Flow<String> = dataStore.storedJukeboxName.map { it ?: "My Jukebox" }
    val jukeboxType: Flow<String?> = _jukeboxType.asFlow()

    val lastSongSelection: MutableState<String> = mutableStateOf( initialState.lastSongSelection)


    init {
        jukeboxState.collect { savedName ->
            jukeboxName.value = savedName ?: "My Jukebox"
        }
    }

    fun updateBluetoothState(isEnabled: Boolean) {
        isBluetoothEnabled.value = isEnabled
    }

    fun updateConnectionStatus(isConnected: Boolean) {
        connectionStatus.value = when (isConnected) {
            true -> "Connected"
            false -> "Disconnected"
        }
    }

    fun updateJukeboxName(newName: String) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[JUKEBOX_NAME_KEY] = newName
            }
            jukeboxName.value = newName
        }
    }

    fun updateJukeboxType(type: String) {
        _jukeboxType.value = type
    }

    fun updateLastSongSelection(selection: String) {
        lastSongSelection.value = selection
        sendSelectionToReceiver(selection)
    }

    fun sendSelectionToReceiver(selection: String) {
        bluetoothManager.sendSelection(selection)
    }
}