package com.example.jukeboxapp.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jukeboxapp.R
import com.example.jukeboxapp.model.JukeboxAppState
import com.example.jukeboxapp.ui.BluetoothManager
import com.example.jukeboxapp.ui.components.MyMachinesCard
import com.example.jukeboxapp.ui.components.PairedCard
import com.example.jukeboxapp.ui.components.TopAppBar
import com.example.jukeboxapp.ui.theme.JukeboxAppTheme
import com.example.jukeboxapp.viewmodel.JukeboxAppViewModel
import java.util.UUID

object BluetoothConstants {
    val DEVICE_NAME = R.string.jukebox_receiver.toString()
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainPage(
    navController: NavController,
    viewModel: JukeboxAppViewModel,
    bluetoothManager: BluetoothManager,
    modifier: Modifier = Modifier
) {
    val isBluetoothEnabled = viewModel.isBluetoothEnabled
    var delayNeeded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var deviceName by remember { mutableStateOf("") }
    val REQUEST_CODE_BLUETOOTH_SCAN = 1002
    val REQUEST_CODE_BLUETOOTH_ADMIN = 1003

    var isDeviceAdded by remember { mutableStateOf(false) }
    var bluetoothAdapter: BluetoothAdapter? by remember { mutableStateOf(null) }
    var discoveredDevices by remember { mutableStateOf<List<BluetoothDevice>>(emptyList()) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("MainPageScreen", "BLUETOOTH_SCAN permission granted")
            bluetoothManager.discoverDevices(REQUEST_CODE_BLUETOOTH_SCAN)
        } else {
            Log.d("MainPageScreen", "BLUETOOTH_SCAN permission denied")
            // Handle permission denial (e.g., show a message)
        }
    }

    LaunchedEffect(context) {
        bluetoothManager.checkBluetoothState(viewModel)
        bluetoothManager.connectToDevice(BluetoothConstants.DEVICE_NAME)
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

            if (!isBluetoothEnabled.value) {
                Text(text = "Turn on Bluetooth to pair with a machine")
                Button(onClick = {
                    deviceName = "Jukebox Receiver"
                    navController.navigate("paired_machine_screen") // TODO make this work
                    if (bluetoothManager.arePermissionsGranted()) {
                        bluetoothManager.connectToDevice(deviceName)
                    } else {
                        // Request permissions
                        bluetoothManager.requestPermissions()
                    }
                }) {
                    Text(stringResource(id = R.string.search))
                }
            } else {
                Button(onClick = {
                    Log.d("MainPageScreen", "Search for Devices button clicked")
                    launcher.launch(Manifest.permission.BLUETOOTH_SCAN)
                }) {
                    Text("Search for Devices")
                }
                Button(onClick = {
                    bluetoothManager.pairWithDiscoveredDevice(REQUEST_CODE_BLUETOOTH_ADMIN)
                }) {
                    Text("Pair with Device")
                }

                discoveredDevices.forEach { device ->
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
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
                PairedCard(navController, viewModel)
            }
        }
    }
}

@Composable
fun MakeToast(text: String) {
    val context = LocalContext.current
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    JukeboxAppTheme {
        val navController = rememberNavController()
        val state = JukeboxAppState(false, "00", "My Jukebox", false)
        val context = LocalContext.current
        val viewModel = JukeboxAppViewModel(state, context)
        val bluetoothManager = BluetoothManager(viewModel, context)
        MainPage(navController, viewModel, bluetoothManager)
    }
}