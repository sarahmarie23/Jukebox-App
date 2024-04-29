package com.example.jukeboxapp

sealed class Screen(val route: String) {
    object MainPageScreen : Screen("main_page_screen")
    object PairedMachineScreen : Screen("paired_machine_screen")
    object RemoteScreen : Screen("remote_screen")
}