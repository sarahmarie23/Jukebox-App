package com.example.jukeboxapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jukeboxapp.R
import com.example.jukeboxapp.model.JukeboxState
import com.example.jukeboxapp.ui.components.AppHeader
import com.example.jukeboxapp.ui.components.JukeboxNameCard
import com.example.jukeboxapp.ui.theme.JukeboxAppTheme
import com.example.jukeboxapp.viewmodel.JukeboxAppViewModel
import kotlinx.coroutines.flow.map

@Composable
fun PairedMachine(
    navController: NavController,
    viewModel: JukeboxAppViewModel,
    modifier: Modifier = Modifier
) {
    //val isBluetoothConnected by viewModel.jukeboxStateFlow.map { it.isBluetoothConnected }.collectAsState(initial = false)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {
        AppHeader()
        JukeboxNameCard(
            viewModel,
            modifier = Modifier.padding(6.dp)
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),

            ) {
            Text(
                stringResource(id = R.string.change_message),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(12.dp)
            )
        }

        Row() {
            OutlinedButton(onClick = {
                navController.navigate("main_page_screen")
                viewModel.updateBluetoothState(false)
            }) {
                Text(stringResource(id = R.string.go_back_button))
            }
            Spacer(modifier = Modifier.width(24.dp))
            Button(onClick = { navController.navigate("main_page_screen")
                viewModel.updateBluetoothState(true)
                //viewModel.updateJukeboxName(nameInput) TODO to save name
            }) {
                Text(stringResource(id = R.string.continue_button))
            }
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun MachinePairingPreview() {
    JukeboxAppTheme {
        val navController = rememberNavController()
        val state = JukeboxState(false, "00", "My Jukebox", "CD Machine", false)
        val context = LocalContext.current
        val viewModel = JukeboxAppViewModel(state, context)
        PairedMachine(navController, viewModel)
    }
}

 */
