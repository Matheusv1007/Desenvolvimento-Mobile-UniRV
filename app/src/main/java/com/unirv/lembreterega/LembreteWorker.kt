package com.unirv.lembreterega

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

const val CHAVE_PLANTA = "planta"
const val CANAL_ID = "lembrete_rega"

// Classe que o WorkManager executa quando chega a hora do lembrete
class LembreteWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val planta = inputData.getString(CHAVE_PLANTA) ?: return Result.failure()

        Log.d("LembreteWorker", "Executando lembrete da planta: $planta")
        mostrarNotificacao(planta)

        return Result.success()
    }

    private fun mostrarNotificacao(planta: String) {
        // canal de notificacao so existe a partir do Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CANAL_ID,
                "Lembretes de rega",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = applicationContext.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }

        // no Android 13+ precisa da permissao, se o usuario negou nao mostra nada
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val notificacao = NotificationCompat.Builder(applicationContext, CANAL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle("Hora de regar!")
            .setContentText("$planta está precisando de água 🌱")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(planta.hashCode(), notificacao)
    }
}
