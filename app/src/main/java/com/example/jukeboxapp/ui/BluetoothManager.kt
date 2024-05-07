package com.example.jukeboxapp.ui


import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.jukeboxapp.R
import com.example.jukeboxapp.viewmodel.JukeboxAppViewModel
import java.util.UUID



class BluetoothManager(
    viewModel: JukeboxAppViewModel,
    private val context: Context
) {
    // Entry point for all bluetooth interaction
    // Used to discover devices, get a list of paired devices, instantiate a BluetoothDevice
    // using a known MAC address, and create a BluetoothServerSocket to listen for communications
    // from other devices
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }

    private var gatt: BluetoothGatt? = null
    private var connectedDevice: BluetoothDevice? = null

    private val isBluetoothEnabled: MutableState<Boolean> = mutableStateOf(false)
    private val appContext = context.applicationContext
    private val ANDROID_SERVICE_UUID = UUID.fromString(appContext.getString(R.string.uuid))
    private val CHARACTERISTIC_UUID = UUID.fromString("25CDD283-218B-44E2-927A-4EC194351E4F")

    val bluetoothManager = BluetoothManager(viewModel,context)

    // Connect to a remote bluetooth device or get info about it
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
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    try {
                        gatt.discoverServices()
                        connectedDevice = gatt.device
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                        Log.d("gattCallBack", "security exception")
                    }
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    Log.d("gattCallBack", "state_disconnected")
                }
            }
        }
    }
/*
    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        super.onServicesDiscovered(gatt, status)
        if (status == BluetoothGatt.GATT_SUCCESS) {
            // Services discovered successfully
            // Proceed with your communication logic
            val service = gatt.getService(ANDROID_SERVICE_UUID)
            val characteristic = service?.getCharacteristic()

            characteristic?.let {
                gatt.readCharacteristic(it)
                val data = byteArrayOf(0x01, 0x02) // Example data
                it.value = data
                gatt.writeCharacteristic(it)
            }
        } else {
            // Failed to discover services
            // Handle the error if needed
            Log.e("BluetoothManager", "Failed to discover services. Status: $status")
        }
    } */

    fun checkBluetoothState(viewModel: JukeboxAppViewModel) {
        val isEnabled = bluetoothAdapter?.isEnabled ?: false
        viewModel.updateBluetoothState(isEnabled)

        val bluetoothAdminPermission = Manifest.permission.BLUETOOTH_CONNECT
        if (ContextCompat.checkSelfPermission(context, bluetoothAdminPermission)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it from the user
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(bluetoothAdminPermission),
                123
            )
        } else {
            try {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                context.startActivity(enableBtIntent)
                bluetoothAdapter?.startDiscovery()
            } catch (e: ActivityNotFoundException) {
                Log.e("MainPage", "Bluetooth enable intent not found: ${e.message}")
            } catch (e: Exception) {
                Log.e("MainPage", "Failed to start Bluetooth enable intent: ${e.message}")
            }
        }
    }

    fun sendSelection(selection: String) {
        checkBluetoothState()
        if (isBluetoothEnabled.value) {
            bluetoothAdapter?.let { bluetoothAdapter ->
                connectedDevice?.let { connectedDevice ->
                    val bytes = selection.toByteArray(Charsets.UTF_8)
                    val characteristic = bluetoothManager.gatt?.getService(ANDROID_SERVICE_UUID)?.getCharacteristic(CHARACTERISTIC_UUID)
                    characteristic?.let {
                        it.value = bytes
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.BLUETOOTH
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return
                        }
                        gatt?.writeCharacteristic(it)
                    }
                }
            }
        }
    }

    fun checkBluetoothState() {
        val isEnabled = bluetoothAdapter?.isEnabled ?: false
        isBluetoothEnabled.value = isEnabled

        val bluetoothAdminPermission = Manifest.permission.BLUETOOTH_CONNECT
        if (ContextCompat.checkSelfPermission(context, bluetoothAdminPermission)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it from the user
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(bluetoothAdminPermission),
                123
            )
        } else {
            try {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                context.startActivity(enableBtIntent)
                bluetoothAdapter?.startDiscovery()
            } catch (e: ActivityNotFoundException) {
                Log.e("MainPage", "Bluetooth enable intent not found: ${e.message}")
            } catch (e: Exception) {
                Log.e("MainPage", "Failed to start Bluetooth enable intent: ${e.message}")
            }
        }
    }

    fun getBluetoothAdapter(): BluetoothAdapter? {
        return bluetoothAdapter
    }

    fun getConnectedDevice(): BluetoothDevice? {
        return connectedDevice
    }

    fun getGatt(): BluetoothGatt? {
        return gatt
    }
    fun getServiceUUID(): UUID {
        return ANDROID_SERVICE_UUID
    }

    fun getCharacteristicUUID(): UUID {
        return CHARACTERISTIC_UUID
    }
}