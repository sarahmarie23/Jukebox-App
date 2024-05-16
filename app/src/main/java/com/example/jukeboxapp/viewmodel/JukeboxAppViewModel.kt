package com.example.jukeboxapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jukeboxapp.data.JukeboxDataStore
import com.example.jukeboxapp.model.JukeboxState
import com.example.jukeboxapp.ui.BluetoothManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch




class JukeboxAppViewModel(initialState: JukeboxState, context: Context) : ViewModel() {
    // Variable declarations
    private val bluetoothManager = BluetoothManager(this, context)
    private val dataStore = JukeboxDataStore(context)
    private val _jukeboxStateFlow: MutableStateFlow<JukeboxState> = MutableStateFlow(JukeboxState())
    val jukeboxStateFlow: StateFlow<JukeboxState> = _jukeboxStateFlow

    init {
        observeJukeboxState()
    }

    private fun observeJukeboxState() {
        viewModelScope.launch {
            dataStore.jukeboxStateFlow.collect { state ->
                _jukeboxStateFlow.value = state
            }
        }
    }

    // Update functions
    private suspend fun updateState(newState: JukeboxState) {
        dataStore.updateJukeboxState(newState)
    }

    fun updateBluetoothState(isEnabled: Boolean) {
        viewModelScope.launch {
            dataStore.updateBluetoothState(isEnabled)
        }
    }

    fun updateLastSongSelection(selection: String) {
        viewModelScope.launch {
            dataStore.updateLastSongSelection(selection)
        }
    }

    fun updateJukeboxName(name: String) {
        val newName = if (name.isBlank()) "My Jukebox" else name

        viewModelScope.launch {
            dataStore.updateJukeboxName(newName)
        }
    }

    fun updateMachineType(machineType: String) {
        viewModelScope.launch {
            dataStore.updateMachineType(machineType)
        }
    }

    fun sendSelectionToReceiver(selection: String) {
        bluetoothManager.sendSelection(selection)
    }

    fun updateIsConnectedState(isConnected: Boolean) {
        viewModelScope.launch {
            dataStore.updateIsConnectedState(isConnected)
        }
    }

    /*
    Example of what you'd put in the Screen

        // Collect the jukebox state using a StateFlow or collectAsState
        val jukeboxState by viewModel.jukeboxState.collectAsState()

        // Update the jukebox state when needed
        LaunchedEffect(key1 = Unit) {
            val newState = JukeboxState(/* new state values */)
            viewModel.updateJukeboxState(newState)
        }
     */
}