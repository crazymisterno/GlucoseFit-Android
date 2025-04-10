package io.github.crazymisterno.GlucoseFit.ui.content

import android.text.format.DateFormat
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.crazymisterno.GlucoseFit.data.storage.DataViewModel
import io.github.crazymisterno.GlucoseFit.ui.theme.switchColors
import io.github.crazymisterno.GlucoseFit.ui.theme.textFieldColors
import io.github.crazymisterno.GlucoseFit.ui.theme.timePickerColors
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoseLog(date: LocalDate, db: DataViewModel = hiltViewModel()) {
    Log.println(Log.INFO, "Message", "Recomposing Dose log")
    val focusManager = LocalFocusManager.current
    var doseValue by remember { mutableStateOf(TextFieldValue()) }
    val interaction = remember { MutableInteractionSource() }
    val logs by db.getDoseLogs(date).collectAsState()
    val currentSettings by db.getTimeSetting().collectAsState()
    var usingCustom by remember { mutableStateOf(false) }
    var dropDown by remember { mutableStateOf(false) }
    var timeState = rememberTimePickerState()
    var time by remember { mutableStateOf(LocalTime.now()) }
    val formatter = if (DateFormat.is24HourFormat(LocalContext.current)) {
        DateTimeFormatter.ofPattern("H:mm")
    }
    else {
        DateTimeFormatter.ofPattern("h:mm a")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.horizontalGradient(listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.secondary
            )))
            .navigationBarsPadding()
            .clickable(interaction, null) {
                focusManager.clearFocus()
            }
    ) {
        Text(
            text = "Insulin Dose Log",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally)
        )

        Column(
            Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Text(
                "Current active settings",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(15.dp)
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (currentSettings != null) {
                    Column {
                        Text(
                            "Insulin:carb",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "1:${currentSettings!!.insulinToCarbRatio}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Column {
                        Text(
                            "Correction",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "1:${currentSettings!!.correctionDose}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Column {
                        Text(
                            "Target",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "${currentSettings!!.targetGlucose} mg/dL",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                else {
                    Text(
                        "No setting configured",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Text(
                "Log a new insulin dose",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(15.dp)
            )
            TextField(
                value = doseValue,
                onValueChange = { doseValue = it },
                label = { Text("Enter number of units") },
                colors = textFieldColors(),
                isError = doseValue.text.toDoubleOrNull() == null,
                modifier = Modifier.fillMaxWidth().padding(15.dp)
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Use a different time",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row {
                    Switch(
                        checked = usingCustom,
                        onCheckedChange = {
                            usingCustom = it
                        },
                        colors = switchColors()
                    )
                    if (usingCustom) {
                        Spacer(Modifier.width(10.dp))
                        TextButton(
                            onClick = {
                                dropDown = true
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(15.dp))
                                .background(Color.Blue)
                        ) {
                            Text(
                                time.format(formatter),
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }
            }

            if (dropDown) {
                Dialog(
                    onDismissRequest = { dropDown = false }
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        TimePicker(
                            state = timeState,
                            modifier = Modifier
                                .padding(15.dp),
                            colors = timePickerColors()
                        )
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(15.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(Color.Blue)
                                .clickable {
                                    time = LocalTime.of(timeState.hour, timeState.minute)
                                    dropDown = false
                                }
                        ) {
                            Text(
                                "Ok",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp)
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.Blue)
                    .clickable {
                        if (doseValue.text.toDoubleOrNull() != null) {
                            if (usingCustom) {
                                db.logDose(doseValue.text.toDouble(), date, time)
                            } else {
                                db.logDose(doseValue.text.toDouble(), date)
                            }
                        }
                    }
            ) {
                Text(
                    "Log",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp)
                )
            }
        }

        LazyColumn {
            items(logs, key = { it.id }) {
                it.Display {
                    db.removeDoseLog(it)
                }
            }
        }
    }
}