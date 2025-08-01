package com.example.vnhai.feature.library

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vnhai.Music

@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier,
    viewModel: LibraryViewModel = LibraryViewModel()
) {

    val state = viewModel.uiState.collectAsState()
    val listMusic = state.value.listMusic
    Screen(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Companion.Black),
        listMusic = listMusic
    )
}

@Composable
fun Screen(
    modifier: Modifier = Modifier,
    listMusic: List<Music> = listOf<Music>(),
)
{
    Row(modifier = modifier){
        HeadLibrary(modifier = modifier
            .height(180.dp)
            .fillMaxWidth()
        )
    }
}

@Composable
fun HeadLibrary(
    modifier: Modifier = Modifier,
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
            Button(
                modifier = Modifier
                    .size(140.dp, 50.dp)
                    .padding(5.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00C2CB)
                ),
                onClick = {}
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
                onClick = {}
            ) {
                Text(
                    text = "Remote",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
        LazyColumn (
            modifier = modifier
        ){

        }
    }
}

@Composable
fun DrawImageFromPath(modifier: Modifier = Modifier, mp3FilePath: String) {
    var albumArtBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

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

    albumArtBitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = "Album Art",
            modifier = modifier
        )
    }
}


@Preview
@Composable
fun PreviewLibrary()
{
    DrawImageFromPath(
        modifier = Modifier.size(40.dp, 40.dp),
        mp3FilePath = "C:/Users/Admin/HaiVN/app/src/main/res/drawable/music1.webp"
    )
}