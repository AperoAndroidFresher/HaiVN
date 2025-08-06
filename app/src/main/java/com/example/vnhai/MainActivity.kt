package com.example.vnhai

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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
import com.example.vnhai.feature.home.Home
import com.example.vnhai.feature.library.LibraryScreen
import com.example.vnhai.feature.library.LibraryViewModel
import com.example.vnhai.feature.myplaylist.MyPlaylist
import com.example.vnhai.feature.profile.Profile
import com.example.vnhai.feature.profile.ProfileViewModel
import com.example.vnhai.feature.signin.LoadingScreen
import com.example.vnhai.feature.signin.SignInScreen
import com.example.vnhai.feature.signin.SignInViewModel
import com.example.vnhai.feature.signup.SignUpScreen
import com.example.vnhai.feature.signup.SignUpViewModel
import com.example.vnhai.ui.theme.VNHaiTheme
import kotlinx.coroutines.delay

sealed interface AppScreen{
    data object SignIn : AppScreen
    data object SignUp : AppScreen
    data object Home : AppScreen
    data object Profile : AppScreen
    data object Library : AppScreen
    data object MyPlaylist: AppScreen
}


class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
                                    onSignUpClick = {backStack.add(AppScreen.SignIn)},
                                )
                            }

                            AppScreen.Home -> NavEntry(key){
                                Home(
                                    onProfileIconClick = {backStack.add(AppScreen.Profile)},
                                    onHomeButtonClick = {},
                                    onPlaylistButtonClick = {backStack.add(AppScreen.Library)},
                                    onMyPlaylistButtonClick = {backStack.add(AppScreen.MyPlaylist)}
                                )
                            }

                            AppScreen.Profile -> NavEntry(key) {
                                Profile(
                                    darkTheme = darkTheme,
                                    onThemeIconClick = onThemeIconClick,
                                )
                            }

                            AppScreen.MyPlaylist -> NavEntry(key){
                                MyPlaylist()
                            }

                            AppScreen.Library -> NavEntry(key){
                                LibraryScreen(
                                    navigationToPlaylist = {backStack.add(AppScreen.MyPlaylist)}
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

