package io.github.crazymisterno.GlucoseFit.ui.content.settings

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.toRoute
import io.github.crazymisterno.GlucoseFit.data.storage.DataViewModel
import io.github.crazymisterno.GlucoseFit.ui.content.settings.timed.dialog.TimedSettingsConfig
import kotlinx.serialization.Serializable

@Serializable
object MainMenu
@Serializable
data class TimedConfig(
    val id: Int,
    val adding: Boolean
)

@Composable
fun SettingsNavigator(db: DataViewModel = hiltViewModel()) {
    val navigator = rememberNavController()
    val graph = navigator.createGraph(MainMenu) {
        composable<MainMenu> {
            SettingsView { id, adding ->
                navigator.navigate(TimedConfig(id, adding))
            }
        }

        dialog<TimedConfig> { entry ->
            val prop = entry.toRoute<TimedConfig>()
            Dialog(
                onDismissRequest = {}
            ) {
                BackHandler {}
                TimedSettingsConfig(prop.id) { close ->
                    navigator.popBackStack()
                    if (prop.adding && close) {
                        db.removeTimeSetting(prop.id)
                    }
                }
            }
        }
    }

    NavHost(navigator, graph)
}
