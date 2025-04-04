package io.github.crazymisterno.GlucoseFit.ui.content.settings.timed

import android.text.format.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.github.crazymisterno.GlucoseFit.data.settings.TimedSettings
import java.time.format.DateTimeFormatter

@Composable
fun TimedSection(setting: TimedSettings, action: (Int) -> Unit) {
    val is24Hour = DateFormat.is24HourFormat(LocalContext.current)
    val format = if (is24Hour) {
        DateTimeFormatter.ofPattern("H:mm")
    }
    else {
        DateTimeFormatter.ofPattern("h:mm a")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable {
                action(0)
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Time: ${setting.startTime.format(format)}",
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 15.dp).padding(horizontal = 15.dp)
            )
            Text(
                "Ratio: 1:${setting.insulinToCarbRatio}",
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 15.dp)
            )
            Text(
                "Correction: 1:${setting.correctionDose}",
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 15.dp)
            )
            Text(
                "Target Glucose: ${setting.targetGlucose}",
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 15.dp).padding(bottom = 15.dp)
            )
        }
        Icon(
            Icons.Filled.Delete,
            contentDescription = "Delete",
            tint = Color.Red,
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .clickable {
                    action(1)
                }
        )
    }
}