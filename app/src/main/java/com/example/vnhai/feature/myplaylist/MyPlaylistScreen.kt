package com.example.vnhai.feature.myplaylist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vnhai.R
import com.example.vnhai.data.local.entity.PlaylistEntity
import com.example.vnhai.data.local.entity.SongEntity

@Composable
fun MyPlaylist(
    modifier: Modifier = Modifier,
    viewModel: MyPlaylistViewModel = viewModel(factory = MyPlaylistViewModel.Factory)
) {
    val state = viewModel.uiState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.processIntent(MyPlaylistIntent.LoadListPlaylistWithSongs(context))
    }

    MyPlaylistScreen(
        isCreateNewVisible = state.value.isCreateNewVisible,
        myPlaylist = state.value.listPlaylistWithSongs.map { it->it.playlist },
        newName = state.value.newPlaylistName,
        onNewNameChange = {viewModel.processIntent(MyPlaylistIntent.EnterNewPlaylistName(it))},
        onChangeCreateVisible = {viewModel.processIntent(MyPlaylistIntent.ChangeCreateNewVisible)},
        onCreateClick = {viewModel.processIntent(MyPlaylistIntent.CreateNewPlaylist(context))},
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black)
    )
}


@Composable
fun MyPlaylistScreen(
    isCreateNewVisible: Boolean,
    newName: String,
    modifier: Modifier = Modifier,
    myPlaylist: List<PlaylistEntity> = listOf(),
    onPlaylistClick: (PlaylistEntity)->Unit = {},
    onNewNameChange: (String) -> Unit = {},
    onChangeCreateVisible: ()->Unit = {},
    onCreateClick: ()->Unit = {}

){
    Column(
        modifier = modifier
    ) {
        PlaylistHead(
            onAddPlaylistClick = onChangeCreateVisible,
            modifier = Modifier
                .padding(top = 50.dp)
                .height(60.dp),
        )

        Box(modifier = modifier
        ){
            if(myPlaylist.isEmpty())
            {
                EmptyPlaylistScreen(
                    onAddPlaylistClick = onChangeCreateVisible,
                    modifier = modifier
                )
            }
            else
            {
                LazyColumn(
                    modifier = modifier
                ){
                    items(myPlaylist) { playlist ->
                        Playlist(
                            playlist = playlist,
                            modifier = modifier.fillMaxWidth()
                        )
                    }
                }
            }

            if(isCreateNewVisible)
            {
                CreateNewPlaylist(
                    newName = newName,
                    onNewNameChange = onNewNameChange,
                    onDismissRequest = onChangeCreateVisible,
                    onCancelClick = onChangeCreateVisible,
                    onCreateClick = onCreateClick
                )
            }
        }

    }
}

@Composable
fun EmptyPlaylistScreen(
    modifier: Modifier,
    onAddPlaylistClick: ()->Unit = {}
) {
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ){
        Text(
            modifier = Modifier
                .padding(
                    start = 50.dp,
                    end = 50.dp,
                    top = 70.dp),
            text = "You don't have any playlists. Click the “+” button to add",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight(400),
            color = Color.White
        )

        Image(
            modifier = Modifier
                .padding(top = 25.dp)
                .clickable(onClick = onAddPlaylistClick)
                .size(86.dp),
            painter = painterResource(R.drawable.add_playlist_icon),
            contentDescription = "add playlist icon"
        )
    }
}

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
fun PlaylistHead(
    modifier: Modifier = Modifier,
    onAddPlaylistClick: ()->Unit = {},
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

        Icon(
            painter = painterResource(R.drawable.add_playlist_mini_icon),
            contentDescription = "layout icon",
            tint = Color.White,
            modifier = Modifier
                .clickable(
                    onClick = onAddPlaylistClick
                )
                .padding(end = 8.dp)
                .size(25.dp, 25.dp)
                .align(Alignment.CenterEnd),
        )
    }
}

@Composable
fun CreateNewPlaylist(
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    newName: String = "",
    onNewNameChange: (String)->Unit = {},
    onDismissRequest: ()->Unit = {},
    onCreateClick: ()->Unit = {},
    onCancelClick: ()->Unit = {},
){
    if(isVisible)
    {
        Dialog(
            onDismissRequest = onDismissRequest
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF292929)
                ),
                modifier = modifier
                    .size(353.dp, 216.dp),
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ){
                    Text(
                        text = "New Playlist",
                        fontWeight = FontWeight(700),
                        fontSize = 20.sp,
                        color = Color.White,
                        modifier = modifier
                            .padding(
                                top = 10.dp,
                                bottom = 32.dp
                            )
                    )

                    TextField(
                        value = newName,
                        placeholder = {
                            Text(
                                text = "Give your playlist a title",
                                color = Color(0xFF8A9A9D)
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF292929),
                            unfocusedContainerColor = Color(0xFF292929),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        onValueChange = onNewNameChange
                    )

                    HorizontalDivider(
                        modifier = modifier.fillMaxWidth()
                    )

                    Row (
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ){
                        Button(
                            onClick = onCancelClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF292929)
                            )
                        ) {
                            Text(
                                text = "Cancel",
                                fontWeight = FontWeight(700),
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        }

                        VerticalDivider(
                            modifier = modifier.fillMaxHeight()
                        )

                        Button(
                            onClick = onCreateClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF292929)
                            )
                        ) {
                            Text(
                                text = "Create",
                                fontWeight = FontWeight(700),
                                fontSize = 18.sp,
                                color = Color(0xFF00C2CB)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Playlist(
    modifier: Modifier = Modifier,
    playlist: PlaylistEntity
){
    Row (verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(8.dp),
        ){
        Image(
            painter = painterResource(R.drawable.music5),
            contentDescription = "Avatar"
        )
        Column (
            modifier = Modifier
                .padding(
                    start = 8.dp
                )
        ){
            Text(
                text = playlist.name,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = " songs",
                color = Color.White
            )
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
fun DrawImageFromPath(modifier: Modifier = Modifier, mp3FilePath: String) {
    var albumArtBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val retriever = MediaMetadataRetriever()
    try {
        retriever.setDataSource(mp3FilePath)
        val art = retriever.embeddedPicture
        if (art != null) {
            albumArtBitmap = BitmapFactory.decodeByteArray(art, 0, art.size)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        retriever.release()
    }

    Box(modifier = modifier){
        if(albumArtBitmap != null){
            Image(
                bitmap = albumArtBitmap!!.asImageBitmap(),
                contentDescription = "Album Art",
                modifier = modifier
            )
        }
        else
        {
            Image(
                painter = painterResource(R.drawable.music1),
                contentDescription = "Album Art",
                modifier = modifier
            )
        }
    }
}

@Preview
@Composable
fun PreviewMPS() {
//    MyPlaylistScreen(
//        isCreateNewVisible = false,
//        modifier = Modifier
//            .fillMaxSize()
//            .background(color = Color.Black)
//    )
}

@Preview
@Composable
fun PreviewMPS2() {
    PlaylistSongs(
        headName = "Alolo",
        listMusic = listOf(),
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    )
}