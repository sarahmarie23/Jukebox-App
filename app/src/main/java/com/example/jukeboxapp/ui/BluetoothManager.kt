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
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.jukeboxapp.R
import com.example.jukeboxapp.viewmodel.JukeboxAppViewModel
import java.util.UUID
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf


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

    private val _connectionStatus = mutableStateOf("Disconnected")
    val connectionStatus: State<String> = _connectionStatus
    private var gatt: BluetoothGatt? = null
    private var connectedDevice: BluetoothDevice? = null

    private val isBluetoothEnabled: MutableState<Boolean> = mutableStateOf(false)
    private val appContext = context.applicationContext
    private val ANDROID_SERVICE_UUID = UUID.fromString(appContext.getString(R.string.service_uuid))
    private val CHARACTERISTIC_UUID = UUID.fromString(appContext.getString(R.string.characteristic_uuid))
    private val PERMISSION_REQUEST_CODE = 123

    private var discoveredDevice: BluetoothDevice? = null

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                    if (device != null) {
                        val deviceAddress = device.address // Get the MAC address
                        var deviceName = "Name not available"
                        Log.d("BluetoothManager", "Discovered Device - Address: ${device.address}, Name: ${device.name ?: "Name not available"}")
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                            deviceName = device.name ?: "Name not available"
                        }

                        Log.d("BluetoothManager", "Device found - Address: $deviceAddress, Name: $deviceName")

                        // Check if the MAC address matches your Arduino's MAC address
                        if (deviceAddress == "F4:12:FA:9C:95:85") {
                            discoveredDevice = device
                        }
                    }
                }
            }
        }
    }

    private val bondStateChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                val previousBondState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, -1)
                val bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)

                var deviceName = "Unknown"
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    deviceName = device?.name ?: "Unknown"
                }

                Log.d("BluetoothManager", "Bond State Change - Device: ${device?.name ?: "Unknown"}, Previous: $previousBondState, Current: $bondState")
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

    // Connect to a remote bluetooth device or get info about it
    fun connectToDevice(deviceName: String) {
        if (arePermissionsGranted()) {
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            Log.d("BluetoothManager", "Attempting to connect to device: $deviceName")
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
                requestPermissions()
                return
            }

            // Access to bonded devices is allowed, proceed with device discovery
            val device = bluetoothAdapter.bondedDevices.find { it.name == deviceName }
            if (device != null) {
                Log.d("BluetoothManager", "Found device: ${device.name}")
                try {
                    // Establish a new GATT connection with the device
                    gatt = device.connectGatt(context, false, gattCallback)
                    Log.d("BluetoothManager", "connectGatt Result: ${gatt != null}")
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            } else {
                // Device with the specified name not found
                Log.e("BluetoothManager", "Device with name $deviceName not found")
            }
        } else {
            // Request permissions
            requestPermissions()
        }
    }

    fun updateConnectionStatus(isConnected: Boolean) {
        _connectionStatus.value = if (isConnected) "Connected" else "Disconnected"
        handleConnectionStateChange(isConnected)
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            Log.d("BluetoothManager", "onConnectionStateChange - Status: $status, New State: $newState")
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
            Log.e("BluetoothManager", "onServicesDiscovered - Status: $status ")
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val service = gatt.getService(ANDROID_SERVICE_UUID)
                val characteristic = service?.getCharacteristic(CHARACTERISTIC_UUID)
                characteristic?.let {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            context as Activity,
                            arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 124
                        )
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
            status: Int) {
            Log.e("BluetoothManager", "gattCallback - onCharacteristicRead - Status: $status")
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val data = characteristic.value;
                if (data.isNotEmpty()) {
                    Log.e("BluetoothManager", "data not empty")
                    val deviceType = if (data[0] == 1.toByte()) "CD Version" else "Vinyl Version"//String(data, Charsets.UTF_8);
                    viewModel.updateJukeboxType(deviceType)
                }
            } else {
                Log.e("BluetoothManager", "Failed to read characteristic. Status: $status")
            }
        }
    }



    fun handleConnectionStateChange(isConnected: Boolean) {
        viewModel.updateConnectionStatus(isConnected)
    }

    fun checkBluetoothState(viewModel: JukeboxAppViewModel) {
        val isEnabled = bluetoothAdapter?.isEnabled ?: false
        Log.d("BluetoothManager", "Bluetooth Enabled: $isEnabled")
        viewModel.updateBluetoothState(isEnabled)

        if (!isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            context.startActivity(enableBtIntent)
        }

        // Request Bluetooth connect permission if not granted
        val bluetoothConnectPermission = Manifest.permission.BLUETOOTH_CONNECT
        if (ContextCompat.checkSelfPermission(context, bluetoothConnectPermission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(bluetoothConnectPermission),
                123
            )
        }
    }

    fun discoverDevices(requestCode: Int) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // Request BLUETOOTH_SCAN permission (you'll need to handle this)
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.BLUETOOTH_SCAN),
                requestCode // Define this request code
            )
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
            Log.d("BluetoothManager", "Device was discovered!")
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                // Request BLUETOOTH_ADMIN permission (you'll need to handle this)
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.BLUETOOTH_ADMIN),
                    requestCode // Define this request code (e.g., 1003)
                )
                return // Don't proceed with pairing if permission is not granted
            }

            pairDevice(discoveredDevice!!)
            bluetoothAdapter?.cancelDiscovery() // Stop discovery after pairing

            // Initiate the GATT connection
            connectToDevice(discoveredDevice!!.name)
        } else {
            // Handle case where no device was found (e.g., show a message)
            Log.d("BluetoothManager", "No device discovered")
        }
    }

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

    fun sendSelection(selection: String) {
        checkBluetoothState()
        if (isBluetoothEnabled.value && gatt != null) {
            val characteristic = gatt?.getService(ANDROID_SERVICE_UUID)?.getCharacteristic(CHARACTERISTIC_UUID)
            characteristic?.let {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return
                }
                val bytes = selection.toByteArray(Charsets.UTF_8)
                it.value = bytes
            /*
            bluetoothAdapter?.let { //bluetoothAdapter ->
                connectedDevice?.let { //connectedDevice ->
                    val bytes = selection.toByteArray(Charsets.UTF_8)
                    val characteristic = gatt?.getService(ANDROID_SERVICE_UUID)?.getCharacteristic(CHARACTERISTIC_UUID)
                    characteristic?.let {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.BLUETOOTH
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            return
                        }
                        */

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    gatt?.writeCharacteristic(it, bytes, BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE)
                } else {
                    characteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                    gatt?.writeCharacteristic(it)
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

    // Constants for permissions
    private val BLUETOOTH_PERMISSION = Manifest.permission.BLUETOOTH
    private val BLUETOOTH_ADMIN_PERMISSION = Manifest.permission.BLUETOOTH_ADMIN

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

        return bluetoothPermissionGranted && bluetoothAdminPermissionGranted
    }

    // Function to request permissions
    fun requestPermissions() {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(BLUETOOTH_PERMISSION, BLUETOOTH_ADMIN_PERMISSION),
            PERMISSION_REQUEST_CODE
        )
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
        return ANDROID_SERVICE_UUID
    }

    fun getCharacteristicUUID(): UUID {
        return CHARACTERISTIC_UUID
    }
}

