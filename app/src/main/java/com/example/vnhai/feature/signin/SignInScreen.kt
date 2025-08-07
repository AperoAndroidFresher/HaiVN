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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vnhai.R

@Composable
fun Loading(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxHeight()
            .background(Color.Black)
            .padding(
                top = 170.dp
            ),
    ) {
        Image(
            painterResource(R.drawable.logo_music_app),
            contentDescription = "Logo music app",
            modifier = Modifier.size(278.dp, 248.dp)
        )

        Text(
            text = "Apero Music",
            style = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color(0xFF427880),
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
        )
    }
}

@Composable
fun SignIn(
    modifier: Modifier = Modifier,
    onSignUpTextClick: () -> Unit = {},
    onSignInButtonClick: () -> Unit = {},
    viewModel: SignInViewModel = viewModel(factory = SignInViewModel.Factory),
)
{
    val state = viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.processIntent(SignInIntent.LoadData(context))
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(
                top = 44.dp,
                start = 20.dp,
                end = 20.dp
            ),
    ) {
        Image(
            painterResource(R.drawable.logo_music_app),
            contentDescription = "Logo music app",
            modifier = Modifier.size(278.dp, 248.dp)
        )

        Text(
            text = "Login to your account",
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color.White
        )

        MyOutLineTextField(
            leadingIcon = R.drawable.user_icon,
            value = state.value.username,
            placeholder = "Username",
            onValueChange = {it ->
                viewModel.processIntent(SignInIntent.EnterUserName(it))
            },
            onFocused = { string, bool -> viewModel.processIntent(SignInIntent.EnterUserName(string)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        )

        MyOutLineTextField(
            leadingIcon = R.drawable.password_icon,
            value = state.value.password,
            containTrailing = true,
            isHidden = state.value.passwordVisible,
            placeholder = "Password",
            onValueChange = {
                viewModel.processIntent(SignInIntent.EnterPassword(it))
            },
            onTrailingIconClick = {
                viewModel.processIntent(SignInIntent.VisiblePassword)
            },
            onFocused = { string, bool -> viewModel.processIntent(SignInIntent.EnterPassword(string))},
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        )

        MyCheckBox(
            isChecked = state.value.checked,
            onCheckedChange = {
                viewModel.processIntent(SignInIntent.CheckRemember)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 20.dp,
                    start = 8.dp
                ),
        )

        MySignInButton(
            text = "Login",
            onClick = {viewModel.processIntent(SignInIntent.SignIn(context, onSignInButtonClick))},
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
        )

        LinkToSignUp(
            onSignUpClick = onSignUpTextClick,
            modifier = modifier
                .fillMaxSize()
                .padding(
                    bottom = 50.dp
                ),
        )
    }

}

@Composable
fun MyOutLineTextField(
    modifier: Modifier = Modifier,
    leadingIcon: Int = 0,
    value: String = "",
    isHidden: Boolean = false,
    placeholder: String = "",
    isError: Boolean = false,
    containTrailing: Boolean = false,
    onTrailingIconClick: () -> Unit = {},
    onValueChange: (String) -> Unit = {},
    onFocused: (String, Boolean) -> Unit = {_, _ ->},
){
    val trailingIcon: Int = if(isHidden)
    {
        R.drawable.eye_icon
    }
    else
    {
        R.drawable.hide_eye_icon
    }
    Column {
        OutlinedTextField(
            shape = RoundedCornerShape(10.dp),
            value = value,
            isError = isError,
            textStyle = TextStyle(
                color = Color.White
            ),
            leadingIcon = {
                Icon(
                    modifier = Modifier
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
            visualTransformation = if (isHidden || !containTrailing) VisualTransformation.None else PasswordVisualTransformation(
                '*'
            ),
            trailingIcon = {
                if (containTrailing) {
                    Icon(
                        painter = painterResource(id = trailingIcon),
                        contentDescription = "Icon",
                        modifier = Modifier
                            .clickable(onClick = onTrailingIconClick)
                            .size(17.dp)
                    )
                }
            },
            onValueChange = onValueChange,
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
        )

        if (isError) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .background(
                        color = Color.Black
                    ),
                text = "Invalid format",
                fontSize = 12.sp,
                fontWeight = FontWeight(400),
                color = Color.Red
            )
        }
    }
}

@Composable
fun MyCheckBox(
    modifier: Modifier = Modifier,
    isChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .size(150.dp, 18.dp),
    ) {
        Checkbox(
            checked = isChecked,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xff00c2cb),
                uncheckedColor = Color(0xff00c2cb),
                checkmarkColor = Color.White
            ),
            onCheckedChange = onCheckedChange
        )
        Text(
            text = "Remember me",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color.White
        )
    }
}

@Composable
fun MySignInButton(
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
            textAlign = TextAlign.Center,
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LinkToSignUp(
    modifier: Modifier,
    onSignUpClick: () -> Unit = {}
){
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = "Don't have an account?",
            fontSize = 16.sp,
            color = Color.White
        )

        Text(
            text = "Sign Up",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xff06A0B5),
            modifier = Modifier
                .clickable(
                    onClick = onSignUpClick
                ),
        )
    }
}