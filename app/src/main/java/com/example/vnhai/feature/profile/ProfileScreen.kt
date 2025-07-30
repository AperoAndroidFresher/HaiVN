package com.example.vnhai.feature.profile

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.vnhai.R
import com.example.vnhai.feature.signup.SignUpViewModel
import com.example.vnhai.onlyLetters

@Composable
fun Profile(
    modifier: Modifier = Modifier,
    darkTheme: Boolean,
    onThemeIconClick: () -> Unit = {},
    viewModel: ProfileViewModel,
) {
    Screen(darkTheme = darkTheme,
        onThemeIconClick = onThemeIconClick,
        viewModel = viewModel
    )
}

@Composable
fun Screen(
    darkTheme: Boolean,
    onThemeIconClick: () -> Unit,
    viewModel: ProfileViewModel,
) {
    val focusManager = LocalFocusManager.current
    val state = viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.Companion
            .fillMaxSize()
            .clickable {
                focusManager.clearFocus()
            }
            .background(
                color = MaterialTheme.colorScheme.background
            )

    )
    {
        Column(
            modifier = Modifier.Companion.padding(
                top = 50.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            ),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Companion.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.Companion
                    .height(50.dp)
                    .fillMaxWidth()
            )
            {
                ThemeIcon(
                    modifier = Modifier.Companion
                        .size(35.dp)
                        .align(Alignment.Companion.CenterStart),
                    darkTheme = darkTheme,
                    onClick = onThemeIconClick
                )
                Text(
                    text = "MY INFORMATION",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Companion.Bold,
                    modifier = Modifier.Companion
                        .align(Alignment.Companion.Center),
                    color = MaterialTheme.colorScheme.primary
                )
                MyEditIcon(
                    modifier = Modifier.Companion
                        .size(35.dp)
                        .align(Alignment.Companion.CenterEnd),
                    visible = state.value.visibleIcon,
                    onClick = {
                        viewModel.processIntent(ProfileIntent.ChangeVisibleIcon)
                    }
                )
            }

            MyAvatar(
                modifier = Modifier.Companion.size(150.dp),
                enabled = !state.value.visibleIcon
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.Companion
                    .height(90.dp)
            ) {
                val modifier: Modifier = Modifier.Companion
                    .weight(1f)

                MyOutlinedTextField(
                    modifier = modifier,
                    textContent = "NAME",
                    outlineTextFieldContent = state.value.name,
                    placeholder = "Enter your name",
                    enabled = !state.value.visibleIcon,
                    isError = state.value.hasNameError,
                    keyboardOptions = KeyboardOptions.Companion.Default.copy(
                        keyboardType = KeyboardType.Companion.Text,
                        imeAction = ImeAction.Companion.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.processIntent(ProfileIntent.HasNameError(!state.value.name.onlyLetters()))
                            if (!state.value.hasNameError) {
                                focusManager.clearFocus()
                            }
                        }
                    ),
                    onValueChange = {
                        viewModel.processIntent(ProfileIntent.EnterName(it))
                    }
                )

                Spacer(modifier = Modifier.Companion.weight(0.05f))

                MyOutlinedTextField(
                    modifier = modifier,
                    textContent = "PHONE",
                    outlineTextFieldContent = state.value.phone,
                    placeholder = "Enter your phone",
                    enabled = !state.value.visibleIcon,
                    keyboardOptions = KeyboardOptions.Companion.Default.copy(
                        keyboardType = KeyboardType.Companion.Number,
                        imeAction = ImeAction.Companion.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        },
                    ),
                    onValueChange = {
                        viewModel.processIntent(ProfileIntent.EnterPhone(it))
                    }
                )
            }

            MyOutlinedTextField(
                modifier = Modifier.Companion
                    .height(90.dp),
                textContent = "University Name",
                outlineTextFieldContent = state.value.universityName,
                placeholder = "Enter your university name ....",
                enabled = !state.value.visibleIcon,
                isError = state.value.hasUniversityNameError,
                keyboardOptions = KeyboardOptions.Companion.Default.copy(
                    keyboardType = KeyboardType.Companion.Text,
                    imeAction = ImeAction.Companion.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.processIntent(ProfileIntent.HasUniversityNameError(!state.value.universityName.onlyLetters()))
                        if (!state.value.hasUniversityNameError) {
                            focusManager.clearFocus()
                        }
                    }
                ),
                onValueChange = {
                    viewModel.processIntent(ProfileIntent.EnterUniversity(it))
                }
            )

            MyOutlinedTextField(
                modifier = Modifier.Companion
                    .height(150.dp)
                    .align(Alignment.Companion.Start),
                textContent = "DESCRIBE YOURSELF",
                outlineTextFieldContent = state.value.description,
                placeholder = "Enter a description about yourself ....",
                enabled = !state.value.visibleIcon,
                isSingleLine = false,
                keyboardOptions = KeyboardOptions.Companion.Default.copy(
                    keyboardType = KeyboardType.Companion.Text,
                    imeAction = ImeAction.Companion.Done
                ),
                onValueChange = {
                    viewModel.processIntent(ProfileIntent.EnterDescription(it))
                }
            )

            MySubmitButton(
                modifier = Modifier.Companion
                    .height(100.dp),
                visible = !state.value.visibleIcon,
                onClick = {
                    viewModel.processIntent(ProfileIntent.ChangeVisibleDialog(!(state.value.hasNameError && state.value.hasUniversityNameError)))
                }
            )
        }
        MyCustomDialog(
            modifier = Modifier.Companion
                .height(250.dp),
            visible = state.value.visibleDialog,
            onDismissRequest = {
                viewModel.processIntent(ProfileIntent.ChangeVisibleDialog(false))
                viewModel.processIntent(ProfileIntent.ChangeVisibleIcon)
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
                .clickable(
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
                .clickable(
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
                .clickable(
                    onClick = onClick
                ),
            painter = painterResource(R.drawable.dark_theme_icon),
            contentDescription = "dark icon",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun MyAvatar(
    modifier: Modifier = Modifier,
    enabled: Boolean = false
) {
    var link by remember { mutableStateOf("") }
    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                link = uri.toString()
            } else {
                Log.d("Main Activity", "No media selected")
            }
        }

    Box(
        modifier = modifier
            .padding(15.dp)
    ) {
        if (link != "") {
            MyImageFromUri(
                modifier = Modifier.Companion
                    .size(125.dp)
                    .padding(12.dp),
                link
            )
        } else {
            Image(
                modifier = Modifier.Companion
                    .size(125.dp)
                    .padding(bottom = 12.dp),
                painter = painterResource(R.drawable.cat),
                contentDescription = "Avatar"
            )
        }

        if (enabled) {
            Icon(
                modifier = Modifier.Companion
                    .size(30.dp)
                    .align(alignment = Alignment.Companion.BottomCenter)
                    .background(
                        color = MaterialTheme.colorScheme.background.copy(alpha = 0.7f),
                        shape = CircleShape
                    )
                    .padding(5.dp)
                    .clickable(
                        onClick = {
                            // Launch the photo picker and let the user choose only images.
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
                    ),
                painter = painterResource(R.drawable.camera_icon),
                contentDescription = "Camera icon",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun MyImageFromUri(
    modifier: Modifier = Modifier,
    uri: String
) {
    AsyncImage(
        modifier = modifier
            .clip(CircleShape),
        model = ImageRequest.Builder(LocalContext.current)
            .data(uri)
            .size(300)
            .crossfade(true)
            .build(),
        contentDescription = "Avatar",
        contentScale = ContentScale.Companion.Crop,
    )
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
    keyboardOptions: KeyboardOptions = KeyboardOptions.Companion.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Companion.Default,
    color: Color = MaterialTheme.colorScheme.primary,
    onValueChange: (String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
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
                )
            },
            isError = isError,
            singleLine = isSingleLine,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 8.sp
            ),
            onValueChange = onValueChange,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
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
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceTint
            )
        ) {
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
                        color = Color.Companion.White,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                    )
                    .wrapContentSize(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .padding(8.dp)
                        .wrapContentSize(),
                    horizontalAlignment = Alignment.Companion.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        modifier = Modifier.Companion
                            .fillMaxWidth()
                            .weight(3f),
                        painter = painterResource(R.drawable.baseline_check_circle_24),
                        contentDescription = "Checked Icon"
                    )
                    Text(
                        modifier = Modifier.Companion
                            .padding(8.dp)
                            .wrapContentSize(),
                        text = "Success!",
                        color = Color(0xFF4FA563),
                        fontSize = 35.sp,
                    )
                    Text(
                        modifier = Modifier.Companion
                            .padding(8.dp),
                        text = "Your information has been updated!",
                    )
                }
            }
        }
    }
}

@Preview(
    name = "1",
    showBackground = true)
@Composable
fun Preview1(

){
    MyImageFromUri(
        modifier = Modifier.Companion
            .size(125.dp)
            .padding(
                start = 12.dp,
                end = 12.dp,
                bottom = 12.dp),
        uri = "C:/Users/Admin/HaiVN/app/src/main/res/drawable/img.webp"
    )
}