package com.example.vnhai

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vnhai.ui.theme.VNHaiTheme

fun String.onlyLetters() = all { it.isLetter() }

data class Music(
    val image: Int,
    val name: String,
    val author: String,
    val duration: String,
)

val listMusic = listOf<Music>(
    Music(
        R.drawable.music1,
        "grainy days",
        "moody",
        "04:30"
    ),
    Music(
        R.drawable.music2,
        "Coffee",
        "Kainbeats",
        "04:30"
    ),
    Music(
        R.drawable.music3,
        "raindrops",
        "rainyyxx",
        "00:30"
    ),
    Music(
        R.drawable.music4,
        "Tokyo",
        "SmYang",
        "04:02"
    ),
    Music(
        R.drawable.music5,
        "Coffee",
        "iamfinenow",
        "04:02"
    ),
    Music(
        R.drawable.music1,
        "grainy days 2",
        "moody",
        "04:30"
    ),
    Music(
        R.drawable.music2,
        "Coffee 2",
        "Kainbeats",
        "04:30"
    ),
    Music(
        R.drawable.music3,
        "raindrops 2",
        "rainyyxx",
        "00:30"
    ),
    Music(
        R.drawable.music4,
        "Tokyo 2",
        "SmYang",
        "04:02"
    ),
    Music(
        R.drawable.music5,
        "Coffee 2",
        "iamfinenow",
        "04:02"
    )
)

class MainActivity : ComponentActivity() {
    @SuppressLint("MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VNHaiTheme {
                var columnLayout by remember { mutableStateOf(false) }
                var listMusicState by remember { mutableStateOf(listMusic) }
                var mutableListMusic = listMusicState.toMutableList()
                Screen(
                    columnLayout = columnLayout,
                    listMusic = listMusicState,
                    onLayoutClick = {
                        columnLayout = !columnLayout
                    },
                    onSoftClick = {
                        listMusicState = listMusicState.sortedBy { it.name }
                    },
                    onDeleteClick = { it ->
                        mutableListMusic.remove(it)
                        listMusicState = mutableListMusic
                    }
                )
            }
        }
    }
}

@Composable
fun Screen(
    modifier: Modifier = Modifier,
    columnLayout: Boolean = true,
    listMusic: List<Music>,
    onLayoutClick: () -> Unit = {},
    onSoftClick: () -> Unit = {},
    onDeleteClick: (music:Music) -> Unit = {},
    )
{
    if (columnLayout)
    {
        LazyColumn(
            modifier = Modifier.background(color = Color.Black)
        ){
            item {
                Head(
                    modifier = Modifier
                        .height(60.dp),
                    layoutIcon = R.drawable.grid_icon,
                    onLayoutClick = onLayoutClick,
                    onSoftClick = onSoftClick
                )
            }
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
            item(span = { GridItemSpan(maxLineSpan) }){
                Head(
                    modifier = Modifier
                        .height(60.dp),
                    layoutIcon = R.drawable.column_icon,
                    onLayoutClick = onLayoutClick,
                    onSoftClick = onSoftClick
                )
            }
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

@Composable
fun Head(
    modifier: Modifier = Modifier,
    layoutIcon: Int = R.drawable.grid_icon,
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
            text = "My Playlist",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            color = Color.White
        )
        Row (
            modifier = modifier
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
                painter = painterResource(layoutIcon),
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
fun ColumnMusicLayout(
    modifier: Modifier = Modifier,
    music: Music = Music(R.drawable.music1,
        "grainy days",
        "moody",
        "04:30"),
    onDeleteClick: (Music)->Unit = {}
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
                Image(
                    painter = painterResource(music.image),
                    contentDescription = "Avatar"
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
                KebabMenu(
                    music = music,
                    onDeleteClick = onDeleteClick
                )
            }
        }
    }
}

@Composable
fun GridMusicLayout(
    modifier: Modifier = Modifier,
    music: Music = Music(
        R.drawable.music1,
        "grainy days",
        "moody",
        "04:30"
    ),
    onDeleteClick: (Music) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box{
            Image(

                modifier = Modifier
                    .size(100.dp, 100.dp),
                painter = painterResource(music.image),
                contentDescription = "Avatar"
            )
            KebabMenu(
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
fun KebabMenu(
    modifier: Modifier = Modifier,
    music: Music,
    onDeleteClick: (Music)->Unit = {}
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

@Preview(
    name = "1",
    showBackground = true)
@Composable
fun Preview1(
){
//    var test by remember { mutableStateOf(true) }
//    Head(
//        modifier = Modifier
//            .background(color = Color.Black),
//        layoutIcon = R.drawable.grid_icon,
//        onLayoutClick = {
//            test = !test
//            Log.d("Test", "test value: $test")
//        },
//        onSoftClick = {  }
//    )
    GridMusicLayout()
}


