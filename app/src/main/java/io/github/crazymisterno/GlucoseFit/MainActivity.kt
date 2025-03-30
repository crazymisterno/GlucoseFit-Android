package io.github.crazymisterno.GlucoseFit

import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import io.github.crazymisterno.GlucoseFit.data.DataManager
import io.github.crazymisterno.GlucoseFit.data.SettingsDataProvider
import io.github.crazymisterno.GlucoseFit.data.SettingsViewModel
import io.github.crazymisterno.GlucoseFit.data.settings
import io.github.crazymisterno.GlucoseFit.ui.ContentView
import io.github.crazymisterno.GlucoseFit.ui.PreviewSettings
import io.github.crazymisterno.GlucoseFit.ui.SettingsView
import io.github.crazymisterno.GlucoseFit.ui.theme.GlucoseFitMaterialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val provider = SettingsDataProvider(applicationContext.settings)
        val model = SettingsViewModel(provider)
        val db = Room.databaseBuilder(
            applicationContext,
            DataManager::class.javaObjectType,
            "glucosefitdata.db"
        ).build()
        enableEdgeToEdge()
        setContent {
            Main(model, db)
        }
    }
}

@Composable
fun Main(settings: SettingsViewModel, db: DataManager) {
    GlucoseFitMaterialTheme {
        ContentView(settings, db)
    }
}

@Preview
@Composable
fun Preview() {
    val model = SettingsViewModel(PreviewSettings())
    val db = Room.inMemoryDatabaseBuilder(
        LocalContext.current,
        DataManager::class.javaObjectType
    ).build()
    Main(model, db)
}