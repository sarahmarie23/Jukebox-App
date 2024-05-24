package com.example.jukeboxapp.ui.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jukeboxapp.R
import com.example.jukeboxapp.Screen.RemoteScreen.route
import com.example.jukeboxapp.viewmodel.JukeboxAppViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                stringResource(id = R.string.app_name),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}
// This is on the PairedMachineScreen where you can change the name and confirm the machine
@Composable
fun JukeboxNameCard(
    viewModel: JukeboxAppViewModel,
    modifier: Modifier = Modifier
) {
    val jukeboxState by viewModel.jukeboxStateFlow.collectAsState()

    var nameInput by remember { mutableStateOf(jukeboxState.machineName) }
    val isConnectedToMachine = jukeboxState.isPairedToMachine
    val machineType = if (isConnectedToMachine) jukeboxState.machineType else "N/A"

    Surface(
        modifier = Modifier.width(300.dp),
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.wurlitzer_1015))
            Image(
                painter = painterResource(id = R.drawable.wurlitzer_omt),
                contentDescription = null,
                modifier = Modifier
                    .padding(12.dp)
                    .border(BorderStroke(2.dp, Color.Black))
            )

            Text(
                text = machineType,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                value = nameInput,
                onValueChange = { nameInput = it },
                label = { Text(stringResource(id = R.string.jukebox_name)) },
            )
        }
    }
}

@Composable
fun PairedCard(
    navController: NavController,
    viewModel: JukeboxAppViewModel
) {
    val jukeboxState by viewModel.jukeboxStateFlow.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable {
                Log.d("Navigation", "Navigating from PairedCard")
                navController.navigate(route)
            }
    ){
        Row {
            Image(
                painter = painterResource(id = R.drawable.wurlitzer_black_bg),
                contentDescription = null
            )
            Column(
                horizontalAlignment = AbsoluteAlignment.Right
            ) {
                Text(
                    text = jukeboxState.machineName,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
                //viewModel.jukeboxType.value?.let {
                //}
                Row(
                    verticalAlignment = Alignment.CenterVertically,

                ) {
                    Icon(Icons.Rounded.CheckCircle, contentDescription = null)
                    Text(
                        text = if (jukeboxState.isPairedToMachine) "Connected" else "Disconnected",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }
                Text(
                    text = if (jukeboxState.isPairedToMachine) jukeboxState.machineType else "N/A",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
            }
        }
    }
}

@Composable
fun MyMachinesCard(
    modifier: Modifier = Modifier
) {
    Text(text = stringResource(R.string.my_machines),
        style = MaterialTheme.typography.displaySmall,
        modifier = Modifier
            .padding(16.dp)
    )
}
/*

@Preview
@Composable
fun TopAppBarPreview(

) {
    JukeboxAppTheme {
        TopAppBar()
    }
}

@Preview(showBackground = true)
@Composable
fun JukeboxNameCardPreview(

) {
    JukeboxAppTheme {
        //JukeboxNameCard()
    }
}
@Preview(showBackground = true)
@Composable
fun PairedCardPreview(

) {
    JukeboxAppTheme {
        val navController = rememberNavController()
        val state = JukeboxState(false, "00", "My Jukebox", "CD Machine")
        val context = LocalContext.current
        val viewModel = JukeboxAppViewModel(state, context)
        AppHeader()
        PairedCard(navController, viewModel)
    }
}

 */