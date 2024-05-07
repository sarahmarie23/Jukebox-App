package com.example.jukeboxapp.viewmodel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.example.jukeboxapp.model.JukeboxAppState
import com.example.jukeboxapp.ui.BluetoothManager


class JukeboxAppViewModel(initialState: JukeboxAppState, context: Context) : ViewModel() {
    private val bluetoothManager = BluetoothManager(this, context)

    val isBluetoothEnabled = mutableStateOf(initialState.isBluetoothEnabled)
    val jukeboxName = mutableStateOf("My Jukebox")
    val lastSongSelection = mutableStateOf(initialState.lastSongSelection)

    fun updateBluetoothState(isEnabled: Boolean) {
        isBluetoothEnabled.value = isEnabled
    }

    fun updateJukeboxName(newName: String) {
        jukeboxName.value = newName
    }



    fun updateLastSongSelection(selection: String) {
        lastSongSelection.value = selection
    }


}