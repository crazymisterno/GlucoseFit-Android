package io.github.crazymisterno.GlucoseFit.ui.content.settings.timed.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.crazymisterno.GlucoseFit.data.settings.TimedSettings
import io.github.crazymisterno.GlucoseFit.data.storage.DataViewModel
import java.time.LocalTime


@Composable
fun TimedSettingsConfig(id: Int, db: DataViewModel = hiltViewModel(), close: (Boolean) -> Unit) {
    val setting: TimedSettings? by remember { db.getTimeSetting(id) }.collectAsState()
    var time by remember { mutableStateOf(LocalTime.now()) }
    var stage by remember { mutableIntStateOf(0) }


    setting?.let { it1 ->
        when (stage) {
            0 -> TimedSettingsClock(it1) { action, newTime ->
                if (action == 0) {
                    time = newTime
                    stage = 1
                }
                else if (action == 1) {
                    close(true)
                }
            }

            1 -> TimedSettingsMainConfig(it1) { ratio, correction, target, cancel ->
                val newSetting = TimedSettings(time, ratio, correction, target, id)
                db.updateSetting(newSetting)
                close(cancel)
            }
        }
    }
}