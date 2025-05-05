package io.github.crazymisterno.GlucoseFit.ui.onboarding.steps

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.crazymisterno.GlucoseFit.data.settings.SettingsViewModel
import io.github.crazymisterno.GlucoseFit.ui.theme.buttonColors
import io.github.crazymisterno.GlucoseFit.ui.theme.switchColors
import io.github.crazymisterno.GlucoseFit.ui.theme.textFieldColors

@Composable
fun InitialSetup(settings: SettingsViewModel = hiltViewModel(), moveOn: () -> Unit) {
    val settingsState by settings.settings.collectAsState()
    val interaction = remember { MutableInteractionSource() }
    var errorAlert by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(interaction, null) {
                focusManager.clearFocus()
            }
    ) {
        Text(
            "Let's get you set up",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(15.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Enable carb-only view",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(vertical = 10.dp)
            )
            Switch(
                checked = settingsState.carbOnly,
                onCheckedChange = { newVal ->
                    settings.updateSettings(carbOnly = newVal)
                },
                colors = switchColors()
            )
        }
        Column(Modifier.padding(15.dp)) {
            Text(
                "Set Calorie Goal",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
            TextField(
                value = settings.manualCaloriesValue,
                onValueChange = { newVal ->
                    settings.manualCaloriesValue = newVal
                },
                label = { Text("Enter Calories (kcal)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors(),
                isError = settings.manualCaloriesValue.text.toDoubleOrNull() == null
            )
        }

        Button(
            onClick = {
                if (settings.manualCaloriesValue.text.toDoubleOrNull() != null) {
                    settings.updateSettings(
                        manualCalories = settings.manualCaloriesValue.text,
                        carbOnly = settingsState.carbOnly
                    )
                    moveOn()
                }
                else errorAlert = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            colors = buttonColors(),
            shape = RoundedCornerShape(15.dp)
        ) {
            Text("Confirm")
        }
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
                title = { Text("Invalid calorie goal input") },
                text = { Text("Calorie goal must be a valid number") },
                containerColor = MaterialTheme.colorScheme.surface,
                textContentColor = MaterialTheme.colorScheme.onSurface,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            )
    }
}