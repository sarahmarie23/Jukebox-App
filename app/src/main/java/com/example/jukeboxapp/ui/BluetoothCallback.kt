package com.example.jukeboxapp.ui

interface BluetoothCallback {
    fun onBluetoothStateChange(isEnabled: Boolean)
    fun onMachineTypeUpdate(machineType: String)
    // Add other callback methods as needed
}