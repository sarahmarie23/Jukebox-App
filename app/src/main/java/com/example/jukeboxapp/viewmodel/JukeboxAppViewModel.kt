package com.example.jukeboxapp.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jukeboxapp.data.JukeboxDataStore
import com.example.jukeboxapp.model.JukeboxAppState
import com.example.jukeboxapp.ui.BluetoothManager
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch



class JukeboxAppViewModel(initialState: JukeboxAppState, context: Context) : ViewModel() {
    private val bluetoothManager = BluetoothManager(this, context)
    private val dataStore: DataStore<Preferences> by context.preferencesDataStore(name = "jukebox_preferences")
    private val jukeboxDataStore = JukeboxDataStore(dataStore)

    val isBluetoothEnabled = mutableStateOf(initialState.isBluetoothEnabled)
    var connectionStatus: MutableState<String> = mutableStateOf("Disconnected")
    val jukeboxName = mutableStateOf("My Jukebox")
    private val _jukeboxType = MutableLiveData<String>()
    val jukeboxType: LiveData<String>
        get() = _jukeboxType

    val lastSongSelection: MutableState<String> = mutableStateOf( initialState.lastSongSelection)

    init {
        viewModelScope.launch {
            dataStore.storedJukeboxName.collect { savedName : String? ->
                jukeboxName.value = savedName?: "My Jukebox" // Load saved name on ViewModel initialization
            }
        }
    }

    val jukeboxUiState = combine(
        jukeboxDataStore.storedJukeboxName
    ) { storedJukeboxName ->
        JukeboxUiState(
            jukeboxName = storedJukeboxName ?: "My Jukebox"
        )
    }.distinctUntilChanged()

    fun updateBluetoothState(isEnabled: Boolean) {
        isBluetoothEnabled.value = isEnabled
    }

    fun updateConnectionStatus(isConnected: Boolean) {
        connectionStatus.value = if (isConnected) "Connected" else "Disconnected"
    }

    fun updateJukeboxName(newName: String) {
        viewModelScope.launch { // Use viewModelScope for asynchronous operations
            jukeboxDataStore.saveJukeboxName(newName) // Save the name to DataStore
            jukeboxName.value = newName // Update the UI state
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
data class JukeboxUiState(
    val jukeboxName: String = "My Jukebox"
)