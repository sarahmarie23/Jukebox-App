package com.example.jukeboxapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.jukeboxapp.R
import com.example.jukeboxapp.ui.theme.JukeboxAppTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

@Composable
fun BluetoothCard(
    //bluetooth: Painter,
    modifier: Modifier = Modifier
) {
    val openAlertDialog = remember { mutableStateOf(true) }

    when {
        // ...
        openAlertDialog.value -> {
            AlertDialogExample(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = {
                    openAlertDialog.value = false
                    println("Confirmation registered") // Add logic here to handle confirmation.
                },
                dialogTitle = stringResource(id = R.string.turn_on_bluetooth),
                dialogText = stringResource(id = R.string.connection_message),
                icon = Icons.Default.Settings
            )
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },

        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Turn on Bluetooth")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun BluetoothCardPreview() {
    JukeboxAppTheme {
        BluetoothCard(
            //painterResource(R.drawable.bluetooth)
        )
    }
}
