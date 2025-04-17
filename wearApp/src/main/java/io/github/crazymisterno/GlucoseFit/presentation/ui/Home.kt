package io.github.crazymisterno.GlucoseFit.presentation.ui

import android.text.format.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import io.github.crazymisterno.GlucoseFit.presentation.NavTarget
import io.github.crazymisterno.GlucoseFit.presentation.ui.theme.LightBgGreen
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun Home(navigate: (NavTarget) -> Unit) {
    var date = LocalDateTime.now()

    val dateFormat = DateTimeFormatter.ofPattern("MMM d, uuuu")
    val timeFormat = if (DateFormat.is24HourFormat(LocalContext.current))
        DateTimeFormatter.ofPattern("H:mm")
    else
        DateTimeFormatter.ofPattern("h:mm a")

    Column(
        Modifier
            .fillMaxSize()
            .background(Brush.horizontalGradient(listOf(
                MaterialTheme.colors.primary,
                MaterialTheme.colors.secondary
            ))),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            Text(
                date.format(dateFormat),
                style = MaterialTheme.typography.title2,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))
            Text(
                date.format(timeFormat),
                style = MaterialTheme.typography.title2,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { navigate(NavTarget.Select) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = LightBgGreen
                ),
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .width(80.dp)
                    .border(2.dp, Color.Gray, RoundedCornerShape(15.dp))
            ) {
                Text(
                    "Log food",
                    textAlign = TextAlign.Center
                )
            }
            Button(
                onClick = { navigate(NavTarget.Log) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = LightBgGreen
                ),
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .width(80.dp)
                    .border(2.dp, Color.Gray, RoundedCornerShape(15.dp))
            ) {
                Text(
                    "Log Dose",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
