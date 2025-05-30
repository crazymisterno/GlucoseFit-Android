package io.github.crazymisterno.GlucoseFit.ui.content.settings.timed.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.crazymisterno.GlucoseFit.data.settings.TimedSettings
import io.github.crazymisterno.GlucoseFit.ui.theme.buttonColors
import io.github.crazymisterno.GlucoseFit.ui.theme.timePickerColors
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimedSettingsClock(setting: TimedSettings, action: (Int, LocalTime) -> Unit) {
    val focusManager = LocalFocusManager.current
    val interaction = remember { MutableInteractionSource() }
    val time = setting.startTime
    val value = rememberTimePickerState(time.hour, time.minute)
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(interaction, null) {
                focusManager.clearFocus()
            },
    ) {
        Column(
            modifier = Modifier
                .padding(all = 15.dp)
        ) {
            Text(
                "Options",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(20.dp))
            TimePicker(
                state = value,
                colors = timePickerColors(),
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { action(0, LocalTime.of(value.hour, value.minute)) },
                colors = buttonColors(),
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Save",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(5.dp)
                )
            }
            Spacer(Modifier.height(15.dp))
            Button(
                onClick = { action(1, LocalTime.of(value.hour, value.minute)) },
                colors = buttonColors(),
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Cancel",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }
}