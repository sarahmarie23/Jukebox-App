package com.example.jukeboxapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.jukeboxapp.R
import com.example.jukeboxapp.ui.theme.JukeboxAppTheme


@Composable
fun JukeboxNameCard(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RectangleShape,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.wurlitzer_1015))
            Image(
                painter = painterResource(id = R.drawable.wurlitzer_omt),
                contentDescription = null
            )
            //TextField(
              //  value = stringResource(id = ),
                //onValueChange = )
        }
    }
}

@Preview
@Composable
fun JukeboxNameCardPreview(

) {
    JukeboxAppTheme {
        JukeboxNameCard()
    }
}