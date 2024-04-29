package com.example.jukeboxapp.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.jukeboxapp.model.JukeboxAppState


class JukeboxAppViewModel(initialState: JukeboxAppState) : ViewModel() {
    //private val bluetoothManager = com.example.jukeboxapp.ui.BluetoothManager(context)

    val isBluetoothEnabled = mutableStateOf(initialState.isBluetoothEnabled)
    val jukeboxName = mutableStateOf("My Jukebox")
    val lastSongSelection = mutableStateOf(initialState.lastSongSelection)

    fun updateBluetoothState(isEnabled: Boolean) {
        isBluetoothEnabled.value = isEnabled
    }

    fun updateJukeboxName(newName: String) {
        jukeboxName.value = newName
    }

    fun sendSelection(selection: String) {
        // TODO send selection via bluetooth
    }

    fun updateLastSongSelection(selection: String) {
        lastSongSelection.value = selection
    }
}