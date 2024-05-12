package com.example.jukeboxapp.model

data class JukeboxAppState(
    val isBluetoothEnabled: Boolean,
    val lastSongSelection: String,
    val machineName: String,
    val isCdMachine: Boolean,
)
