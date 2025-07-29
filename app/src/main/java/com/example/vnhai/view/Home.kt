package com.example.vnhai.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vnhai.R

@Composable
fun Home(
    modifier: Modifier = Modifier,
    onProfileIconClick: ()->Unit = {},
    onHomeButtonClick: ()->Unit = {},
    onPlaylistButtonClick: ()->Unit = {},
    onMyPlaylistButtonClick: ()->Unit = {}

) {
    Scaffold (
        topBar = {
            TopHomeBar(
                modifier = modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                onProfileIconClick = onProfileIconClick
            )
        },
        bottomBar = {
            BottomHomeBar(
                modifier = modifier
                    .fillMaxWidth(),
                onHomeButtonClick = onHomeButtonClick,
                onPlaylistButtonClick = onPlaylistButtonClick,
                onMyPlaylistButtonClick = onMyPlaylistButtonClick
            )
        }
    ){
            innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Home")
        }
    }
}

@Composable
fun TopHomeBar(
    modifier: Modifier = Modifier,
    onProfileIconClick: ()->Unit = {}
){
    Row (
        modifier = modifier
            .padding(top = 30.dp),
        horizontalArrangement = Arrangement.End
    ){
        Icon(
            modifier = Modifier
                .size(30.dp, 30.dp)
                .padding(end = 10.dp)
                .clickable(onClick = onProfileIconClick),
            painter = painterResource(R.drawable.setting_icon),
            contentDescription = "setting icon"
        )
    }
}

@Composable
fun BottomHomeBar(
    modifier: Modifier = Modifier,
    onHomeButtonClick: ()->Unit = {},
    onPlaylistButtonClick: ()->Unit = {},
    onMyPlaylistButtonClick: ()->Unit = {}
){
    Row(
        modifier = modifier
    ) {
        Button(
            modifier = modifier
                .weight(1f),
            shape = RectangleShape,
            border = BorderStroke(2.dp, Color.Black),
            onClick = onHomeButtonClick
        ) {
            Text("Home")
        }
        Button(
            modifier = modifier
                .weight(1f),
            shape = RectangleShape,
            border = BorderStroke(2.dp, Color.Black),
            onClick = onPlaylistButtonClick
        ) {
            Text("Playlist")
        }
        Button(
            modifier = modifier
                .weight(1f),
            shape = RectangleShape,
            border = BorderStroke(2.dp, Color.Black),
            onClick = onMyPlaylistButtonClick
        ) {
            Text("My Playlist")
        }
    }
}
