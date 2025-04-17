package io.github.crazymisterno.GlucoseFit.ui.content

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.NodeClient
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.AndroidEntryPoint
import io.github.crazymisterno.GlucoseFit.data.messaging.MessagingManager
import io.github.crazymisterno.GlucoseFit.data.messaging.MessagingService
import io.github.crazymisterno.GlucoseFit.ui.theme.GlucoseFitMaterialTheme
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var messenger: MessagingManager

    private fun isWatchConnected(context: Context): Boolean {
        val nodeClient: NodeClient = Wearable.getNodeClient(context)
        return try {
            val nodes = Tasks.await(nodeClient.connectedNodes)
            nodes.any { it.isNearby }
        } catch (_: Exception) {
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val serviceIntent = Intent(applicationContext, MessagingService::class.java)
        applicationContext.startForegroundService(serviceIntent)
        Log.i("Message", "Listener was added")
        enableEdgeToEdge()
        setContent {
            Main()
        }
    }
}

@Composable
fun Main() {
    GlucoseFitMaterialTheme {
        ContentView()
    }
}