package com.example.vnhai.feature.library

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.vnhai.Music
import com.example.vnhai.Playlist
import com.example.vnhai.R

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier,
    viewModel: LibraryViewModel = LibraryViewModel(),
    navigationToPlaylist: () -> Unit,
) {
    val state = viewModel.uiState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.processIntent(LibraryIntent.GetPermissionState(context))
    }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        viewModel.processIntent(LibraryIntent.GetPermissionState(context))
    }

    Column(modifier = modifier
        .fillMaxSize()
        .background(color = Color.Black))
    {
        HeadLibrary(modifier = Modifier
            .height(180.dp)
            .fillMaxWidth(),
            isLocal = state.value.isLocal,
            onLocalClick = { viewModel.processIntent(LibraryIntent.ChangeDirection(true)) },
            onRemoteClick = {viewModel.processIntent(LibraryIntent.ChangeDirection(false))}
        )
        if(state.value.isLocal)
        {
            Log.d("main", "permission: ${state.value.hasPermission}")
            if(state.value.hasPermission)
            {
                LaunchedEffect(Unit) {
                    viewModel.processIntent(LibraryIntent.LoadData(context))
                }
                Box {
                    LocalScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.Black),
                        listMusic = state.value.listMusic,
                        onAddToPlaylistClick = {viewModel.processIntent(LibraryIntent.ChangeVisiblePlaylist)}
                    )

                    ChoosePlaylistDialog(
                        modifier = Modifier
                            .size(353.dp, 458.dp),
                        isVisible = state.value.isVisiblePlaylist,
                        onDismissRequest = {viewModel.processIntent(LibraryIntent.ChangeVisiblePlaylist)},
                        onAddPlaylistClick = {
                            navigationToPlaylist()
                            viewModel.processIntent(LibraryIntent.ChangeVisiblePlaylist)
                        }
                    )
                }
            }
            else
            {
                MyPermissionDialog(
                    modifier = Modifier
                        .size(353.dp, 204.dp),
                    isVisible = state.value.isPermissionDialogVisible,
                    onAllowClick = {
                        requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO) },
                    onDontAllowClick = {viewModel.processIntent(LibraryIntent.ChangeVisiblePermissionDialog(false))},
                )
            }
        }
        else{
            RemoteScreen()
        }
    }
}

@Composable
fun LocalScreen(
    modifier: Modifier = Modifier,
    listMusic: List<Music> = listOf(),
    onAddToPlaylistClick: (Music) -> Unit = {}
)
{
    Box(modifier = modifier)
    {
        LazyColumn(
            modifier = modifier
                .padding(8.dp)
        ){
            items(listMusic) {
                    music -> ColumnMusicLayout(
                modifier = Modifier
                    .fillMaxWidth(),
                music = music,
                onAddToPlaylistClick = onAddToPlaylistClick,
                onShareClick = {})
            }
        }
    }
}

@Composable
fun RemoteScreen(){

}

