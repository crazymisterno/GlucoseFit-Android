package io.github.crazymisterno.GlucoseFit.ui.content.settings.timed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.crazymisterno.GlucoseFit.data.settings.TimedSettings
import io.github.crazymisterno.GlucoseFit.data.storage.DataViewModel
import io.github.crazymisterno.GlucoseFit.ui.theme.buttonColors
import kotlinx.coroutines.launch
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimedSettingsList(db: DataViewModel = hiltViewModel(), select: (Int, Boolean) -> Unit) {
    val allSettings by db.getAllTimeSettings().collectAsState()
    val currentSetting by db.getTimeSetting().collectAsState()
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.surfaceBright),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .height(500.dp)
        ) {
            items(allSettings, key = { it.id }) { setting ->
                TimedSection(setting, setting.id == (currentSetting?.id ?: -1)) { action ->
                    if (action == 0) {
                        select(setting.id, false)
                    } else if (action == 1) {
                        db.removeTimeSetting(setting)
                    }
                }
            }
        }
        Button(
            onClick = {
                scope.launch {
                    val setting = TimedSettings(
                        LocalTime.now(),
                        "",
                        "",
                        ""
                    )
                    val id = db.addSettingAsync(setting)
                    select(id.toInt(), true)
                }
            },
            colors = buttonColors(),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.fillMaxWidth().padding(15.dp)
        ) {
            Icon(
                Icons.Filled.AddCircle,
                "Add timed setting",
            )
            Spacer(Modifier.padding(5.dp))
            Text(
                "Add new",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}

