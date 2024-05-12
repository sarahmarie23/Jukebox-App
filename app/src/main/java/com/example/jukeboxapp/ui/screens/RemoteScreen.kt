package com.example.jukeboxapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jukeboxapp.model.JukeboxAppState
import com.example.jukeboxapp.viewmodel.JukeboxAppViewModel
import com.example.jukeboxapp.ui.theme.JukeboxAppTheme



@Composable
fun Remote(
    navController: NavController,
    viewModel: JukeboxAppViewModel,
    modifier: Modifier = Modifier
) {
    //val isBluetoothEnabled by viewModel.isBluetoothEnabled.mutableStateOf(false)
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        //if (!isBluetoothEnabled) {
            // display error message
        //} else {
            RemoteNumberInput(
                viewModel = viewModel,
                value = viewModel.lastSongSelection.value,
                onValueChange = { newSelection -> viewModel.updateLastSongSelection(newSelection) },
                onNumberSent = {
                    viewModel.sendSelectionToReceiver(viewModel.lastSongSelection.value)
                    Toast.makeText(context, "Selection ${viewModel.lastSongSelection.value} sent", Toast.LENGTH_SHORT).show()
                }
            )
            }
        //}
}



@Composable
fun RemoteNumberInput(
    viewModel: JukeboxAppViewModel,
    value: String,
    onValueChange: (String) -> Unit,
    onNumberSent: () -> Unit
) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Enter a number")},
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { onNumberSent() }
        ),
        modifier = Modifier.fillMaxWidth()
    )
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

@Preview(showBackground = true)
@Composable
fun RemoteScreenPreview() {
    val navController = rememberNavController()
    val state = JukeboxAppState(false, "00")
    val context = LocalContext.current
    val testViewModel = JukeboxAppViewModel(state, context)
    JukeboxAppTheme {
        Remote(navController, testViewModel)
    }
}