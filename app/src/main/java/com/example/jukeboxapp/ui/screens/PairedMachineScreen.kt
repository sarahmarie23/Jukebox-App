package com.example.jukeboxapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jukeboxapp.R
import com.example.jukeboxapp.ui.components.AppHeader
import com.example.jukeboxapp.ui.components.Background
import com.example.jukeboxapp.ui.components.Background2
import com.example.jukeboxapp.ui.components.JukeboxNameCard
import com.example.jukeboxapp.ui.theme.JukeboxAppTheme

@Composable
fun MachinePairing(
    modifier: Modifier = Modifier
) {
    //Background2()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {
        AppHeader()
        JukeboxNameCard(
            modifier = Modifier.padding(6.dp)
        )
        //Spacer(modifier = Modifier.height(24.dp))
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

        //Spacer(modifier = Modifier.height(24.dp))
        Row(

        ) {

            OutlinedButton(onClick = { /*TODO*/ }) {
                Text(stringResource(id = R.string.go_back_button))
            }
            Spacer(modifier = Modifier.width(24.dp))
            Button(onClick = { /*TODO*/ }) {
                Text(stringResource(id = R.string.continue_button))
            }


        }
    }


}

@Preview(showBackground = true)
@Composable
fun MachinePairingPreview() {
    JukeboxAppTheme {
        MachinePairing()
    }
}
