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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
                Box()
                {
                    Column (
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        val onValueChange: (String)->Unit = {}
                        val onClick: ()->Unit = {}
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
                                modifier = modifier,
                                onValueChange = onValueChange
                            )

                            Spacer(modifier = Modifier.weight(0.2f))

                            MyOutlinedTextField(
                                "PHONE",
                                "Enter your phone ....",
                                modifier = modifier,
                                onValueChange = onValueChange
                            )
                        }

                        MyOutlinedTextField(
                            "UNIVERSITY NAME",
                            "Enter your university name ....",
                            modifier = Modifier
                                .weight(2f)
                                .align(Alignment.Start),
                            onValueChange = onValueChange
                        )

                        MyOutlinedTextField(
                            "DESCRIBE YOURSELF",
                            "Enter a description about yourself ....",
                            modifier = Modifier
                                .weight(4f)
                                .align(Alignment.Start),
                            onValueChange = onValueChange
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
                    MyCustomDialog {  }
                }
            }
        }
    }
}

@Composable
fun MyEditIcon(
    visible: Boolean,
    onClick: ()->Unit,
    modifier: Modifier
) {
    Icon(
        painter = painterResource(R.drawable.icon),
        contentDescription = "Edit icon",
        modifier = modifier
    )
}


@Composable
fun MySubmitButton(
    visible: Boolean,
    onClick: ()->Unit,
    modifier: Modifier
){
    Button(
        onClick = {},
        modifier = modifier,
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

@Composable
fun MyOutlinedTextField(
    textContent: String,
    outlineTextFieldContent: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Gray,
    onValueChange: (String) -> Unit
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
            onValueChange = onValueChange,
            shape = RoundedCornerShape(20.dp),
            modifier = modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun MyCustomDialog(
    visible: Boolean = false,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
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
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(R.drawable.baseline_check_circle_24),
                    contentDescription = "Checked Icon"
                )
                Text(
                    text = "Success!",
                    color = Color(0xFF4FA563),
                    fontSize = 35.sp,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = "Your information has been updated!",
                    modifier = Modifier.padding(16.dp)
                )
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
    MyCustomDialog(
        modifier = Modifier.width(125.dp)
    ) { }
}


