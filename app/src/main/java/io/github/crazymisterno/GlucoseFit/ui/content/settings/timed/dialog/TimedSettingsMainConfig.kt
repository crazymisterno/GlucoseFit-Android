package io.github.crazymisterno.GlucoseFit.ui.content.settings.timed.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.crazymisterno.GlucoseFit.data.settings.TimedSettings
import io.github.crazymisterno.GlucoseFit.ui.theme.buttonColors
import io.github.crazymisterno.GlucoseFit.ui.theme.textFieldColors

@Composable
fun TimedSettingsMainConfig(setting: TimedSettings, action: (String, String, String, Boolean) -> Unit) {
    val focusManager = LocalFocusManager.current
    var ratioValue by remember { mutableStateOf(TextFieldValue(setting.insulinToCarbRatio)) }
    var correctionValue by remember { mutableStateOf(TextFieldValue(setting.correctionDose)) }
    var targetValue by remember { mutableStateOf(TextFieldValue(setting.targetGlucose)) }
    var errorAlert by remember { mutableStateOf(false) }
    val interaction = remember { MutableInteractionSource() }

    Box(
        Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(interaction, null) {
                focusManager.clearFocus()
            }
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    "Settings for profile",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(5.dp))
                TextField(
                    value = ratioValue,
                    onValueChange = { ratioValue = it },
                    label = { Text("Insulin to Carb Ratio") },
                    colors = textFieldColors(),
                    modifier = Modifier.fillMaxWidth(),
                    prefix = { Text("1:") },
                    isError = ratioValue.text.toDoubleOrNull() == null && ratioValue.text.isNotEmpty()
                )
                TextField(
                    value = correctionValue,
                    onValueChange = { correctionValue = it },
                    label = { Text("Correction Dose") },
                    colors = textFieldColors(),
                    modifier = Modifier.fillMaxWidth(),
                    prefix = { Text("1:") },
                    isError = correctionValue.text.toDoubleOrNull() == null && correctionValue.text.isNotEmpty()
                )
                TextField(
                    value = targetValue,
                    onValueChange = { targetValue = it },
                    label = { Text("Target Glucose") },
                    colors = textFieldColors(),
                    modifier = Modifier.fillMaxWidth(),
                    suffix = { Text("mg/dL") },
                    isError = targetValue.text.toDoubleOrNull() == null && targetValue.text.isNotEmpty()
                )
            }
            Button(
                onClick = {
                    if (
                        ratioValue.text.toDoubleOrNull() == null ||
                        correctionValue.text.toDoubleOrNull() == null ||
                        targetValue.text.toDoubleOrNull() == null
                    ) errorAlert = true
                    else
                        action(ratioValue.text, correctionValue.text, targetValue.text, false)
                },
                colors = buttonColors(),
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Save",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 15.dp)
                )
            }
            Button(
                onClick = {
                    action(
                        ratioValue.text,
                        correctionValue.text,
                        targetValue.text,
                        true
                    )
                },
                colors = buttonColors(),
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Cancel",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 15.dp)
                )
            }
            if (errorAlert)
                AlertDialog(
                    onDismissRequest = { errorAlert = false },
                    confirmButton = {
                        TextButton(onClick = {errorAlert = false }) {
                            Text(
                                "Ok",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    title = { Text("Invalid settings") },
                    text = {
                        Column {
                            if (ratioValue.text.toDoubleOrNull() == null)
                                Text("Insulin to Carb Ratio must be a valid number")
                            if (correctionValue.text.toDoubleOrNull() == null)
                                Text("Correction dose must be a valid number")
                            if (targetValue.text.toDoubleOrNull() == null)
                                Text("Target glucose must be a valid number")
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    textContentColor = MaterialTheme.colorScheme.onSurface
                )
        }
    }
}