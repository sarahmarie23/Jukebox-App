package com.example.jukeboxapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.jukeboxapp.model.JukeboxAppState
import com.example.jukeboxapp.viewmodel.JukeboxAppViewModel
import com.example.jukeboxapp.ui.screens.PairedMachine
import com.example.jukeboxapp.ui.screens.MainPage
import com.example.jukeboxapp.ui.theme.JukeboxAppTheme

class MainActivity : ComponentActivity() {
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
                    val testState = JukeboxAppState(/*true,*/ "00")
                    val testViewModel = JukeboxAppViewModel(testState)
                    //RemoteScreen(testViewModel)
                    val navController = rememberNavController()
                    //MainPage(navController)
                    Navigation(navController, testViewModel)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JukeboxAppTheme {
        val navController = rememberNavController()
        PairedMachine(navController)
    }
}