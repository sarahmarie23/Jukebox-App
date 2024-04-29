package com.example.jukeboxapp.ui.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jukeboxapp.R
import com.example.jukeboxapp.Screen
import com.example.jukeboxapp.Screen.*
import com.example.jukeboxapp.Screen.RemoteScreen.route
import com.example.jukeboxapp.ui.theme.JukeboxAppTheme



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

@Composable
fun JukeboxNameCard(
    modifier: Modifier = Modifier
) {
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
                text = stringResource(id = R.string.cd_edition),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                value = stringResource(id = R.string.my_jukebox),
                onValueChange = { /**/ },
                label = { Text(stringResource(id = R.string.type_to_rename)) },
                //placeholder = { Text(stringResource(id = R.string.my_jukebox))}
            )
        }
    }

}
@Composable
fun PairedCard(navController: NavController) {
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
                    text = stringResource(id = R.string.my_jukebox),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
                Text(
                    text = stringResource(id = R.string.cd_edition),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,

                ) {
                    Icon(Icons.Rounded.CheckCircle, contentDescription = null)
                    Text(
                        text = stringResource(id = R.string.connected),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }

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
        AppHeader()
        PairedCard(navController)
    }
}