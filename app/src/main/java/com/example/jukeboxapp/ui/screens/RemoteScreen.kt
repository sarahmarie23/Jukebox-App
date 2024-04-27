package com.example.jukeboxapp.ui.screens

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jukeboxapp.ui.theme.JukeboxAppTheme



@Composable
fun RemoteScreen(
    modifier: Modifier = Modifier
) {
    var input by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        RemoteNumberInput(
            value = input,
            onValueChange = { newValue -> input = newValue },
            onNumberSent = {
                Toast.makeText(context, "Selection $input sent", Toast.LENGTH_SHORT).show()
                input = ""
            }
        )
    }
}

@Composable
fun RemoteNumberInput(
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
    JukeboxAppTheme {
        RemoteScreen()
    }
}