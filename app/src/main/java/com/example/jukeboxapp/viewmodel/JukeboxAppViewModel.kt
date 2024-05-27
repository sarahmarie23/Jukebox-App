package com.example.jukeboxapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jukeboxapp.data.JukeboxDataStore
import com.example.jukeboxapp.model.JukeboxState
import com.example.jukeboxapp.ui.BluetoothCallback
import com.example.jukeboxapp.ui.BluetoothManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch




class JukeboxAppViewModel(
    private val dataStore: JukeboxDataStore,
    private val bluetoothManager: BluetoothManager
) : ViewModel(), BluetoothCallback {
    // Variable declarations

    private val _jukeboxStateFlow = MutableStateFlow(JukeboxState())
    val jukeboxStateFlow: StateFlow<JukeboxState> = _jukeboxStateFlow.asStateFlow()

    init {
        bluetoothManager.setBluetoothCallback(this)
        observeJukeboxState()
    }

    private fun observeJukeboxState() {
        viewModelScope.launch {
            try {
                dataStore.jukeboxStateFlow.collect { state ->
                    _jukeboxStateFlow.value = state
                }
            } catch (e: Exception) {
                // Handle the exception
                e.printStackTrace()
            }
        }
    }

    override fun onBluetoothStateChange(isEnabled: Boolean) {
        viewModelScope.launch {
            try {
                dataStore.updateBluetoothState(isEnabled)
                _jukeboxStateFlow.value = _jukeboxStateFlow.value.copy(isBluetoothConnected = isEnabled)
            } catch (e: Exception) {
                // Handle the exception
                e.printStackTrace()
            }
        }
    }

    override fun onMachineTypeUpdate(machineType: String) {
        viewModelScope.launch {
            try {
                dataStore.updateMachineType(machineType)
                //_jukeboxStateFlow.value = _jukeboxStateFlow.value.copy(machineType = machineType)
            } catch (e: Exception) {
                // Handle the exception
                e.printStackTrace()
            }
        }
    }

    // Update functions
    private suspend fun updateState(newState: JukeboxState) {
        dataStore.updateJukeboxState(newState)
    }

    fun updateBluetoothState(isEnabled: Boolean) {
        viewModelScope.launch {
            try {
                dataStore.updateBluetoothState(isEnabled)
            } catch (e: Exception) {
                // Handle the exception
                e.printStackTrace()
            }
        }
    }

    fun updateLastSongSelection(selection: String) {
        viewModelScope.launch {
            try {
                dataStore.updateLastSongSelection(selection)
            } catch (e: Exception) {
                // Handle the exception
                e.printStackTrace()
            }
        }
    }

    fun updateJukeboxName(name: String) {
        val newName = if (name.isBlank()) "My Jukebox" else name

        viewModelScope.launch {
            try {
                dataStore.updateJukeboxName(newName)
                _jukeboxStateFlow.update { currentState ->
                    currentState.copy(machineName = newName)
                }
            } catch (e: Exception) {
                // Handle the exception
                e.printStackTrace()
            }
        }
    }

    fun updateMachineType(machineType: String) {
        viewModelScope.launch {
            try {
                dataStore.updateMachineType(machineType)
                _jukeboxStateFlow.value = _jukeboxStateFlow.value.copy(machineType = machineType)
            } catch (e: Exception) {
                // Handle the exception
                e.printStackTrace()
            }
        }
    }

    fun sendSelectionToReceiver(selection: String) {
        bluetoothManager.sendSelection(selection)
    }

    fun updateIsPairedState(isConnected: Boolean) {
        viewModelScope.launch {
            try {
                dataStore.updateIsPairedState(isConnected)
                _jukeboxStateFlow.value = _jukeboxStateFlow.value.copy(isPairedToMachine = isConnected)
            } catch (e: Exception) {
                // Handle the exception
                e.printStackTrace()
            }
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