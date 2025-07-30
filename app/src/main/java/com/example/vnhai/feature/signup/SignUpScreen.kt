package com.example.vnhai.feature.signup

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vnhai.R
import com.example.vnhai.User
import com.example.vnhai.checkUserName
import com.example.vnhai.feature.signin.MyButton
import com.example.vnhai.feature.signin.MyOutLineTextField
import com.example.vnhai.isValidateEmail
import com.example.vnhai.onlyLetters

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    saveCurrentUser: (User)->Unit = {},
    onSignUpClick: () -> Unit = {}
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var userError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var confirmError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }

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
            isError = userError,
            value = username,
            placeholder = "Username",
            onValueChange = {
                username = it
            },
            onFocused = { string, bool -> username = string; userError = bool }
        )

        MyOutLineTextField(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(top = 24.dp),
            leadingIcon = R.drawable.password_icon,
            isError = passwordError,
            password = passwordVisible,
            value = password,
            containTrailing = true,
            placeholder = "Password",
            onValueChange = {
                password = it
            },
            onTrailingIconClick = {
                passwordVisible = !passwordVisible
            },
            onFocused = { string, bool -> password = string; passwordError = bool }
        )

        MyOutLineTextField(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(top = 24.dp),
            leadingIcon = R.drawable.password_icon,
            isError = confirmError,
            value = confirm,
            containTrailing = true,
            password = confirmVisible,
            placeholder = "Confirm password",
            onValueChange = {
                confirm = it
            },
            onTrailingIconClick = {
                confirmVisible = !confirmVisible
            },
            onFocused = { string, bool -> confirm = string; confirmError = bool }
        )

        MyOutLineTextField(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(top = 24.dp),
            leadingIcon = R.drawable.email_icon,
            isError = emailError,
            value = email,
            placeholder = "Email",
            onValueChange = {
                email = it
            },
            onFocused = { string, bool -> email = string; emailError = bool }
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
                    if (!username.onlyLetters()) {
                        userError = true
                    }
                    if (!password.onlyLetters()) {
                        passwordError = true
                    }
                    if (!confirm.onlyLetters() || confirm != password) {
                        confirmError = true
                    }
                    if (!email.isValidateEmail()) {
                        emailError = true
                    }
                    if (!userError && !passwordError && !confirmError && !emailError) {
                        if (checkUserName(username)) {
                            onSignUpClick()
                            saveCurrentUser(User(username, password))
                        } else {
                            userError = true
                        }
                    }
                })
        }
    }
}