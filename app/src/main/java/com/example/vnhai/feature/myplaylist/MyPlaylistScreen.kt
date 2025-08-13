package com.example.vnhai.feature.myplaylist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
import com.example.vnhai.data.local.entity.PlaylistWithSongs

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
        isRenameVisible = state.value.isRenameVisible,
        listPlaylistWithSongs = state.value.listPlaylistWithSongs,
        newName = state.value.newPlaylistName,
        onNewNameChange = {viewModel.processIntent(MyPlaylistIntent.EnterNewPlaylistName(it))},
        onChangeCreateVisible = {viewModel.processIntent(MyPlaylistIntent.ChangeCreateNewVisible)},
        onCreateClick = { viewModel.processIntent(MyPlaylistIntent.CreateNewPlaylist(context))},
        onOKClick = { viewModel.processIntent(MyPlaylistIntent.RenamePlaylist(context)) },
        onFocusedNewNameTextField = {viewModel.processIntent(MyPlaylistIntent.EnterNewPlaylistName(it))},
        onChangeRenameVisible = {viewModel.processIntent(MyPlaylistIntent.ChangeRenameVisible)},
        onPlaylistClick = {},
        onKebabMenuClick = {it -> viewModel.processIntent(MyPlaylistIntent.SetCurrentPlaylistWithSongs(it))},
        onRenameDropClick = {viewModel.processIntent(MyPlaylistIntent.ChangeRenameVisible)},
        onDeleteDropClick = {viewModel.processIntent(MyPlaylistIntent.RemovePlaylist)},
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black)
    )
}


