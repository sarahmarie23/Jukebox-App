package com.example.jukeboxapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.jukeboxapp.data.DataStoreManager
import com.example.jukeboxapp.data.JukeboxDataStore
import com.example.jukeboxapp.model.JukeboxState
import com.example.jukeboxapp.ui.BluetoothManager
import com.example.jukeboxapp.viewmodel.JukeboxAppViewModel
import com.example.jukeboxapp.ui.theme.JukeboxAppTheme
import com.example.jukeboxapp.viewmodel.JukeboxAppViewModelFactory
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private lateinit var viewModel: JukeboxAppViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataStoreManager.initialize(applicationContext)

        setContent {
            JukeboxAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    navController = rememberNavController()
                    val state = JukeboxState(false, "00", "My Jukebox", "N/A", false)
                    val bluetoothManager = BluetoothManager(context)

                    val factory = JukeboxAppViewModelFactory(JukeboxDataStore(context), bluetoothManager)
                    viewModel = viewModel(factory = factory)
                    Navigation(navController, viewModel, bluetoothManager)
                }
            }
        }
    }
}

/*
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val dataStore = remember { JukeboxDataStore(context) }
    val bluetoothManager = remember { BluetoothManager(context) }

    val factory = remember { JukeboxAppViewModelFactory(dataStore, bluetoothManager) }
    val viewModel: JukeboxAppViewModel = viewModel(factory = factory)

    Navigation(navController, viewModel, bluetoothManager)
}

 */

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