package com.unirv.jogodavelha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JogoDaVelha()
        }
    }
}

@Composable
fun JogoDaVelha() {
    var tabuleiro by remember { mutableStateOf(List(9) { "" }) }
    var jogadorAtual by remember { mutableStateOf("X") }
    var vencedor by remember { mutableStateOf("") }

    fun verificarVencedor(): String {
        val combinacoes = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6)
        )
        for ((a, b, c) in combinacoes) {
            if (tabuleiro[a] != "" && tabuleiro[a] == tabuleiro[b] && tabuleiro[a] == tabuleiro[c]) {
                return tabuleiro[a]
            }
        }
        return ""
    }

    fun jogar(index: Int) {
        if (tabuleiro[index] == "" && vencedor == "") {
            tabuleiro = tabuleiro.toMutableList().apply { this[index] = jogadorAtual }

            val resultado = verificarVencedor()
            if (resultado != "") {
                vencedor = resultado
            } else {
                jogadorAtual = if (jogadorAtual == "X") "O" else "X"
            }
        }
    }

    fun reiniciar() {
        tabuleiro = List(9) { "" }
        jogadorAtual = "X"
        vencedor = ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Jogo da Velha",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (vencedor != "") {
            Text(
                text = "Jogador $vencedor venceu!",
                fontSize = 22.sp,
                color = Color(0xFF4CAF50),
                modifier = Modifier.padding(bottom = 12.dp)
            )
        } else if (!tabuleiro.contains("")) {
            Text(
                text = "Empate!",
                fontSize = 22.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        } else {
            Text(
                text = "Vez do jogador: $jogadorAtual",
                fontSize = 22.sp,
                color = if (jogadorAtual == "X") Color(0xFF2196F3) else Color(0xFFFF9800),
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        for (linha in 0..2) {
            Row {
                for (coluna in 0..2) {
                    val index = linha * 3 + coluna
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(4.dp)
                            .background(Color(0xFFE0E0E0))
                            .clickable { jogar(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tabuleiro[index],
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (tabuleiro[index]) {
                                "X" -> Color(0xFF2196F3)
                                "O" -> Color(0xFFFF9800)
                                else -> Color.Black
                            }
                        )
                    }
                }
            }
        }

        Button(
            onClick = { reiniciar() },
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(text = "Novo Jogo")
        }
    }
}