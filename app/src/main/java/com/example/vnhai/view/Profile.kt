package com.example.vnhai.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import com.example.vnhai.R
import com.example.vnhai.onlyLetters


@Composable
fun Profile(
    modifier: Modifier = Modifier,
    darkTheme: Boolean,
    onThemeIconClick: () -> Unit = {})
{
    Screen(darkTheme = darkTheme,
        onThemeIconClick = onThemeIconClick
    )
}

@Composable
fun Screen(
    darkTheme: Boolean,
    onThemeIconClick: () -> Unit
)
{
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var universityName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var visibleIcon by remember { mutableStateOf(true) }
    var visibleDialog by remember { mutableStateOf(false) }
    var hasNameError by remember { mutableStateOf(false) }
    var hasUniversityNameError by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                focusManager.clearFocus()
            }
            .background(
                color = MaterialTheme.colorScheme.background
            )

    )
    {
        Column (
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
            {
                ThemeIcon(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .fillMaxHeight(),
                    darkTheme = darkTheme,
                    onClick = onThemeIconClick
                )
                Text(
                    text = "MY INFORMATION",
                    fontSize  = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
                MyEditIcon(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .fillMaxHeight(),
                    visible = visibleIcon,
                    onClick = {
                        visibleIcon = false
                    }
                )
            }

            Image(
                painter = painterResource(R.drawable.cat),
                contentDescription = "Avatar",
                modifier = Modifier
                    .weight(3f)
            )

            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(1.5f)
            ){
                val modifier: Modifier = Modifier
                    .weight(1f)

                MyOutlinedTextField(
                    modifier = modifier,
                    textContent = "NAME",
                    outlineTextFieldContent = name,
                    placeholder = "Enter your name...",
                    enabled = !visibleIcon,
                    isError = hasNameError,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            hasNameError = !name.onlyLetters()
                            if(!hasNameError)
                            {
                                focusManager.clearFocus()
                            }
                        }
                    ),
                    onValueChange = {
                        name = it
                    }
                )

                Spacer(modifier = Modifier.weight(0.1f))

                MyOutlinedTextField(
                    modifier = modifier,
                    textContent = "PHONE",
                    outlineTextFieldContent = phone,
                    placeholder = "Enter your phone ....",
                    enabled = !visibleIcon,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        },
                    ),
                    onValueChange = {
                        phone = it
                    }
                )
            }

            MyOutlinedTextField(
                modifier = Modifier
                    .weight(1.5f),
                textContent = "University Name",
                outlineTextFieldContent = universityName,
                placeholder = "Enter your university name ....",
                enabled = !visibleIcon,
                isError = hasUniversityNameError,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        hasUniversityNameError = !universityName.onlyLetters()
                        if(hasUniversityNameError)
                        {
                            focusManager.clearFocus()
                        }
                    }
                ),
                onValueChange = {
                    universityName = it
                }
            )

            MyOutlinedTextField(
                modifier = Modifier
                    .weight(4f)
                    .align(Alignment.Start),
                textContent = "DESCRIBE YOURSELF",
                outlineTextFieldContent = description,
                placeholder = "Enter a description about yourself ....",
                enabled = !visibleIcon,
                isSingleLine = false,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                onValueChange = {
                    description = it
                }
            )

            MySubmitButton(
                modifier = Modifier
                    .weight(1f),
                visible = !visibleIcon,
                onClick = {
                    visibleDialog = !(hasNameError && hasUniversityNameError)
                }
            )
        }
        MyCustomDialog(
            modifier = Modifier
                .height(338.dp),
            visible = visibleDialog,
            onDismissRequest = {
                visibleDialog = false
                visibleIcon = true
            }
        )
    }
}

@Composable
fun MyEditIcon(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    onClick: ()->Unit = {}
) {
    if (visible)
    {
        Icon(
            modifier = modifier
                .clickable (
                    onClick = onClick
                ),
            painter = painterResource(R.drawable.icon),
            contentDescription = "Edit icon",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ThemeIcon(
    modifier: Modifier = Modifier,
    darkTheme: Boolean = false,
    onClick: ()->Unit = {}
) {
    if (darkTheme)
    {
        Icon(
            modifier = modifier
                .clickable (
                    onClick = onClick
                ),
            painter = painterResource(R.drawable.light_theme_icon),
            contentDescription = "light icon",
            tint = MaterialTheme.colorScheme.primary
        )
    }
    else
    {
        Icon(
            modifier = modifier
                .clickable (
                    onClick = onClick
                ),
            painter = painterResource(R.drawable.dark_theme_icon),
            contentDescription = "dark icon",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun MySubmitButton(
    modifier: Modifier = Modifier,
    visible: Boolean = false,
    onClick: ()->Unit = {}
){
    if (visible)
    {
        Button(
            onClick = onClick,
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceTint
            )
        ){
            Text(
                text = "SUBMIT",
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
    else
    {
        Spacer(modifier = modifier)
    }
}

@Composable
fun MyOutlinedTextField(
    modifier: Modifier = Modifier,
    textContent: String = "",
    outlineTextFieldContent: String = "",
    placeholder: String = "",
    enabled: Boolean = false,
    isError: Boolean = false,
    isSingleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    color: Color = MaterialTheme.colorScheme.primary,
    onValueChange: (String) -> Unit = {}
) {
    Column (
        modifier = modifier
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ){
        Text(
            text = textContent,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.onSecondary,
                    shape = RoundedCornerShape(20.dp)
                ),
            value = outlineTextFieldContent,
            enabled = enabled,
            placeholder = {
                Text(
                    text = placeholder,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                ) },
            isError = isError,
            singleLine = isSingleLine,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            textStyle = TextStyle(color = color),
            onValueChange = onValueChange,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun MyCustomDialog(
    modifier: Modifier = Modifier,
    visible: Boolean = false,
    onDismissRequest: () -> Unit
) {
    if (visible)
    {
        Dialog(
            onDismissRequest = onDismissRequest
        ) {
            Card(
                modifier = modifier
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .wrapContentSize(),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(3f),
                        painter = painterResource(R.drawable.baseline_check_circle_24),
                        contentDescription = "Checked Icon"
                    )
                    Text(
                        modifier = Modifier
                            .padding(8.dp)
                            .wrapContentSize(),
                        text = "Success!",
                        color = Color(0xFF4FA563),
                        fontSize = 35.sp,
                    )
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = "Your information has been updated!",
                    )
                }
            }
        }
    }
}
