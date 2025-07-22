package com.example.vnhai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.vnhai.ui.theme.VNHaiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VNHaiTheme {
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
                        Text(
                            text = "MY INFORMATION",
                            fontSize  = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                        Icon(
                            painter = painterResource(R.drawable.icon),
                            contentDescription = "Edit icon",
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .fillMaxHeight()
                        )
                    }

                    Image(
                        painter = painterResource(R.drawable.cat),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .weight(4f)
                    )

                    Row (
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .weight(2f)
                    ){
                        val modifier: Modifier = Modifier
                            .weight(1f)

                        MyOutlinedTextField(
                            "NAME",
                            "Enter your name ....",
                            modifier = modifier
                        )

                        Spacer(modifier = Modifier.weight(0.2f))

                        MyOutlinedTextField(
                            "PHONE",
                            "Enter your phone ....",
                            modifier = modifier
                        )
                    }

                    MyOutlinedTextField(
                        "UNIVERSITY NAME",
                        "Enter your university name ....",
                        modifier = Modifier
                            .weight(2f)
                            .align(Alignment.Start)
                    )

                    MyOutlinedTextField(
                        "DESCRIBE YOURSELF",
                        "Enter a description about yourself ....",
                        modifier = Modifier
                            .weight(4f)
                            .align(Alignment.Start)
                    )

                    Button(
                        onClick = {},
                        modifier = Modifier
                            .weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2DA4DF)
                        )
                    ){
                        Text(
                            text = "SUBMIT",
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MyOutlinedTextField(
    textContent: String,
    outlineTextFieldContent: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Gray
) {
    Column (
        modifier = modifier
            .padding(8.dp),
    ){
        Text(
            text = textContent,
            modifier = Modifier
        )

        OutlinedTextField(
            value = outlineTextFieldContent,
            textStyle = TextStyle(color = color),
            onValueChange = {},
            shape = RoundedCornerShape(20.dp),
            modifier = modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun MyCustomDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
       onDismissRequest = {onDismissRequest}
    ){
        Image(
            painter = painterResource(R.drawable.baseline_check_circle_24),
            contentDescription = "checked icon"
        )
    }
}


@Preview(
    name = "1",
    showBackground = true)
@Composable
fun GreetingPreview() {
    VNHaiTheme {
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
                Text(
                    text = "MY INFORMATION",
                    fontSize  = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
                Icon(
                    painter = painterResource(R.drawable.icon),
                    contentDescription = "Edit icon",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .fillMaxHeight()
                )
            }

            Image(
                painter = painterResource(R.drawable.cat),
                contentDescription = "Avatar",
                modifier = Modifier
                    .weight(4f)
            )

            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(2f)
            ){
                val modifier: Modifier = Modifier
                    .weight(1f)

                MyOutlinedTextField(
                    "NAME",
                    "Enter your name ....",
                    modifier = modifier
                )

                Spacer(modifier = Modifier.weight(0.2f))

                MyOutlinedTextField(
                    "PHONE",
                    "Enter your phone ....",
                    modifier = modifier
                )
            }

            MyOutlinedTextField(
                "UNIVERSITY NAME",
                "Enter your university name ....",
                modifier = Modifier
                    .weight(2f)
                    .align(Alignment.Start)
            )

            MyOutlinedTextField(
                "DESCRIBE YOURSELF",
                "Enter a description about yourself ....",
                modifier = Modifier
                    .weight(4f)
                    .align(Alignment.Start)
            )

            Button(
                onClick = {},
                modifier = Modifier
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2DA4DF)
                )
            ){
                Text(
                    text = "SUBMIT",
                    color = Color.White,
                )
            }
        }
    }
}

@Preview(
    name = "2",
    showBackground = true)
@Composable
fun GreetingPreview2() {
    VNHaiTheme {
        MyCustomDialog(a())
    }
}



