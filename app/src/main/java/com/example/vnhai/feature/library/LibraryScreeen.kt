package com.example.vnhai.feature.library

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Build
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.vnhai.R
import com.example.vnhai.RemoteState
import com.example.vnhai.data.local.entity.SongEntity
import com.example.vnhai.data.local.entity.PlaylistEntity
import com.example.vnhai.data.remote.model.MusicFRemote

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier,
    viewModel: LibraryViewModel = viewModel(factory = LibraryViewModel.Factory),
    navigationToPlaylist: () -> Unit,
) {
    val state = viewModel.uiState.collectAsState()

    val context = LocalContext.current

    //Kiem tra va yeu cau quyen truy cap khi lan dau chuyen vao local.
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
            onRemoteClick = {
                viewModel.processIntent(LibraryIntent.ChangeDirection(false))
                viewModel.processIntent(LibraryIntent.GetRemoteListMusic(context))
            }
        )
        if(state.value.isLocal)
        {
            if(state.value.hasPermission)
            {
                LaunchedEffect(Unit) {
                    viewModel.processIntent(LibraryIntent.GetLocalListMusic(context))
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
                        requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
                        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)},
                    onDontAllowClick = {viewModel.processIntent(LibraryIntent.ChangeVisiblePermissionDialog(false))},
                )
            }
        }

        else{
            RemoteScreen(
                remoteState = state.value.remoteState,
                onTryAgainClick = {
                    viewModel.processIntent(LibraryIntent.GetRemoteListMusic(context))
                },
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black),
            )
        }
    }
}

@Composable
fun LocalScreen(
    modifier: Modifier = Modifier,
    listMusic: List<SongEntity> = listOf(),
    onAddToPlaylistClick: (SongEntity) -> Unit = {}
)
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

@Composable
fun RemoteScreen(
    remoteState: RemoteState,
    modifier: Modifier = Modifier,
    listMusic: List<MusicFRemote> = listOf(),
    onAddToPlaylistClick: (MusicFRemote) -> Unit = {},
    onTryAgainClick: () -> Unit = {},
){
    when(remoteState)
    {
        RemoteState.Loading -> {
            LibraryLoadingScreen(modifier = modifier)
        }
        RemoteState.Error -> {
            LibraryErrorScreen(
                modifier = modifier,
                onTryAgainClick = onTryAgainClick
            )
        }
        RemoteState.Success -> {
            LibrarySuccessScreen(
                modifier = modifier,
                )
        }
    }
}

@Composable
fun HeadLibrary(
    modifier: Modifier = Modifier,
    isLocal: Boolean = true,
    onLocalClick:()->Unit = {},
    onRemoteClick:()->Unit = {}
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(top = 50.dp),
    ) {
        Text(
            text = "Library",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .height(30.dp),
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(70.dp)
                .fillMaxWidth(),
        ){
            Button(
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if(isLocal) {
                        Color(0xFF00C2CB)
                    } else {
                        Color(0xFF1E1E1E)
                    }
                ),
                onClick = onLocalClick,
                modifier = Modifier
                    .size(140.dp, 50.dp)
                    .padding(5.dp),
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
                    containerColor = if(!isLocal) {
                        Color(0xFF00C2CB)
                    } else {
                        Color(0xFF1E1E1E)
                    }
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

@Composable
fun ColumnMusicLayout(
    music: SongEntity,
    modifier: Modifier = Modifier,
    onAddToPlaylistClick: (SongEntity) -> Unit = {},
    onShareClick: (SongEntity) -> Unit = {}
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(8.dp),
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
            music = music,
            onAddToPlaylistClick = onAddToPlaylistClick,
            onShareClick = onShareClick,
            modifier = Modifier
                .size(25.dp),
        )
    }
}

@Composable
fun KebabMenu(
    music: SongEntity,
    modifier: Modifier = Modifier,
    onAddToPlaylistClick: (SongEntity)->Unit = {},
    onShareClick: (SongEntity)->Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .padding(4.dp)
    ) {
        IconButton(
            onClick = { expanded = !expanded },
            modifier = modifier
                .size(24.dp, 24.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.kebab_menu),
                contentDescription = "Delete Icon",
                tint = Color.White,
                modifier = modifier
                    .size(20.dp, 20.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.7f),
                        shape = CircleShape
                    ),
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
        if(albumArtBitmap != null){
            Image(
                bitmap = albumArtBitmap!!.asImageBitmap(),
                contentDescription = "Album Art",
                modifier = modifier
                    .size(53.dp),
            )
        }
        else
        {
            Box(modifier = Modifier
                .size(53.dp)
                .background(color = Color.White,
                    shape = RoundedCornerShape(10.dp)))
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
    myPlaylist: List<PlaylistEntity> = listOf<PlaylistEntity>(),
    onDismissRequest: () -> Unit = {},
    onAddPlaylistClick: () -> Unit = {},
    onPlaylistClick: (PlaylistEntity) -> Unit = {},
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
                                            onClick = { onPlaylistClick(playlist) }
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
    playlist: PlaylistEntity
){
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(8.dp),
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier,
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
                        text = " songs",
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun LibraryLottieAnimation(
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_remote_item_loading))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,)
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier,

    )
}

@Composable
fun LibraryLoadingScreen(modifier: Modifier = Modifier)
{
    LibraryLottieAnimation(modifier = modifier)
}

@Composable
fun LibraryErrorScreen(
    modifier: Modifier = Modifier,
    onTryAgainClick: ()->Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(
                start = 60.dp,
                end = 60.dp
            ),
    ) {
        Image(
            painter = painterResource(R.drawable.error_network_icon),
            contentDescription = "Error network Icon",
            modifier = Modifier
                .size(110.dp, 110.dp)
                .padding(10.dp)
        )

        Text(
            text = "No internet connection, please check your connection again",
            fontSize = 20.sp,
            fontWeight = FontWeight(400),
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(10.dp)
        )

        Button(
            onClick = onTryAgainClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00C2CB)
            ),
            shape = RoundedCornerShape(16),
            modifier = Modifier
                .size(150.dp, 72.dp)
                .background(
                    color = Color.Black,
                    shape = RoundedCornerShape(16)
                )
                .padding(10.dp)
        ) {
            Text(
                text = "Try again",
                fontSize = 18.sp,
                fontWeight = FontWeight(400),
                modifier = Modifier
            )
        }
    }
}

@Composable
fun LibrarySuccessScreen(
    modifier: Modifier = Modifier,
    listMusic: List<SongEntity> = listOf(),
    onAddToPlaylistClick: (SongEntity) -> Unit = {}
) {
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

@Preview
@Composable
fun PreviewLibrary()
{
    LibraryErrorScreen(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    )
}