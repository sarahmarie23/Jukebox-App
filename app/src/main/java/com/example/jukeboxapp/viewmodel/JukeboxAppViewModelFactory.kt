package com.example.jukeboxapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jukeboxapp.data.JukeboxDataStore
import com.example.jukeboxapp.model.JukeboxState
import com.example.jukeboxapp.ui.BluetoothManager

class JukeboxAppViewModelFactory(
    private val initialState: JukeboxState,
    private val dataStore: JukeboxDataStore,
    private val bluetoothManager: BluetoothManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JukeboxAppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JukeboxAppViewModel(initialState, dataStore, bluetoothManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}