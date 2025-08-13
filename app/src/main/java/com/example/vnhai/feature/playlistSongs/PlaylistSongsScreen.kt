package com.example.vnhai.feature.playlistSongs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vnhai.R
import com.example.vnhai.data.local.entity.SongEntity
import com.example.vnhai.feature.myplaylist.DrawImageFromPath

@Composable
fun PlaylistSongs(
    headName: String,
    modifier: Modifier = Modifier,
    columnLayout: Boolean = true,
    listMusic: List<SongEntity>,
    onLayoutClick: () -> Unit = {},
    onSoftClick: () -> Unit = {},
    onDeleteClick: (music: SongEntity) -> Unit = {},
)
{
    Column(
        modifier = modifier
    ){
        PlaylistSongsHead(
            headName = headName,
            columnLayout = columnLayout,
            onLayoutClick = onLayoutClick,
            onSoftClick = onSoftClick,
            modifier = Modifier
                .padding(top = 50.dp)
                .height(60.dp),
        )

        if (columnLayout)
        {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
            ){
                items(listMusic) {
                        music -> ColumnMusicLayout(
                    modifier = Modifier
                        .fillMaxWidth(),
                    music = music,
                    onDeleteClick = onDeleteClick)
                }
            }
        }
        else
        {
            LazyVerticalGrid(
                modifier = Modifier
                    .background(color = Color.Black)
                    .fillMaxHeight(),
                columns = GridCells.Fixed(2),
            ) {
                items(listMusic) {
                        music -> GridMusicLayout(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                    ,
                    music = music,
                    onDeleteClick = onDeleteClick)
                }
            }
        }
    }
}

@Composable
fun PlaylistSongsHead(
    headName: String,
    modifier: Modifier = Modifier,
    columnLayout: Boolean = true,
    onLayoutClick: ()->Unit = {},
    onSoftClick: ()->Unit = {}
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
    {
        Text(
            modifier = Modifier
                .align(Alignment.Center),
            text = headName,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            color = Color.White
        )
        Row (
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(
                    end = 8.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                modifier = Modifier
                    .clickable(
                        onClick = onLayoutClick
                    )
                    .size(20.dp, 20.dp),
                painter = painterResource(if(columnLayout) R.drawable.grid_icon else R.drawable.column_icon),
                contentDescription = "layout icon",
                tint = Color.White
            )

            Icon(
                modifier = Modifier
                    .clickable(onClick = onSoftClick)
                    .size(25.dp, 25.dp),
                painter = painterResource(R.drawable.soft_icon),
                contentDescription = "soft_icon",
                tint = Color.White
            )
        }
    }
}

@Composable
fun GridMusicLayout(
    music: SongEntity,
    modifier: Modifier = Modifier,
    onDeleteClick: (SongEntity) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box{

            DrawImageFromPath(
                mp3FilePath = music.link,
                modifier = Modifier.size(100.dp)
            )

            KebabSongsMenu(
                modifier = Modifier
                    .align(Alignment.TopEnd),
                music = music,
                onDeleteClick = onDeleteClick
            )
        }
        Text(
            text = music.name,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.White
        )
        Text(
            text = music.author,
            fontSize = 12.sp,
            color = Color.Gray
        )
        Text(
            text = music.duration,
            fontSize = 12.sp,
            color = Color.White
        )
    }
}

@Composable
fun KebabSongsMenu(
    modifier: Modifier = Modifier,
    music: SongEntity,
    onDeleteClick: (SongEntity)->Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .padding(4.dp)
    ){
        IconButton(
            modifier = modifier
                .size(24.dp, 24.dp),
            onClick = { expanded = !expanded }
        ) {
            Icon(
                modifier = modifier
                    .size(20.dp, 20.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.7f),
                        shape = CircleShape
                    ),
                painter = painterResource(R.drawable.kebab_menu),
                contentDescription = "Delete Icon",
                tint = Color.White)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Delete") },
                onClick = {
                    expanded = false
                    onDeleteClick(music) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_delete_24),
                        contentDescription = "leading delete icon"
                    )
                }
            )
        }
    }
}

@Composable
fun ColumnMusicLayout(
    music: SongEntity,
    modifier: Modifier = Modifier,
    onDeleteClick: (SongEntity)->Unit = {}
){
    Row (
        modifier = modifier
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ){
        Row (
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row {

                DrawImageFromPath(
                    mp3FilePath = music.link,
                    modifier = Modifier.size(53.dp)
                )

                Column (
                    modifier = Modifier
                        .padding(
                            start = 8.dp
                        )
                ){
                    Text(
                        text = music.name,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = music.author,
                        color = Color.White
                    )
                }
            }

            Row (
                modifier = Modifier
                    .padding(
                        end = 8.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = music.duration,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(10.dp))
                KebabSongsMenu(
                    music = music,
                    onDeleteClick = onDeleteClick
                )
            }
        }
    }
}