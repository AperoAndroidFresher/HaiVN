package com.example.vnhai.feature.signup

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vnhai.R
import com.example.vnhai.feature.signin.MyOutLineTextField

@Composable
fun SignUp(
    modifier: Modifier = Modifier,
    onSignUpClick: () -> Unit = {},
    viewModel: SignUpViewModel = viewModel(factory = SignUpViewModel.Factory)
) {

    val context = LocalContext.current
    val state = viewModel.uiState.collectAsState()

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Companion.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(Color.Companion.Black)
            .padding(
                top = 44.dp,
                start = 20.dp,
                end = 20.dp
            ),
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
            },
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(top = 24.dp),
        )

        MyOutLineTextField(
            leadingIcon = R.drawable.password_icon,
            isError = state.value.passwordError,
            isHidden = state.value.passwordVisible,
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
            },
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(top = 24.dp),
        )

        MyOutLineTextField(
            leadingIcon = R.drawable.password_icon,
            isError = state.value.confirmError,
            value = state.value.confirm,
            containTrailing = true,
            isHidden = state.value.confirmVisible,
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
            },
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(top = 24.dp),
        )

        MyOutLineTextField(
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
            },
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(top = 24.dp),
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Companion.Bottom,
            modifier = modifier
                .fillMaxSize()
                .fillMaxWidth()
                .padding(bottom = 50.dp),
        ) {
            MySignUpButton(
                text = "Sign Up",
                onClick = {viewModel.processIntent(SignUpIntent.SignUp(context, onSignUpClick))},
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
            )
        }
    }
}

@Composable
fun MySignUpButton(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {}
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xff06A0B5)
        ),
        onClick = onClick,
        modifier = modifier
            .height(59.dp)
            .fillMaxWidth()
            .background(
                color = Color(0xff06A0B5),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(50.dp)
            ),
    ) {
        Text(
            textAlign = TextAlign.Companion.Center,
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Companion.Bold
        )
    }
}
