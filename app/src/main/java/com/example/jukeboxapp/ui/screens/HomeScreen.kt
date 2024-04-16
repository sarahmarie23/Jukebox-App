package com.example.jukeboxapp.ui.screens


import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.jukeboxapp.R
import com.example.jukeboxapp.ui.theme.JukeboxAppTheme

@Composable
fun OpeningScreen(name: String, logo: Painter, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Text(text = name,
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(text = stringResource(R.string.my_machines),
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier
                    .padding(16.dp)
            )
            Image(
                painter = logo,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Blue.copy(alpha = 0.2f))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OpeningScreenPreview() {
    JukeboxAppTheme {
        OpeningScreen(
            stringResource(R.string.app_name),
            painterResource(R.drawable.logo)
            )
    }
}