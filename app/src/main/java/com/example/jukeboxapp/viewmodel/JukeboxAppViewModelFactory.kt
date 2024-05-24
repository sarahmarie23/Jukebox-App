package com.example.jukeboxapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jukeboxapp.data.JukeboxDataStore
import com.example.jukeboxapp.model.JukeboxState
import com.example.jukeboxapp.ui.BluetoothManager

class JukeboxAppViewModelFactory(
    private val context: Context,
    private val bluetoothManager: BluetoothManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JukeboxAppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JukeboxAppViewModel(JukeboxDataStore(context), bluetoothManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}