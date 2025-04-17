package io.github.crazymisterno.GlucoseFit.data.messaging

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.wearable.MessageClient
import dagger.hilt.android.AndroidEntryPoint
import io.github.crazymisterno.GlucoseFit.R
import javax.inject.Inject

@AndroidEntryPoint
class MessagingService: Service() {
    @Inject
    lateinit var messenger: MessagingManager

    @Inject
    lateinit var messageClient: MessageClient

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1, buildNotification())
        messageClient.addListener(messenger)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        messageClient.removeListener(messenger)
    }

    private fun buildNotification(): Notification {
        val channelId = "messaging_channel"

        val channel = NotificationChannel(
            channelId,
            "Messaging Service",
            NotificationManager.IMPORTANCE_LOW
        )
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Messaging Active")
            .setContentText("Waiting for watch messages")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .build()
    }
}