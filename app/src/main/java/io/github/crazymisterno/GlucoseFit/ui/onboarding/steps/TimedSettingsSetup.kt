package io.github.crazymisterno.GlucoseFit.ui.onboarding.steps

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.toRoute
import io.github.crazymisterno.GlucoseFit.data.storage.DataViewModel
import io.github.crazymisterno.GlucoseFit.ui.content.settings.timed.TimedSettingsList
import io.github.crazymisterno.GlucoseFit.ui.content.settings.timed.dialog.TimedSettingsConfig
import io.github.crazymisterno.GlucoseFit.ui.theme.buttonColors
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object SetupMenu
@Serializable
data class Details(
    val settingId: Int,
    val adding: Boolean
)

@Composable
fun TimedSettingsSetup(db: DataViewModel = hiltViewModel(), moveOn: () -> Unit) {
    var errorAlert by remember { mutableStateOf(false) }
    val navigator = rememberNavController()
    val scope = rememberCoroutineScope()
    val graph = navigator.createGraph(startDestination = SetupMenu) {
        composable<SetupMenu> {
            Column {
                Text(
                    "Let's get your timed ratios set up",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(15.dp)
                )
                TimedSettingsList { id, adding ->
                    navigator.navigate(Details(id, adding))
                }
                Button(
                    onClick = {
                        scope.launch {
                            val list = db.readTimeSettingsOnce()
                            if (list.isNotEmpty())
                                moveOn()
                            else
                                errorAlert = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    colors = buttonColors(),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text("Confirm")
                }
            }
        }

        dialog<Details> { entry ->
            val prop = entry.toRoute<Details>()
            Dialog(
                onDismissRequest = {}
            ) {
                BackHandler {}
                TimedSettingsConfig(prop.settingId) { close ->
                    navigator.popBackStack()
                    if (prop.adding && close) {
                        db.removeTimeSetting(prop.settingId)
                    }
                }
            }
        }
    }
    NavHost(navigator, graph)

    if (errorAlert)
        AlertDialog(
            onDismissRequest = { errorAlert = false },
            confirmButton = {
                TextButton(onClick = { errorAlert = false }) {
                    Text(
                        "Ok",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            title = { Text("No timed settings") },
            text = { Text("Please configure at least one timed setting") },
            containerColor = MaterialTheme.colorScheme.surface,
            textContentColor = MaterialTheme.colorScheme.onSurface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
}