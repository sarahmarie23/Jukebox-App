package com.example.jukeboxapp.ui


import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.lifecycle.ViewModel
import com.example.jukeboxapp.viewmodel.JukeboxAppViewModel
import java.util.UUID



class BluetoothManager(
    viewModel: JukeboxAppViewModel,
    private val context: Context
) {
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }

    private var gatt: BluetoothGatt? = null

    private val LED_SERVICE_UUID = UUID.fromString("LED")
    private val SWITCH_CHARACTERISTIC_UUID = UUID.fromString("SWITCH")

    fun connectToDevice(device: BluetoothDevice) {
        try {
            gatt?.disconnect()
            gatt = device.connectGatt(context, false, gattCallback)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                try {
                    gatt.discoverServices()
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {

            }
        }
    }

    fun checkBluetoothState(viewModel: JukeboxAppViewModel) {
        val isEnabled = bluetoothAdapter?.isEnabled ?: false
        viewModel.updateBluetoothState(isEnabled)
    }
}