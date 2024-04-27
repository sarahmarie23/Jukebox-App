package com.example.jukeboxapp.ui

import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.compose.runtime.derivedStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class JukeboxAppViewModel(context: Context) : ViewModel() {
    private val bluetoothManager = BluetoothManager(context)

    private val _isBluetoothEnabled = MutableStateFlow(bluetoothManager.isBluetoothEnabled())
    val isBluetoothEnabled: StateFlow<Boolean> = _isBluetoothEnabled

    fun sendSelection(selection: String) {

    }
}