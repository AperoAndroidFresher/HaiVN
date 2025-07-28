
package com.example.vnhai

import com.example.vnhai.view.Profile
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.vnhai.ui.theme.VNHaiTheme
import com.example.vnhai.view.Home
import com.example.vnhai.view.LoadingScreen
import com.example.vnhai.view.MyPlaylist
import com.example.vnhai.view.Playlist
import com.example.vnhai.view.SignInScreen
import com.example.vnhai.view.SignUpScreen
import kotlinx.coroutines.delay

fun String.onlyLetters() = all { it.isLetterOrDigit() }
fun String.isValidateEmail() = endsWith("@apero.vn") &&
        subSequence(0,lastIndexOf("@apero.vn")).all { it.isLowerCase() || it=='_' || it.isDigit() }

fun checkUserName(name: String): Boolean
{
    return listUser.all { user -> user.username != name.lowercase() }
}

sealed interface AppScreen{
    data object SignIn : AppScreen
    data object SignUp : AppScreen
    data object Home : AppScreen
    data object Profile : AppScreen
    data object Playlist : AppScreen
    data object MyPlaylist: AppScreen
}

data class User(
    val username: String,
    val password: String
)

var listUser = mutableListOf(User("hai", "hai@apero.vn"))



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var currentNighMode = isSystemInDarkTheme()
            var darkTheme by remember { mutableStateOf(currentNighMode) }
            VNHaiTheme (darkTheme = darkTheme) {
                App(
                    modifier = Modifier
                        .fillMaxSize(),
                    darkTheme = darkTheme,
                    onThemeIconClick = {
                        darkTheme = !darkTheme
                    }
                )
            }
        }
    }
}

@Composable
fun App(
    modifier: Modifier = Modifier,
    darkTheme: Boolean,
    onThemeIconClick: ()->Unit = {}
){
    var start by remember { mutableStateOf(true) }
    AnimatedContent(
        targetState = start,
        label = "animated content"
    ) { targetCount ->
        when(targetCount)
        {
            true -> {
                LoadingScreen(modifier = Modifier.fillMaxWidth())
                LaunchedEffect(Unit) {
                    delay(2000)
                    start = false
                }
            }
            false ->
            {
                val backStack = remember { mutableStateListOf<AppScreen>(AppScreen.SignIn) }

                NavDisplay(
                    backStack = backStack,
                    onBack = {backStack.removeLastOrNull()},
                    entryProvider = { key ->
                        when(key){
                            AppScreen.SignIn -> NavEntry(key){
                                SignInScreen(
                                    currentUser = listUser.last(),
                                    onSignInButtonClick = {
                                        backStack.add(AppScreen.Home)
                                        backStack.subList(0, backStack.size - 1).clear() },
                                    onSignUpTextClick = {
                                        backStack.add(AppScreen.SignUp)
                                    }
                                )
                            }

                            AppScreen.SignUp -> NavEntry(key){
                                SignUpScreen(
                                    saveCurrentUser = { user ->
                                        listUser.add(user)
                                    },
                                    onSignUpClick = {backStack.add(AppScreen.SignIn)},
                                )
                            }

                            AppScreen.Home -> NavEntry(key){
                                Home(
                                    onProfileIconClick = {backStack.add(AppScreen.Profile)},
                                    onHomeButtonClick = {},
                                    onPlaylistButtonClick = {backStack.add(AppScreen.Playlist)},
                                    onMyPlaylistButtonClick = {backStack.add(AppScreen.MyPlaylist)}
                                )
                            }

                            AppScreen.Profile -> NavEntry(key) {
                                Profile(
                                    darkTheme = darkTheme,
                                    onThemeIconClick = onThemeIconClick
                                )
                            }

                            AppScreen.MyPlaylist -> NavEntry(key){
                                MyPlaylist()
                            }

                            AppScreen.Playlist -> NavEntry(key){
                                Playlist()
                            }
                        }
                    }
                )
            }
        }
    }
}