@Composable
fun MyPlaylistScreen(
    isCreateNewVisible: Boolean,
    isRenameVisible: Boolean,
    newName: String,
    listPlaylistWithSongs: List<PlaylistWithSongs>,
    modifier: Modifier = Modifier,
    onNewNameChange: (String) -> Unit = {},
    onChangeCreateVisible: ()->Unit = {},
    onCreateClick: ()->Unit = {},
    onOKClick: () -> Unit = {},
    onDeleteDropClick: ()->Unit = {},
    onKebabMenuClick: (PlaylistWithSongs) -> Unit = {},
    onPlaylistClick: (PlaylistWithSongs) -> Unit = {},
    onRenameDropClick: () -> Unit = {},
    onFocusedNewNameTextField: (String) -> Unit = {},
    onChangeRenameVisible: ()->Unit ={}
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
            if(listPlaylistWithSongs.isEmpty())
            {
                EmptyPlaylistScreen(
                    onAddPlaylistClick = onChangeCreateVisible,
                    modifier = modifier
                )
            }
            else
            {
                ListPlaylistScreen(
                    listPlaylistWithSongs = listPlaylistWithSongs,
                    isRenameVisible = isRenameVisible,
                    newName = newName,
                    onPlaylistClick = onPlaylistClick,
                    onKebabMenuClick = onKebabMenuClick,
                    onDeleteDropClick = onDeleteDropClick,
                    onRenameDropClick = onRenameDropClick,
                    onChangeRenameVisible = onChangeRenameVisible,
                    onNewNameChange = onNewNameChange,
                    onOKClick = onOKClick,
                    onFocusedNewNameTextField = onFocusedNewNameTextField,
                    modifier = modifier,
                )
            }

            CreateNewPlaylist(
                newName = newName,
                isVisible = isCreateNewVisible,
                onNewNameChange = onNewNameChange,
                onDismissRequest = onChangeCreateVisible,
                onCancelClick = onChangeCreateVisible,
                onCreateClick = onCreateClick,
                onFocused = onFocusedNewNameTextField
            )
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
fun ListPlaylistScreen(
    listPlaylistWithSongs: List<PlaylistWithSongs>,
    isRenameVisible: Boolean,
    newName: String,
    modifier: Modifier = Modifier,
    onDeleteDropClick: ()->Unit = {},
    onKebabMenuClick: (PlaylistWithSongs) -> Unit = {},
    onPlaylistClick: (PlaylistWithSongs) -> Unit = {},
    onRenameDropClick: () -> Unit = {},
    onChangeRenameVisible: ()->Unit = {},
    onNewNameChange: (String)->Unit = {},
    onOKClick: () -> Unit = {},
    onFocusedNewNameTextField: (String)->Unit = {},
) {
    Box{
        LazyColumn(
            modifier = modifier
        ){
            items(listPlaylistWithSongs) { playlistWithSongs ->
                Playlist(
                    playlistWithSongs = playlistWithSongs,
                    onPlaylistClick = { onPlaylistClick(playlistWithSongs) },
                    onKebabMenuClick = { onKebabMenuClick(playlistWithSongs) },
                    onDeleteDropClick = onDeleteDropClick,
                    onRenameDropClick = onRenameDropClick,
                    modifier = modifier
                        .fillMaxWidth()
                )
            }
        }

        RenamePlaylist(
            isVisible = isRenameVisible,
            newName = newName,
            onNewNameChange = onNewNameChange,
            onDismissRequest = onChangeRenameVisible,
            onCancelClick = onChangeRenameVisible,
            onOKClick = onOKClick,
            onFocused = onFocusedNewNameTextField
        )
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
    onFocused: (String)->Unit = {},
){
    if(isVisible)
    {
        val focusManager = LocalFocusManager.current
        Dialog(
            onDismissRequest = onDismissRequest
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF292929)
                ),
                modifier = modifier
                    .size(353.dp, 216.dp)
                    .focusable(),
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
                        singleLine = true,
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        onValueChange = onNewNameChange,
                        modifier = Modifier
                            .onFocusChanged {
                                if(it.isFocused)
                                {
                                    onFocused("")
                                }
                            }
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
                            onClick = {
                                onCreateClick()
                                focusManager.clearFocus()},
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
fun RenamePlaylist(
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    newName: String = "",
    onNewNameChange: (String)->Unit = {},
    onDismissRequest: ()->Unit = {},
    onOKClick: ()->Unit = {},
    onCancelClick: ()->Unit = {},
    onFocused: (String)->Unit = {},
){
    if(isVisible)
    {
        val focusManager = LocalFocusManager.current

        Dialog(
            onDismissRequest = onDismissRequest
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF292929)
                ),
                modifier = modifier
                    .size(353.dp, 216.dp)
                    .focusable(),
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ){
                    Text(
                        text = "Rename Playlist",
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
                                text = "Give your playlist a new name",
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
                        singleLine = true,
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        onValueChange = onNewNameChange,
                        modifier = Modifier
                            .onFocusChanged {
                                if(it.isFocused)
                                {
                                    onFocused("")
                                }
                            }
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
                            onClick = {
                                onOKClick()
                                focusManager.clearFocus(force = true)},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF292929)
                            )
                        ) {
                            Text(
                                text = "OK",
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
    playlistWithSongs: PlaylistWithSongs,
    modifier: Modifier = Modifier,
    onPlaylistClick: ()->Unit = {},
    onKebabMenuClick: ()->Unit = {},
    onDeleteDropClick: ()->Unit = {},
    onRenameDropClick: ()->Unit = {},
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(
                start = 30.dp,
                end = 30.dp,
                top = 8.dp,
                bottom = 8.dp
            ),
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(onClick = onPlaylistClick)
        ) {
            Image(
                painter = painterResource(R.drawable.music5),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(84.dp)
            )
            Column (
                modifier = Modifier
                    .padding(start = 8.dp)
            ){
                Text(
                    text = playlistWithSongs.playlist.name,
                    fontWeight = FontWeight(700),
                    fontSize = 22.sp,
                    color = Color.White
                )
                Text(
                    text = "${playlistWithSongs.songs.size} songs",
                    fontWeight = FontWeight(700),
                    fontSize = 18.sp,
                    color = Color(0xFF8A9A9D)
                )
            }
        }

        Spacer(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onPlaylistClick)
        )

        KebabPlaylistMenu(
            onKebabMenuClick = onKebabMenuClick,
            onDeleteDropClick = onDeleteDropClick,
            onRenameDropClick = onRenameDropClick,
            modifier = Modifier
                .size(30.dp)
        )
    }
}

@Composable
fun KebabPlaylistMenu(
    modifier: Modifier = Modifier,
    onKebabMenuClick: () -> Unit = {},
    onDeleteDropClick: ()->Unit = {},
    onRenameDropClick: ()->Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .padding(4.dp)
    ){
        IconButton(
            modifier = modifier
                .size(24.dp, 24.dp),
            onClick = {
                expanded = !expanded
                onKebabMenuClick()
            }
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
                text = { Text("Remove Playlist") },
                onClick = {
                    expanded = false
                    onDeleteDropClick() },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.remove_icon),
                        contentDescription = "leading delete icon"
                    )
                }
            )

            DropdownMenuItem(
                text = { Text("Rename") },
                onClick = {
                    expanded = false
                    onRenameDropClick() },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.rename_icon),
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
    Playlist(
        playlistWithSongs = PlaylistWithSongs(PlaylistEntity(name = "ahaha", userId = 0), listOf()),
        modifier = Modifier
            .fillMaxWidth()
    )
}

