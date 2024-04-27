package com.example.jukeboxapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.jukeboxapp.ui.JukeboxAppViewModel
import com.example.jukeboxapp.ui.screens.MachinePairing
import com.example.jukeboxapp.ui.screens.RemoteScreen
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
                    viewModel = JukeboxAppViewModel(this)
                    RemoteScreen(viewModel)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JukeboxAppTheme {
        MachinePairing()
    }
}