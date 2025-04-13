package io.github.crazymisterno.GlucoseFit.ui.content.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.crazymisterno.GlucoseFit.data.settings.SettingsViewModel
import io.github.crazymisterno.GlucoseFit.ui.content.settings.timed.TimedSettingsList
import io.github.crazymisterno.GlucoseFit.ui.theme.switchColors
import io.github.crazymisterno.GlucoseFit.ui.theme.textFieldColors

@Composable
fun SettingsView(viewModel: SettingsViewModel = hiltViewModel(), select: (Int, Boolean) -> Unit) {
    val settings = viewModel.settings.collectAsState()
    val interaction = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current

    Column(
        Modifier
            .background(
                Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                )
            )
            .verticalScroll(ScrollState(0), enabled = true)
            .padding(horizontal = 15.dp)
            .padding(top = 15.dp)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(bottom = 10.dp)
                .align(Alignment.CenterHorizontally)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(15.dp)
                .clickable(interactionSource = interaction, indication = null) {
                    viewModel.updateSettings(
                        manualCalories = viewModel.manualCaloriesValue.text
                    )
                    focusManager.clearFocus(true)
                },
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Enable carb-only view",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                )
                Switch(
                    checked = settings.value.carbOnly,
                    onCheckedChange = { newVal ->
                        viewModel.updateSettings(carbOnly = newVal)
                    },
                    colors = switchColors()
                )
            }
            Column {
                Text(
                    "Set Calorie Goal",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                TextField(
                    value = viewModel.manualCaloriesValue,
                    onValueChange = { newVal ->
                        viewModel.manualCaloriesValue = newVal
                    },
                    label = { Text("Enter Calories (kcal)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors()
                )
            }
            Spacer(Modifier.padding(4.dp))
            TimedSettingsList { id, adding ->
                select(id, adding)
            }
        }
    }
}