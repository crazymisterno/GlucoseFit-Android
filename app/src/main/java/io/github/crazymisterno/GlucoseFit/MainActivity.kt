package io.github.crazymisterno.GlucoseFit

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import io.github.crazymisterno.GlucoseFit.ui.theme.GlucoseFitRoot
import io.github.crazymisterno.GlucoseFit.ui.theme.SettingsView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GlucoseFitRoot {
                Main(applicationContext)
            }
        }
    }
}

@Composable
fun Main(context: Context) {
    SettingsView(context)
}

@Preview
@Composable
fun Preview() {
    val localContext = LocalContext.current
    Main(localContext)
}