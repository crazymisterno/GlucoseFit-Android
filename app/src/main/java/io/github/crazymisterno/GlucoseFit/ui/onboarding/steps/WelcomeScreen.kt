package io.github.crazymisterno.GlucoseFit.ui.onboarding.steps

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.wearable.Wearable
import io.github.crazymisterno.GlucoseFit.ui.theme.buttonColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Composable
fun WelcomeScreen(context: Context, moveOn: (Boolean) -> Unit) {
    var nodeConnected by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    val nodes = Wearable.getNodeClient(context).connectedNodes.await()
                    nodes.isNotEmpty()
                } catch (_: Exception) {
                    false
                }
            }
            nodeConnected = result
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Welcome to GlucoseFit",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        )
        Button(
            onClick = { moveOn(nodeConnected) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            colors = buttonColors(),
            shape = RoundedCornerShape(15.dp)
        ) {
            Text("Let's Go")
        }
    }
}