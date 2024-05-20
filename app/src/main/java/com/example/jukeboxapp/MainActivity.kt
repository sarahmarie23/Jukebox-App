package com.example.jukeboxapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.jukeboxapp.data.JukeboxDataStore
import com.example.jukeboxapp.model.JukeboxState
import com.example.jukeboxapp.ui.BluetoothManager
import com.example.jukeboxapp.viewmodel.JukeboxAppViewModel
import com.example.jukeboxapp.ui.screens.PairedMachine
import com.example.jukeboxapp.ui.theme.JukeboxAppTheme
import com.example.jukeboxapp.viewmodel.JukeboxAppViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private lateinit var viewModel: JukeboxAppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JukeboxAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    navController = rememberNavController()
                    val state = JukeboxState(false, "00", "My Jukebox", "CD Machine")
                    val context = LocalContext.current
                    val dataStore = JukeboxDataStore(this)
                    val bluetoothManager = BluetoothManager(this)

                    val factory = JukeboxAppViewModelFactory(state, dataStore, bluetoothManager)
                    viewModel = ViewModelProvider(this, factory).get(JukeboxAppViewModel::class.java)
                    Navigation(navController, viewModel, bluetoothManager)
                }
            }
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JukeboxAppTheme {
        val navController = rememberNavController()
        val state = JukeboxState(false, "00", "My Jukebox", "CD Machine")
        val context = LocalContext.current
        val viewModel = JukeboxAppViewModel(state, context)
        PairedMachine(navController, viewModel)
    }
}

 */