@Composable
fun HeadLibrary(
    modifier: Modifier = Modifier,
    isLocal: Boolean = true,
    onLocalClick:()->Unit = {},
    onRemoteClick:()->Unit = {}
){
    Column(
        modifier = modifier
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .height(30.dp),
            text = "Library",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Row(
            modifier = Modifier
                .height(70.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            if(isLocal)
            {
                Button(
                    modifier = Modifier
                        .size(140.dp, 50.dp)
                        .padding(5.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00C2CB)
                    ),
                    onClick = onLocalClick
                ) {
                    Text(
                        text = "Local",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Button(
                    modifier = Modifier
                        .size(140.dp, 50.dp)
                        .padding(5.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1E1E1E)
                    ),
                    onClick = onRemoteClick
                ) {
                    Text(
                        text = "Remote",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
            else
            {
                Button(
                    modifier = Modifier
                        .size(140.dp, 50.dp)
                        .padding(5.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1E1E1E)
                    ),
                    onClick = onLocalClick
                ) {
                    Text(
                        text = "Local",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Button(
                    modifier = Modifier
                        .size(140.dp, 50.dp)
                        .padding(5.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00C2CB)
                    ),
                    onClick = onRemoteClick
                ) {
                    Text(
                        text = "Remote",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ColumnMusicLayout(
    modifier: Modifier = Modifier,
    music:Music = Music(
        "C:/Users/Admin/HaiVN/app/src/main/res/drawable/music1.webp",
        "grainy days",
        "moody",
        "04:30"),
    onAddToPlaylistClick: (Music) -> Unit = {},
    onShareClick: (Music) -> Unit = {}
){
    Row(
        modifier = modifier
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        DrawImageFromPath(mp3FilePath = music.link)
        Column(
            modifier = Modifier
                .padding(
                    start = 8.dp
                )
                .weight(1f)
        ) {
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
        Text(
            text = music.duration,
            color = Color.White
        )
        Spacer(modifier = Modifier.width(10.dp))
        KebabMenu(
            modifier = Modifier
                .size(25.dp),
            music = music,
            onAddToPlaylistClick = onAddToPlaylistClick,
            onShareClick = onShareClick
        )
    }
}

@Composable
fun KebabMenu(
    modifier: Modifier = Modifier,
    music: Music,
    onAddToPlaylistClick: (Music)->Unit = {},
    onShareClick: (Music)->Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .padding(4.dp)
    ) {
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
                tint = Color.White
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Add to Playlist") },
                onClick = {
                    expanded = false
                    onAddToPlaylistClick(music)
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.music_icon),
                        contentDescription = "leading music icon"
                    )
                }
            )

            DropdownMenuItem(
                text = { Text("Share") },
                onClick = {
                    expanded = false
                    onShareClick(music)
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.share_icon),
                        contentDescription = "leading share icon"
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
        albumArtBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Album Art",
                modifier = modifier
                    .size(53.dp),
            )
        }
    }
}

@Composable
fun MyPermissionDialog(
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    onDismissRequest: () -> Unit = {},
    onAllowClick: () -> Unit = {},
    onDontAllowClick: () -> Unit = {}
) {
    Log.d("main", "$isVisible")
    if(isVisible)
    {
        Dialog(
            onDismissRequest = onDismissRequest
        ) {
            Card(
                modifier = modifier,
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E1E1E)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 8.dp,
                            end = 8.dp,
                            top = 8.dp,
                            bottom = 16.dp
                        )
                        .wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Storage Permission",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        modifier = Modifier
                            .padding(8.dp)
                            .wrapContentSize(),
                        text = "Apero Music requires storage access to import Local music",
                        color = Color.White,
                        fontSize = 16.sp,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    )
                    {
                        Text(
                            modifier = Modifier
                                .clickable(onClick = onDontAllowClick),
                            text = "Don't Allow",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            modifier = Modifier
                                .clickable(onClick = onAllowClick),
                            text = "Ok",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChoosePlaylistDialog(
    modifier: Modifier = Modifier,
    isVisible: Boolean = false,
    myPlaylist: List<Playlist> = listOf<Playlist>(),
    onDismissRequest: () -> Unit = {},
    onAddPlaylistClick: () -> Unit = {},
    onPlaylistClick: (Playlist) -> Unit = {},
){
    if(isVisible)
    {
        Dialog(
            onDismissRequest = onDismissRequest
        ) {
            Card(
                modifier = modifier,
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF292929)
                )
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(25.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    Text(
                        text = "Choose playlist",
                        fontSize = 20.sp,
                        fontWeight = FontWeight(700),
                        color = Color.White
                    )
                    if(myPlaylist.isEmpty())
                    {
                        Text(
                            modifier = Modifier
                                .padding(
                                    start = 50.dp,
                                    end = 50.dp,
                                    top = 70.dp),
                            text = "You don't have any playlists. Click the “+” button to add",
                            fontSize = 20.sp,
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
                    else
                    {
                        LazyColumn {
                            items(myPlaylist){ playlist ->
                                Playlist(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(
                                            onClick = {onPlaylistClick(playlist)}
                                        ),
                                    playlist = playlist
                                )
                            }
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
    playlist: Playlist
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
                    painter = painterResource(playlist.image),
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
                        text = "${playlist.listMusic.size} songs",
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewLibrary()
{
    ChoosePlaylistDialog(
        modifier = Modifier
            .size(353.dp, 458.dp),
        isVisible = true
    )
}