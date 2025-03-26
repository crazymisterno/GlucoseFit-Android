package io.github.crazymisterno.GlucoseFit

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStoreFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.crazymisterno.GlucoseFit.data.SettingsDataProvider
import io.github.crazymisterno.GlucoseFit.data.SettingsProvider
import io.github.crazymisterno.GlucoseFit.data.SettingsViewModel
import io.github.crazymisterno.GlucoseFit.data.settings
import io.github.crazymisterno.GlucoseFit.ui.theme.GlucoseFitRoot
import io.github.crazymisterno.GlucoseFit.ui.theme.PreviewSettings
import io.github.crazymisterno.GlucoseFit.ui.theme.SettingsView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val provider = SettingsDataProvider(applicationContext.settings)
        val model = SettingsViewModel(provider)
        enableEdgeToEdge()
        setContent {
            GlucoseFitRoot {
                Main(model)
            }
        }
    }
}

@Composable
fun Main(settings: SettingsViewModel) {
    SettingsView(settings)
}

@Preview
@Composable
fun Preview() {
    val model = SettingsViewModel(PreviewSettings())
    Main(model)
}