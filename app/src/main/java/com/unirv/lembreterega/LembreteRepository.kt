package com.unirv.lembreterega

import android.content.Context
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

const val TAG_LEMBRETE = "lembrete"

// Toda a parte do WorkManager fica aqui, a tela nao mexe direto nele
class LembreteRepository(context: Context) {

    private val workManager = WorkManager.getInstance(context)

    val lembretes: Flow<List<WorkInfo>> = workManager.getWorkInfosByTagFlow(TAG_LEMBRETE)

    fun agendarLembrete(planta: String, tempo: Long, unidade: TimeUnit) {
        // so roda se a bateria nao estiver baixa
        val restricoes = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val dados = workDataOf(CHAVE_PLANTA to planta)

        val request = OneTimeWorkRequestBuilder<LembreteWorker>()
            .setInitialDelay(tempo, unidade)
            .setConstraints(restricoes)
            .setInputData(dados)
            .addTag(TAG_LEMBRETE)
            .addTag("planta:$planta")
            .build()

        workManager.enqueue(request)
    }

    fun cancelarTodos() {
        workManager.cancelAllWorkByTag(TAG_LEMBRETE)
    }

    fun limparHistorico() {
        workManager.pruneWork()
    }
}
