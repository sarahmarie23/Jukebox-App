package com.example.jukeboxapp.ui.screens

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
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
import androidx.core.content.ContextCompat
import com.example.jukeboxapp.R
import com.example.jukeboxapp.ui.components.AppHeader
import com.example.jukeboxapp.ui.components.MyMachinesCard
import com.example.jukeboxapp.ui.components.PairedCard
import com.example.jukeboxapp.ui.components.TopAppBar
import com.example.jukeboxapp.ui.theme.JukeboxAppTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainPage(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var bluetoothAdapter: BluetoothAdapter? by remember { mutableStateOf(null) }

    LaunchedEffect(context) {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            try {
                context.startActivity(enableBtIntent)
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
fun MakeToast() {
    val context = LocalContext.current
    Toast.makeText(context, "Bluetooth not connected", Toast.LENGTH_SHORT).show()
}

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    JukeboxAppTheme {
        MainPage()

    }
}