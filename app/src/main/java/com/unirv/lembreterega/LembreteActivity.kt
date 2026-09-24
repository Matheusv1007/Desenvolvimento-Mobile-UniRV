package com.unirv.lembreterega

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.WorkInfo

class LembreteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TelaLembrete()
                }
            }
        }
    }
}

val plantas = listOf("Samambaia", "Suculenta", "Orquídea", "Cacto", "Espada-de-São-Jorge")

@Composable
fun TelaLembrete(viewModel: LembreteViewModel = viewModel()) {
    val context = LocalContext.current
    var plantaSelecionada by remember { mutableStateOf(plantas[0]) }
    var tempoSelecionado by remember { mutableStateOf(OpcaoTempo.CINCO_SEGUNDOS) }
    val lembretes by viewModel.lembretes.collectAsState(initial = emptyList())

    // pede permissao de notificacao (Android 13+)
    val permissaoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissaoLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Text(
            text = "Lembrete de Rega",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Planta",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )
        plantas.forEach { planta ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = planta == plantaSelecionada,
                        onClick = { plantaSelecionada = planta }
                    )
            ) {
                RadioButton(
                    selected = planta == plantaSelecionada,
                    onClick = { plantaSelecionada = planta }
                )
                Text(text = planta)
            }
        }

        Text(
            text = "Lembrar daqui a",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 12.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            OpcaoTempo.entries.forEach { opcao ->
                FilterChip(
                    selected = opcao == tempoSelecionado,
                    onClick = { tempoSelecionado = opcao },
                    label = { Text(opcao.texto, fontSize = 12.sp) }
                )
            }
        }

        Button(
            onClick = {
                viewModel.agendar(plantaSelecionada, tempoSelecionado)
                Toast.makeText(context, "Lembrete agendado!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("Agendar lembrete")
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 4.dp)
        ) {
            OutlinedButton(onClick = { viewModel.cancelarTodos() }) {
                Text("Cancelar pendentes")
            }
            OutlinedButton(onClick = { viewModel.limparHistorico() }) {
                Text("Limpar histórico")
            }
        }

        Text(
            text = "Lembretes (${lembretes.size})",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
        )
        LazyColumn {
            items(lembretes) { info ->
                ItemLembrete(info)
            }
        }
    }
}

@Composable
fun ItemLembrete(info: WorkInfo) {
    val planta = info.tags
        .firstOrNull { it.startsWith("planta:") }
        ?.removePrefix("planta:") ?: "?"

    val (status, cor) = when (info.state) {
        WorkInfo.State.ENQUEUED -> "Agendado" to Color(0xFF2196F3)
        WorkInfo.State.RUNNING -> "Executando" to Color(0xFFFF9800)
        WorkInfo.State.SUCCEEDED -> "Concluído" to Color(0xFF4CAF50)
        WorkInfo.State.FAILED -> "Falhou" to Color.Red
        WorkInfo.State.BLOCKED -> "Bloqueado" to Color.Gray
        WorkInfo.State.CANCELLED -> "Cancelado" to Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = planta)
            Text(text = status, color = cor, fontWeight = FontWeight.Bold)
        }
    }
}
