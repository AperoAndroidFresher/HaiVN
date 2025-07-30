package com.example.vnhai.feature.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vnhai.R

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(Color.Companion.Black)
            .padding(
                top = 170.dp
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
            modifier = Modifier.Companion
                .align(Alignment.Companion.CenterHorizontally),
            text = "Apero Music",
            style = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight.Companion.Bold
            ),
            color = Color(0xFF427880)
        )
    }
}

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    //currentUser: User = User("", ""),
    onSignUpTextClick: () -> Unit = {},
    onSignInButtonClick: () -> Unit = {},
    viewModel: SignInViewModel,
)
{

//    var username by remember { mutableStateOf(currentUser.username) }
//    var password by remember { mutableStateOf(currentUser.password) }
//    var checked by remember { mutableStateOf(false) }
//    var passwordVisible by remember { mutableStateOf(false) }

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
            text = "Login to your account",
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Companion.Bold
            ),
            color = Color.Companion.White
        )
        MyOutLineTextField(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(top = 16.dp),
            leadingIcon = R.drawable.user_icon,
            value = state.value.username,
            placeholder = "Username",
            onValueChange = {it ->
                viewModel.processIntent(SignInIntent.EnterUserName(it))
            },
            onFocused = { string, bool -> viewModel.processIntent(SignInIntent.EnterUserName(string)) }
        )

        MyOutLineTextField(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(top = 16.dp),
            leadingIcon = R.drawable.password_icon,
            value = state.value.password,
            containTrailing = true,
            password = state.value.passwordVisible,
            placeholder = "Password",
            onValueChange = {
                viewModel.processIntent(SignInIntent.EnterPassword(it))
            },
            onTrailingIconClick = {
                viewModel.processIntent(SignInIntent.VisiblePassword)
            },
            onFocused = { string, bool -> viewModel.processIntent(SignInIntent.EnterPassword(string))}
        )

        MyCheckBox(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(
                    top = 20.dp,
                    start = 8.dp
                ),
            checked = state.value.checked,
            onCheckedChange = {
                viewModel.processIntent(SignInIntent.CheckRemember)
            }
        )

        MyButton(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            text = "Login",
            onClick = onSignInButtonClick
        )

        LinkToSignUp(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    bottom = 50.dp
                ),
            onSignUpClick = onSignUpTextClick
        )
    }

}

@Composable
fun MyOutLineTextField(
    modifier: Modifier = Modifier,
    leadingIcon: Int = 0,
    value: String = "",
    password: Boolean = false,
    placeholder: String = "",
    isError: Boolean = false,
    containTrailing: Boolean = false,
    onTrailingIconClick: () -> Unit = {},
    onValueChange: (String) -> Unit = {},
    onFocused: (String, Boolean) -> Unit = {_, _ ->},
){
    val trailingIcon: Int = if(password)
    {
        R.drawable.eye_icon
    }
    else
    {
        R.drawable.hide_eye_icon
    }
    Column {
        OutlinedTextField(
            modifier = modifier
                .height(59.dp)
                .background(
                    color = Color(0xff1e1e1e),
                    shape = RoundedCornerShape(10.dp)
                ).onFocusChanged {
                    if (it.isFocused) {
                        onFocused("", false)
                    }
                },
            shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp),
            value = value,
            isError = isError,
            textStyle = TextStyle(
                color = Color.Companion.White
            ),
            leadingIcon = {
                Icon(
                    modifier = Modifier.Companion
                        .size(12.dp),
                    painter = painterResource(id = leadingIcon),
                    contentDescription = "Icon",
                )
            },

            placeholder = {
                Text(
                    text = placeholder,
                    fontWeight = FontWeight(400),
                    fontSize = 16.sp,
                )
            },
            visualTransformation = if (password || !containTrailing) VisualTransformation.Companion.None else PasswordVisualTransformation(
                '*'
            ),
            trailingIcon = {
                if (containTrailing) {
                    Icon(
                        painter = painterResource(id = trailingIcon),
                        contentDescription = "Icon",
                        modifier = Modifier.Companion
                            .clickable(onClick = onTrailingIconClick)
                            .size(17.dp)
                    )
                }
            },
            onValueChange = onValueChange
        )

        if (isError) {
            Text(
                modifier = Modifier.Companion
                    .padding(start = 16.dp)
                    .background(
                        color = Color.Companion.Black
                    ),
                text = "Invalid format",
                fontSize = 12.sp,
                fontWeight = FontWeight(400),
                color = Color.Companion.Red
            )
        }
    }
}

@Composable
fun MyCheckBox(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    Row(
        modifier = modifier
            .size(150.dp, 18.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xff00c2cb),
                uncheckedColor = Color(0xff00c2cb),
                checkmarkColor = Color.Companion.White
            ),
            onCheckedChange = onCheckedChange
        )
        Text(
            text = "Remember me",
            fontWeight = FontWeight.Companion.Bold,
            fontSize = 12.sp,
            color = Color.Companion.White
        )
    }
}

@Composable
fun MyButton(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier
            .height(59.dp)
            .fillMaxWidth()
            .background(
                color = Color(0xff06A0B5),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(50.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xff06A0B5)
        ),
        onClick = onClick
    ) {
        Text(
            textAlign = TextAlign.Companion.Center,
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Companion.Bold
        )
    }
}

@Composable
fun LinkToSignUp(
    modifier: Modifier,
    onSignUpClick: () -> Unit
){
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Companion.Bottom
    ) {
        Text(
            text = "Don't have an account?",
            fontSize = 16.sp,
            color = Color.Companion.White
        )

        Text(
            modifier = Modifier.Companion
                .clickable(
                    onClick = onSignUpClick
                ),
            text = "Sign Up",
            fontSize = 16.sp,
            fontWeight = FontWeight.Companion.Bold,
            color = Color(0xff06A0B5)
        )
    }
}