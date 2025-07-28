
package com.example.vnhai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vnhai.ui.theme.VNHaiTheme

fun String.onlyLetters() = all { it.isLetterOrDigit() }
fun String.isValidateEmail() = endsWith("@apero.vn") && all { it.isLowerCase() || it=='_' || it.isDigit() }

enum class AppScreen{
    Loading, SignIn, SignUp
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VNHaiTheme{
                
            }
        }
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(Color.Black)
            .padding(
                top = 170.dp
            ),
        verticalArrangement = Arrangement.Top,
    ){
        Image(
            painterResource(R.drawable.logo_music_app),
            contentDescription = "Logo music app",
            modifier = Modifier.size(278.dp, 248.dp)
        )

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = "Apero Music",
            style = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color(0xFF427880)
        )
    }
}


@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onSignUpClick: () -> Unit = {}
) {
    var username by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}
    var confirm by remember { mutableStateOf("")}
    var email by remember { mutableStateOf("")}

    var userError by remember { mutableStateOf(false)}
    var passwordError by remember { mutableStateOf(false)}
    var confirmError by remember { mutableStateOf(false)}
    var emailError by remember { mutableStateOf(false)}

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(
                top = 44.dp,
                start = 20.dp,
                end = 20.dp
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(R.drawable.logo_music_app),
            contentDescription = "Logo music app",
            modifier = Modifier.size(278.dp, 248.dp)
        )
        Text(
            text = "Sign Up",
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color.White
        )
        MyOutLineTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            leadingIcon = R.drawable.user_icon,
            isError = userError,
            value = username,
            placeholder = "Username",
            onValueChange = {
                username = it
            },
            onFocused = {string, bool -> username = string; userError = bool}
        )

        MyOutLineTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding( top = 24.dp),
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
            onFocused = {string, bool -> password = string; passwordError = bool}
        )

        MyOutLineTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding( top = 24.dp),
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
            onFocused = {string, bool -> confirm = string; confirmError = bool}
        )

        MyOutLineTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding( top = 24.dp),
            leadingIcon = R.drawable.email_icon,
            isError = emailError,
            value = email,
            placeholder = "Email",
            onValueChange = {
                email = it
            },
            onFocused = {string, bool -> email = string; emailError = bool}
        )

        Row (
            modifier = modifier
                .fillMaxSize()
                .fillMaxWidth()
                .padding(bottom = 50.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ){
            MyButton(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                text = "Sign Up",
                onClick = {
                    if(!username.onlyLetters())
                    {
                        userError = true
                    }
                    if(!password.onlyLetters())
                    {
                        passwordError = true
                    }
                    if(!confirm.onlyLetters() || confirm != password)
                    {
                        confirmError = true
                    }
                    if(!email.isValidateEmail())
                    {
                        emailError = true
                    }
                    if(!userError && !passwordError && !confirmError && !emailError)
                    {
                        onSignUpClick()
                    }
                })
        }
    }
}

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onSignUpTextClick: () -> Unit = {}
)
{
    var username by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}
    var checked by remember { mutableStateOf(false)}
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(
                top = 44.dp,
                start = 20.dp,
                end = 20.dp
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            leadingIcon = R.drawable.user_icon,
            value = username,
            placeholder = "Username",
            onValueChange = {
                username = it
            },
            onFocused = {string, bool -> username = string}
        )

        MyOutLineTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding( top = 16.dp),
            leadingIcon = R.drawable.password_icon,
            value = password,
            containTrailing = true,
            password = passwordVisible,
            placeholder = "Password",
            onValueChange = {
                password = it
            },
            onTrailingIconClick = {
                passwordVisible = !passwordVisible
            },
            onFocused = {string, bool -> password = string;}
        )

        MyCheckBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 20.dp,
                    start = 8.dp
                ),
            checked = checked,
            onCheckedChange = {
                checked = it
            }
        )

        MyButton(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            text = "Login",
            onClick = {

            }
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
                ).onFocusChanged{
                    if(it.isFocused)
                    {
                        onFocused("", false)
                    }
                },
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
            visualTransformation = if (password || !containTrailing) VisualTransformation.None else PasswordVisualTransformation('*'),
            trailingIcon = {
                if(containTrailing)
                {
                    Icon(
                        painter = painterResource(id = trailingIcon),
                        contentDescription = "Icon",
                        modifier = Modifier
                            .clickable(onClick = onTrailingIconClick)
                            .size(17.dp)
                    )
                }
            },
            onValueChange = onValueChange
        )

        if (isError)
        {
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
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    Row (
        modifier = modifier
            .size(150.dp, 18.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Checkbox(
            checked = checked,
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
                shape = RoundedCornerShape(50.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xff06A0B5)
        ),
        onClick = onClick
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
    onSignUpClick: () -> Unit
){
    Row (
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ){
        Text(
            text = "Don't have an account?",
            fontSize = 16.sp,
            color = Color.White
        )

        Text(
            modifier = Modifier
                .clickable(
                    onClick = onSignUpClick
                ),
            text = "Sign Up",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xff06A0B5)
        )
    }
}

@Preview(
    name = "1",
    showBackground = true)
@Composable
fun Preview1(

){
    SignUpScreen()
}

