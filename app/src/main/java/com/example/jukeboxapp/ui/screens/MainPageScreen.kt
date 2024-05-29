package com.example.jukeboxapp.ui.screens

import android.Manifest
import androidx.compose.runtime.State
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.jukeboxapp.R
import com.example.jukeboxapp.model.JukeboxState
import com.example.jukeboxapp.ui.BluetoothManager
import com.example.jukeboxapp.ui.components.MyMachinesCard
import com.example.jukeboxapp.ui.components.PairedCard
import com.example.jukeboxapp.ui.components.TopAppBar
import com.example.jukeboxapp.viewmodel.JukeboxAppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

object BluetoothConstants {
    val DEVICE_NAME = R.string.jukebox_receiver.toString()
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainPage(
    navController: NavController,
    viewModel: JukeboxAppViewModel,
    bluetoothManager: BluetoothManager,
    state: State<JukeboxState>,
    modifier: Modifier = Modifier
) {
    val isBluetoothConnected by viewModel.jukeboxStateFlow.map { it.isBluetoothConnected }.collectAsState(initial = false)
    val currentState = state.value
    val isConnectedToMachine by viewModel.jukeboxStateFlow.map { it.isPairedToMachine }.collectAsState(initial = false)
    var delayNeeded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var deviceName by remember { mutableStateOf("") }
    val REQUEST_CODE_BLUETOOTH_SCAN = 1002
    val REQUEST_CODE_BLUETOOTH_ADMIN = 1003

    var isDeviceAdded by remember { mutableStateOf(false) }
    var bluetoothAdapter: BluetoothAdapter? by remember { mutableStateOf(null) }
    var discoveredDevices by remember { mutableStateOf<List<BluetoothDevice>>(emptyList()) }

    val bluetoothEnableLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("MainPageScreen", "Bluetooth enabled by user")
            // Now you can proceed with Bluetooth operations
        } else {
            Log.d("MainPageScreen", "Bluetooth enabling denied by user")
            // Handle Bluetooth not being enabled by user
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.BLUETOOTH_CONNECT] == true &&
            permissions[Manifest.permission.BLUETOOTH_SCAN] == true
        ) {
            Log.d("MainPageScreen", "All Bluetooth permissions granted")
            bluetoothManager.discoverDevices(123)
        } else {
            Log.d("MainPageScreen", "Bluetooth permissions denied")
            // Handle permission denial (e.g., show a message)
        }
    }

    LaunchedEffect(context) {
        // Request Bluetooth permissions
        withContext(Dispatchers.Main) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN
                )
            )
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            // Check Bluetooth state and connect to device
            bluetoothManager.checkBluetoothState(bluetoothEnableLauncher, permissionLauncher, viewModel)
            bluetoothManager.connectToDevice(BluetoothConstants.DEVICE_NAME)
        }
    }

    Scaffold(
        topBar = { TopAppBar() },
        modifier = modifier
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            ) {
                MyMachinesCard()
            }

            if (!isBluetoothConnected) {
                Text(text = "Turn on Bluetooth to pair with a machine")
            }
            Button(onClick = {
                Log.d("MainPageScreen", "Search for Devices button clicked")
                permissionLauncher.launch(arrayOf(Manifest.permission.BLUETOOTH_SCAN))
            }) {
                Text("Search for Devices")
            }
            Button(onClick = {
                Log.d("MainPageScreen", "Pair with Device button clicked")
                bluetoothManager.pairWithDiscoveredDevice(REQUEST_CODE_BLUETOOTH_ADMIN)
                navController.navigate("paired_machine_screen")
            }) {
                Text("Pair with Device")
            }
                /*
                discoveredDevices.forEach { device ->
                    if (ContextCompat.checkSelfPermission(context, Manifest
                        .permission.BLUETOOTH_CONNECT) == PackageManager
                            .PERMISSION_GRANTED) {
                        Text(
                            text = device.name ?: "Unknown Device",
                            modifier = Modifier.clickable {
                                bluetoothManager.pairDevice(device)
                                bluetoothAdapter?.cancelDiscovery() // Stop discovery after pairing
                            }
                        )
                    } else {
                        // Handle the case where BLUETOOTH_CONNECT permission is not granted
                        // You might want to display a message to the user or request the permission
                        Text("Missing Bluetooth Connect permission")
                    }
                }

                 */
                PairedCard(navController, viewModel)

        }
    }
}

@Composable
fun MakeToast(text: String) {
    val context = LocalContext.current
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}
/*
@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    JukeboxAppTheme {
        val navController = rememberNavController()
        val state = JukeboxState(false, "00", "My Jukebox", "CD Machine", false)
        val context = LocalContext.current
        val viewModel = JukeboxAppViewModel(state, context)
        val bluetoothManager = BluetoothManager(viewModel, context)
        MainPage(navController, viewModel, bluetoothManager)
    }
}

 */