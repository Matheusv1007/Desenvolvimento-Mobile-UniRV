package com.unirv.lembreterega

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.work.WorkInfo
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

class LembreteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LembreteRepository(application)

    val lembretes: Flow<List<WorkInfo>> = repository.lembretes

    fun agendar(planta: String, opcao: OpcaoTempo) {
        repository.agendarLembrete(planta, opcao.tempo, opcao.unidade)
    }

    fun cancelarTodos() {
        repository.cancelarTodos()
    }

    fun limparHistorico() {
        repository.limparHistorico()
    }
}

enum class OpcaoTempo(val texto: String, val tempo: Long, val unidade: TimeUnit) {
    CINCO_SEGUNDOS("5 segundos", 5, TimeUnit.SECONDS),
    UM_MINUTO("1 minuto", 1, TimeUnit.MINUTES),
    UMA_HORA("1 hora", 1, TimeUnit.HOURS),
    UM_DIA("1 dia", 1, TimeUnit.DAYS)
}
