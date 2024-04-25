package com.example.jukeboxapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jukeboxapp.R
import com.example.jukeboxapp.ui.components.AppHeader
import com.example.jukeboxapp.ui.components.MyMachinesCard
import com.example.jukeboxapp.ui.components.PairedCard
import com.example.jukeboxapp.ui.components.TopAppBar
import com.example.jukeboxapp.ui.theme.JukeboxAppTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainPage(
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { TopAppBar() },
        modifier = modifier
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            ) {
                MyMachinesCard()
            }

            PairedCard()

        }

    }


}

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    JukeboxAppTheme {
        MainPage()

    }
}