package com.example.mobile_example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile_example.ui.theme.Mobile_ExampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Aula()
        }
    }
}

@Composable
fun Aula(){
    Column() {
        Text(
            text = "Ola Alunos da UniRV",
            fontSize = 30.sp,
            modifier = Modifier
                .padding(all = 75.dp)
                .background(color = Color.Red)
        )
        Text(
            text = "Macaquito",
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(all = 15.dp)
                .background(color = Color.Blue)
        )
        Text(
            text = "Kaue otario",
            fontSize = 30.sp,
            modifier = Modifier
                .padding(all = 75.dp)
                .background(color = Color.Yellow)
        )
    }
}