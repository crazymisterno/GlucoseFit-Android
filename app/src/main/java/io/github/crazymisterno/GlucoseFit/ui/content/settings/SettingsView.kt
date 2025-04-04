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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.crazymisterno.GlucoseFit.data.settings.SettingsViewModel
import io.github.crazymisterno.GlucoseFit.data.settings.format
import io.github.crazymisterno.GlucoseFit.data.proto.ActivityLevel
import io.github.crazymisterno.GlucoseFit.data.proto.GenderOption
import io.github.crazymisterno.GlucoseFit.data.proto.Goal
import io.github.crazymisterno.GlucoseFit.ui.content.settings.timed.TimedSettingsList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(viewModel: SettingsViewModel = hiltViewModel(), select: (Int, Boolean) -> Unit) {
    val settings = viewModel.settings.collectAsState()
    var genderDropDown by remember { mutableStateOf(false) }
    var activityDropDown by remember { mutableStateOf(false) }
    var goalDropDown by remember { mutableStateOf(false) }
    val interaction = remember { MutableInteractionSource() }
    val textFocus = remember { FocusRequester() }
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
                        weight = viewModel.weightValue.text,
                        heightFeet = viewModel.heightFeetValue.text,
                        heightInches = viewModel.heightInchesValue.text,
                        age = viewModel.ageValue.text,
                        insulinToCarbRatio = viewModel.insulinToCarbRatioValue.text,
                        correctionDose = viewModel.correctionDoseValue.text,
                        targetGlucose = viewModel.targetGlucoseValue.text,
                        manualCalories = viewModel.manualCaloriesValue.text
                    )
                    viewModel.recommendedCalories = viewModel.evaluateRecommendedCalories()
                    viewModel.finalCalories = viewModel.evaluateFinalCalories()
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
                )
            }
            Column {
                Text(
                    "Weight (lbs)",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                TextField(
                    value = viewModel.weightValue,
                    onValueChange = { newVal ->
                        viewModel.weightValue = newVal
                    },
                    label = { Text("Enter Weight (lbs)") },
                    modifier = Modifier
                        .focusRequester(textFocus),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(Modifier.padding(vertical = 5.dp))

            Column {
                Text(
                    "Height",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Row {
                    TextField(
                        value = viewModel.heightFeetValue,
                        onValueChange = { newVal ->
                            viewModel.heightFeetValue = newVal
                        },
                        label = @Composable { Text("Feet") },
                        modifier = Modifier
                            .width(70.dp)
                            .focusRequester(textFocus),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                    Text(
                        "ft",
                        modifier = Modifier
                            .offset(y = 27.dp)
                            .padding(horizontal = 4.dp),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    TextField(
                        value = viewModel.heightInchesValue,
                        onValueChange = { newVal ->
                            viewModel.heightInchesValue = newVal
                        },
                        label = @Composable { Text("Inches") },
                        modifier = Modifier
                            .width(90.dp)
                            .focusRequester(textFocus),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Text(
                        "in",
                        modifier = Modifier
                            .offset(y = 27.dp)
                            .padding(horizontal = 4.dp),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            Spacer(Modifier.padding(vertical = 5.dp))

            Column {
                Text(
                    "Age",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                TextField(
                    value = viewModel.ageValue,
                    onValueChange = { newVal ->
                        viewModel.ageValue = newVal
                    },
                    label = @Composable { Text("Enter Age") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .focusRequester(textFocus)
                )
            }
            Spacer(Modifier.padding(4.dp))
            Column {
                Text(
                    "Gender",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                ExposedDropdownMenuBox(
                    expanded = genderDropDown,
                    onExpandedChange = { expanded ->
                        genderDropDown = expanded
                    }
                ) {
                    OutlinedTextField(
                        value = settings.value.gender.format(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Gender") },
                        trailingIcon = {
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                contentDescription = "Dropdown",
                            )
                        },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )

                    ExposedDropdownMenu(
                        expanded = genderDropDown,
                        onDismissRequest = {
                            genderDropDown = false
                        }
                    ) {
                        GenderOption.entries.forEach { opt ->
                            if (opt != GenderOption.UNRECOGNIZED) {
                                DropdownMenuItem(
                                    text = { Text(opt.format()) },
                                    onClick = {
                                        viewModel.updateSettings(gender = opt)
                                        genderDropDown = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.padding(vertical = 4.dp))
            Column {
                Text(
                    "Activity Level",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                ExposedDropdownMenuBox(
                    expanded = activityDropDown,
                    onExpandedChange = { expanded ->
                        activityDropDown = expanded
                    }
                ) {
                    OutlinedTextField(
                        value = settings.value.activityLevel.format(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Activity Level") },
                        trailingIcon = {
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                contentDescription = "Dropdown"
                            )
                        },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )

                    ExposedDropdownMenu(
                        expanded = activityDropDown,
                        onDismissRequest = {
                            activityDropDown = false
                        }
                    ) {
                        ActivityLevel.entries.forEach { opt ->
                            if (opt != ActivityLevel.UNRECOGNIZED) {
                                DropdownMenuItem(
                                    text = { Text(opt.format()) },
                                    onClick = {
                                        viewModel.updateSettings(activityLevel = opt)
                                        activityDropDown = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.padding(4.dp))
            Column {
                Text(
                    "Goal",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,

                    )
                ExposedDropdownMenuBox(
                    expanded = goalDropDown,
                    onExpandedChange = { expanded ->
                        goalDropDown = true
                    }
                ) {
                    OutlinedTextField(
                        value = settings.value.goal.format(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Goal") },
                        trailingIcon = {
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                contentDescription = "DropDown"
                            )
                        },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = goalDropDown,
                        onDismissRequest = {
                            goalDropDown = false
                        }
                    ) {
                        Goal.entries.forEach { opt ->
                            if (opt != Goal.UNRECOGNIZED) {
                                DropdownMenuItem(
                                    text = { Text(opt.format()) },
                                    onClick = {
                                        viewModel.updateSettings(goal = opt)
                                        goalDropDown = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.padding(4.dp))
            TimedSettingsList { id, adding ->
                select(id, adding)
            }
            Spacer(Modifier.padding(4.dp))
            Column {
                Text(
                    "Recommended Daily Calories",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    viewModel.recommendedCalories.toString() + " kcal",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primaryContainer,
                )
            }
            Spacer(Modifier.padding(4.dp))
            Column {
                Text(
                    "Set Custom Calories",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                TextField(
                    value = viewModel.manualCaloriesValue,
                    onValueChange = { newVal ->
                        viewModel.manualCaloriesValue = newVal
                    },
                    label = { Text("Enter Custom Calories") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Spacer(Modifier.padding(4.dp))
            Column {
                Text(
                    "Final Daily Calories",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    viewModel.finalCalories.toString() + " kcal",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }
        }

//        Spacer(Modifier.height(20.dp))
    }
}