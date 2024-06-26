package com.example.jukeboxapp


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jukeboxapp.ui.BluetoothManager
import com.example.jukeboxapp.ui.screens.MainPage
import com.example.jukeboxapp.ui.screens.PairedMachine
import com.example.jukeboxapp.ui.screens.Remote
import com.example.jukeboxapp.viewmodel.JukeboxAppViewModel


@Composable
fun Navigation(navController: NavHostController, viewModel: JukeboxAppViewModel, bluetoothManager: BluetoothManager) {

    val state = viewModel.jukeboxStateFlow.collectAsState()

    NavHost(navController, startDestination = Screen.MainPageScreen.route) {
        composable(route = Screen.MainPageScreen.route) {
            MainPage(navController, viewModel, bluetoothManager, state)
        }
        composable(route = Screen.PairedMachineScreen.route) {
            PairedMachine(navController, viewModel, state)
        }
        composable(route = Screen.RemoteScreen.route) {
            Remote(navController, viewModel, state)
        }
    }
}
