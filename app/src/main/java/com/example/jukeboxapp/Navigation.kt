package com.example.jukeboxapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.navigation
import com.example.jukeboxapp.ui.screens.MainPage
import com.example.jukeboxapp.ui.screens.PairedMachine
import com.example.jukeboxapp.ui.screens.Remote
import com.example.jukeboxapp.viewmodel.JukeboxAppViewModel


@Composable
fun Navigation(navController: NavController, viewModel: JukeboxAppViewModel) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Screen.MainPageScreen.route) {
        composable(route = Screen.MainPageScreen.route) {
            MainPage(navController)
        }
        composable(route = Screen.PairedMachineScreen.route) {
            PairedMachine(navController)
        }
        composable(route = Screen.RemoteScreen.route) {
            Remote(navController, viewModel)
        }
    }
}
