package com.example.vnhai.feature.signup

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vnhai.R
import com.example.vnhai.User
import com.example.vnhai.feature.signin.MyButton
import com.example.vnhai.feature.signin.MyOutLineTextField

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onSignUpClick: () -> Unit = {},
    viewModel: SignUpViewModel = viewModel(factory = SignUpViewModel.Factory)
) {

    val state = viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Companion.Black)
            .padding(
                top = 44.dp,
                start = 20.dp,
                end = 20.dp
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {
        Image(
            painterResource(R.drawable.logo_music_app),
            contentDescription = "Logo music app",
            modifier = Modifier.Companion.size(278.dp, 248.dp)
        )
        Text(
            text = "Sign Up",
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Companion.Bold
            ),
            color = Color.Companion.White
        )
        MyOutLineTextField(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(top = 24.dp),
            leadingIcon = R.drawable.user_icon,
            isError = state.value.userError,
            value = state.value.username,
            placeholder = "Username",
            onValueChange = {
                viewModel.processIntent(SignUpIntent.EnterUserName(it))
            },
            onFocused = { string, bool ->
                viewModel.processIntent(SignUpIntent.EnterUserName(string))
                viewModel.processIntent(SignUpIntent.ChangeUserError(bool))
            }
        )

        MyOutLineTextField(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(top = 24.dp),
            leadingIcon = R.drawable.password_icon,
            isError = state.value.passwordError,
            password = state.value.passwordVisible,
            value = state.value.password,
            containTrailing = true,
            placeholder = "Password",
            onValueChange = {
                viewModel.processIntent(SignUpIntent.EnterPassword(it))
            },
            onTrailingIconClick = {
                viewModel.processIntent(SignUpIntent.VisiblePassword)
            },
            onFocused = { string, bool ->
                viewModel.processIntent(SignUpIntent.EnterPassword(string))
                viewModel.processIntent(SignUpIntent.ChangePasswordError(bool))
            }
        )

        MyOutLineTextField(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(top = 24.dp),
            leadingIcon = R.drawable.password_icon,
            isError = state.value.confirmError,
            value = state.value.confirm,
            containTrailing = true,
            password = state.value.confirmVisible,
            placeholder = "Confirm password",
            onValueChange = {
                viewModel.processIntent(SignUpIntent.EnterConfirm(it))
            },
            onTrailingIconClick = {
                viewModel.processIntent(SignUpIntent.VisibleConfirm)
            },
            onFocused = { string, bool ->
                viewModel.processIntent(SignUpIntent.EnterConfirm(string))
                viewModel.processIntent(SignUpIntent.ChangeConfirmError(bool))
            }
        )

        MyOutLineTextField(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(top = 24.dp),
            leadingIcon = R.drawable.email_icon,
            isError = state.value.emailError,
            value = state.value.email,
            placeholder = "Email",
            onValueChange = {
                viewModel.processIntent(SignUpIntent.EnterEmail(it))
            },
            onFocused = { string, bool ->
                viewModel.processIntent(SignUpIntent.EnterEmail(string))
                viewModel.processIntent(SignUpIntent.ChangeEmailError(bool))
            }
        )

        Row(
            modifier = modifier
                .fillMaxSize()
                .fillMaxWidth()
                .padding(bottom = 50.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Companion.Bottom
        ) {
            MyButton(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                text = "Sign Up",
                onClick = {
                    viewModel.processIntent(SignUpIntent.SignUp)
                    if(state.value.userError && state.value.passwordError && state.value.confirmError && state.value.emailError)
                    {
                        viewModel.processIntent(SignUpIntent.SaveCurrentUser)
                        Log.d("main", "ahihi")
                        onSignUpClick()
                    }
                })
        }
    }
}