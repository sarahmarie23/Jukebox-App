package com.example.jukeboxapp


import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.navigation
import com.example.jukeboxapp.model.JukeboxAppState
import com.example.jukeboxapp.ui.BluetoothManager
import com.example.jukeboxapp.ui.screens.MainPage
import com.example.jukeboxapp.ui.screens.PairedMachine
import com.example.jukeboxapp.ui.screens.Remote
import com.example.jukeboxapp.viewmodel.JukeboxAppViewModel


@Composable
fun Navigation(navController: NavHostController, viewModel: JukeboxAppViewModel, bluetoothManager: BluetoothManager) {
    val state = JukeboxAppState(false, "00")
    val context = LocalContext.current

    NavHost(navController, startDestination = Screen.MainPageScreen.route) {
        composable(route = Screen.MainPageScreen.route) {
            MainPage(navController, viewModel, bluetoothManager)
        }
        composable(route = Screen.PairedMachineScreen.route) {
            PairedMachine(navController, viewModel)
        }
        composable(route = Screen.RemoteScreen.route) {
            Remote(navController, viewModel)
        }
    }
}
