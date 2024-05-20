package com.example.jukeboxapp.ui


import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.jukeboxapp.viewmodel.JukeboxAppViewModel
import java.util.UUID
import androidx.compose.runtime.State
import com.example.jukeboxapp.R
import com.example.jukeboxapp.R.string.jukebox_service_uuid
import com.example.jukeboxapp.R.string.machine_type_uuid
import com.example.jukeboxapp.R.string.song_number_uuid


class BluetoothManager(
    private val viewModel: JukeboxAppViewModel,
    private val context: Context
) {
    // Entry point for all bluetooth interaction
    // Used to discover devices, get a list of paired devices, instantiate a BluetoothDevice
    // using a known MAC address, and create a BluetoothServerSocket to listen for communications
    // from other devices
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }

    private val JUKEBOX_SERVICE_UUID = UUID.fromString(context.getString(jukebox_service_uuid))
    private val MACHINE_TYPE_UUID = UUID.fromString(context.getString(machine_type_uuid))
    private val SONG_NUMBER_UUID = UUID.fromString(context.getString(song_number_uuid))

    // Constants for permissions
    private val BLUETOOTH_PERMISSION = Manifest.permission.BLUETOOTH
    private val BLUETOOTH_ADMIN_PERMISSION = Manifest.permission.BLUETOOTH_ADMIN
    private val BLUETOOTH_CONNECT_PERMISSION = Manifest.permission.BLUETOOTH_CONNECT
    private val BLUETOOTH_SCAN_PERMISSION = Manifest.permission.BLUETOOTH_SCAN

    private val _connectionStatus = mutableStateOf("Disconnected")
    val connectionStatus: State<String> = _connectionStatus
    private var gatt: BluetoothGatt? = null
    private var connectedDevice: BluetoothDevice? = null

    private val isBluetoothEnabled: MutableState<Boolean> = mutableStateOf(false)

    private val PERMISSION_REQUEST_CODE = 123

    private var discoveredDevice: BluetoothDevice? = null

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            intent.getParcelableExtra(
                                BluetoothDevice.EXTRA_DEVICE,
                                BluetoothDevice::class.java
                            )
                        } else {
                            @Suppress("DEPRECATION")
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        }

                    if (device != null) {
                        val deviceAddress = device.address // Get the MAC address
                        var deviceName = "Name not available"
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.BLUETOOTH_CONNECT
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

                        // Check if permissions are granted before accessing device name
                        if (arePermissionsGranted()) {
                            deviceName = device.name ?: "Name not available"
                        }

                        // Check if the MAC address matches your Arduino's MAC address
                        if (deviceAddress == context.getString(R.string.ARDUINO_MAC_ADDRESS)) {
                            discoveredDevice = device
                        }
                    }
                }
            }
        }
    }

    private val bondStateChangeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
                // Check Bluetooth permissions before accessing device information
                if (arePermissionsGranted()) {
                    val device: BluetoothDevice? =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            intent.getParcelableExtra(
                                BluetoothDevice.EXTRA_DEVICE,
                                BluetoothDevice::class.java
                            )
                        } else {
                            @Suppress("DEPRECATION")
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        }

                    val previousBondState =
                        intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, -1)
                    val bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)

                    // Proceed with device information processing
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        Log.d(
                            "BluetoothManager",
                            "Bond State Change - Device: ${device?.name ?: "Unknown"}, Previous: $previousBondState, Current: $bondState"
                        )
                    }

                } else {
                    // Handle case where Bluetooth permissions are not granted
                    Log.e("BluetoothManager", "Bluetooth permissions not granted")
                }
            }
        }
    }

    init {
        // Register for Bluetooth device discovery broadcasts
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(receiver, filter)
        val bondStateChangeFilter = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        context.registerReceiver(bondStateChangeReceiver, bondStateChangeFilter)
    }

    fun checkBluetoothState(
        activityResultLauncher: ActivityResultLauncher<Intent>,
        permissionLauncher: ActivityResultLauncher<Array<String>>,
        viewModel: JukeboxAppViewModel
    ) {

        val isEnabled = bluetoothAdapter?.isEnabled ?: false
        Log.d("BluetoothManager", "Bluetooth Enabled: $isEnabled")
        viewModel.updateBluetoothState(isEnabled)

        if (!isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activityResultLauncher.launch(enableBtIntent)
        }

        // Request Bluetooth connect permission if not granted
        if (!arePermissionsGranted()) {
            requestPermissions(permissionLauncher)
        }
        Log.d("BluetoothManager", "Bluetooth Enabled: $isEnabled")
        viewModel.updateBluetoothState(isEnabled)
    }

    fun discoverDevices(requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //handlePermissionResult(requestCode)
            return // Don't proceed with discovery if permission is not granted
        }
        Log.d("BluetoothManager", "Starting Bluetooth discovery")
        discoveredDevice = null
        if (bluetoothAdapter?.isDiscovering == true) {
            bluetoothAdapter?.cancelDiscovery()
        }
        bluetoothAdapter?.startDiscovery()
    }

    fun pairWithDiscoveredDevice(requestCode: Int) {
        if (discoveredDevice != null) {
            Log.d("BluetoothManager", "Device was discovered! ${discoveredDevice!!.name}")
            // Request BLUETOOTH_ADMIN permission if not granted
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.BLUETOOTH_ADMIN),
                    requestCode
                )
                return
            }

            pairDevice(discoveredDevice!!)
            bluetoothAdapter?.cancelDiscovery() // Stop discovery after pairing

            // Add a delay to give the pairing process time to complete
            Handler(Looper.getMainLooper()).postDelayed({
                connectToDevice(discoveredDevice!!.address) // Use the MAC address instead of the device name
            }, 5000)
        } else {
            // Handle case where no device was found (e.g., show a message)
            Log.d("BluetoothManager", "No device discovered")
        }
    }

    // Connect to a remote bluetooth device or get info about it
    fun connectToDevice(deviceName: String) {
        // Get the Bluetooth adapter
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

        if (bluetoothAdapter == null) {
            Log.e("BluetoothManager", "Bluetooth adapter is not available")
            return
        }

        // Check if access to bonded devices is allowed
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission not granted, request it
            //requestPermissions()
            return
        }

        // Access to bonded devices is allowed, proceed with device discovery
        //val device = bluetoothAdapter.bondedDevices.find { it.name == deviceName }
        connectedDevice = bluetoothAdapter.bondedDevices.find { it.name == "Jukebox Receiver" || it.name == "Arduino"}
        if (connectedDevice != null) {
            Log.d("BluetoothManager", "Found device: ${connectedDevice!!.name}")
            try {
                // Establish a new GATT connection with the device
                gatt = connectedDevice!!.connectGatt(context, false, gattCallback)
                Log.d("BluetoothManager", "connectGatt Result: ${gatt != null}")
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        } else {
            // Device with the specified name not found
            Log.e("BluetoothManager", "Device with name $deviceName not found")
        }
    }

    fun sendSelection(selection: String) {
        Log.d("BluetoothManager", "About to send selection: $selection")
        Log.d("BluetoothManager", "Bluetooth Enabled: ${isBluetoothEnabled.value}")
        Log.d("BluetoothManager", "Gatt: ${gatt != null}")

        checkBluetoothState()

        if (isBluetoothEnabled.value && gatt != null) {
            val characteristic =
                gatt?.getService(JUKEBOX_SERVICE_UUID)?.getCharacteristic(SONG_NUMBER_UUID)
            characteristic?.let {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                val bytes = selection.toByteArray(Charsets.UTF_8)
                it.value = bytes

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    gatt?.writeCharacteristic(
                        it,
                        bytes,
                        BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
                    )
                } else {
                    characteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                    gatt?.writeCharacteristic(it)
                }
            }
        } else {
            Log.e("BluetoothManager", "Didn't send I think")
        }
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            Log.d(
                "BluetoothManager",
                "onConnectionStateChange - Status: $status, New State: $newState"
            )
            super.onConnectionStateChange(gatt, status, newState)
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    try {
                        gatt.discoverServices()
                        connectedDevice = gatt.device
                        updateConnectionStatus(true)
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                        Log.d("gattCallBack", "security exception")
                    }
                }

                BluetoothProfile.STATE_DISCONNECTED -> {
                    Log.d("gattCallBack", "state_disconnected")
                    connectedDevice = null // Reset connected device
                    updateConnectionStatus(false) // Update connection status here
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            Log.d("BluetoothManager", "onServicesDiscovered - Status: $status ")
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val service = gatt.getService(JUKEBOX_SERVICE_UUID)
                val characteristic = service?.getCharacteristic(MACHINE_TYPE_UUID)
                characteristic?.let {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            ActivityCompat.requestPermissions(
                                context as Activity,
                                arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 124
                            )
                        }
                        return
                    }
                    gatt.readCharacteristic(it)
                }
            } else {
                Log.e("BluetoothManager", "Failed to discover services. Status: $status")
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val data = characteristic.value
                val machineType = if (data[0] == 1.toByte()) "CD Version" else "Vinyl Version"
                Log.d("BluetoothManager", "Machine Type: $machineType")
                viewModel.updateMachineType(machineType)
            } else {
                Log.e("BluetoothManager", "Failed to read characteristic. Status: $status")
            }
        }
    }


    fun onDestroy() {
        // Unregister the receivers to prevent memory leaks
        context.unregisterReceiver(receiver)
        context.unregisterReceiver(bondStateChangeReceiver)
        // Close the BluetoothGatt connection if it's open
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
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
        gatt?.close()
        gatt = null
    }

    fun updateConnectionStatus(isConnected: Boolean) {
        _connectionStatus.value = if (isConnected) "Connected" else "Disconnected"
        //handleConnectionStateChange(isConnected)
    }

        /*
    fun handleConnectionStateChange(isConnected: Boolean) {
        viewModel.updateConnectionStatus(isConnected)
    }

 */

    fun pairDevice(device: BluetoothDevice) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_ADMIN // Important: need BLUETOOTH_ADMIN permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request BLUETOOTH_ADMIN permission
            return
        }

        Log.d("BluetoothManager", "Attempting to pair with: ${device.name}")
        val isPairingSuccessful = device.createBond()
        Log.d("BluetoothManager", "Pairing Result: $isPairingSuccessful")
    }

    fun checkBluetoothState() {
        val isEnabled = bluetoothAdapter?.isEnabled ?: false
        isBluetoothEnabled.value = isEnabled

        val bluetoothAdminPermission = Manifest.permission.BLUETOOTH_CONNECT
        if (ContextCompat.checkSelfPermission(context, bluetoothAdminPermission)
            != PackageManager.PERMISSION_GRANTED
        ) {
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

    // Function to check if permissions are granted
    fun arePermissionsGranted(): Boolean {
        val bluetoothPermissionGranted = ContextCompat.checkSelfPermission(
            context,
            BLUETOOTH_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

        val bluetoothAdminPermissionGranted = ContextCompat.checkSelfPermission(
            context,
            BLUETOOTH_ADMIN_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

        val bluetoothConnectPermissionGranted = ContextCompat.checkSelfPermission(
            context,
            BLUETOOTH_CONNECT_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

        val bluetoothScanPermissionGranted = ContextCompat.checkSelfPermission(
            context,
            BLUETOOTH_SCAN_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

        return bluetoothPermissionGranted && bluetoothAdminPermissionGranted &&
                bluetoothConnectPermissionGranted && bluetoothScanPermissionGranted
    }

    // Function to request permissions
    fun requestPermissions(permissionLauncher: ActivityResultLauncher<Array<String>>) {
        permissionLauncher.launch(arrayOf(
            BLUETOOTH_PERMISSION, BLUETOOTH_ADMIN_PERMISSION,
            BLUETOOTH_CONNECT_PERMISSION, BLUETOOTH_SCAN_PERMISSION
        ))
    }

    // Function to handle permission results
    fun handlePermissionResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            // Check if all permissions are granted
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Permissions are granted, proceed with Bluetooth operations
            } else {
                // Permissions are not granted, handle accordingly
            }
        }
    }

    // Other Bluetooth-related functions in BluetoothManager...

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
    }

    //fun getBluetoothAdapter(): BluetoothAdapter? {
    //  return bluetoothAdapter
    //}

    fun getConnectedDevice(): BluetoothDevice? {
        return connectedDevice
    }

    fun getGatt(): BluetoothGatt? {
        return gatt
    }

    fun getServiceUUID(): UUID {
        return JUKEBOX_SERVICE_UUID
    }

    fun getCharacteristicUUID(): UUID {
        return SONG_NUMBER_UUID
    }

}

