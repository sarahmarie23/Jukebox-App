package com.example.jukeboxapp.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.jukeboxapp.R
import com.example.jukeboxapp.ui.components.AppHeader
import com.example.jukeboxapp.ui.components.MyMachinesCard
import com.example.jukeboxapp.ui.components.PairedCard
import com.example.jukeboxapp.ui.components.TopAppBar
import com.example.jukeboxapp.ui.theme.JukeboxAppTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainPage(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var isDeviceAdded by remember { mutableStateOf(false) }
    var bluetoothAdapter: BluetoothAdapter? by remember { mutableStateOf(null) }

    LaunchedEffect(context) {
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

            PairedCard()

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
        MainPage(navController)
    }
}