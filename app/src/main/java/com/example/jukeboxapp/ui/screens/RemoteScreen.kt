package com.example.jukeboxapp.ui.screens

import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jukeboxapp.model.JukeboxState
import com.example.jukeboxapp.viewmodel.JukeboxAppViewModel
import com.example.jukeboxapp.ui.theme.JukeboxAppTheme
import kotlinx.coroutines.flow.map


@Composable
fun Remote(
    navController: NavController,
    viewModel: JukeboxAppViewModel,
    state: State<JukeboxState>,
    modifier: Modifier = Modifier
) {
    val jukeboxState by viewModel.jukeboxStateFlow.collectAsState()
    //val isBluetoothConnected by viewModel.jukeboxStateFlow.map { it.isBluetoothConnected }.collectAsState(initial = false)
    var lastSongSelection by rememberSaveable { mutableStateOf(jukeboxState.lastSongSelection.ifEmpty { "00" }) }
    var inputCompleted by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        //if (!isBluetoothConnected) {
            //  Text("Bluetooth is not connected", color = Color.Red)
        //} else {
            TextField(
                value = if (inputCompleted && lastSongSelection.isEmpty()) "00" else lastSongSelection,
                onValueChange = { newSelection ->
                    lastSongSelection = newSelection
                    inputCompleted = false
                    viewModel.updateLastSongSelection(newSelection)
                },
                label = { Text("Enter a number")},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        inputCompleted = true
                        if (lastSongSelection.isEmpty()) {
                            lastSongSelection = "00"
                            viewModel.updateLastSongSelection("00")
                        }
                        viewModel.sendSelectionToReceiver(lastSongSelection)
                        Toast.makeText(context, "Selection $lastSongSelection sent", Toast.LENGTH_SHORT).show()
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )
            }
        //}
}

// toast to show that the user must enter a number only
/*
fun onValueChange(newValue: String) {
    val numbersOnly = newValue.filter { it.isDigit() }
    if (numbersOnly != newValue) {
        showToastError(context)
    } else {
        // Update state with the validated number
    }
}
private fun showToastError(context: Context) {
    Toast.makeText(context,"Enter a number", Toast.LENGTH_SHORT)
}
*/
/*
@Preview(showBackground = true)
@Composable
fun RemoteScreenPreview() {
    val navController = rememberNavController()
    val state = JukeboxState(false, "00", "My Jukebox", "CD Machine",false)
    val context = LocalContext.current
    val testViewModel = JukeboxAppViewModel(state, context)
    JukeboxAppTheme {
        Remote(navController, testViewModel)
    }
}

 */