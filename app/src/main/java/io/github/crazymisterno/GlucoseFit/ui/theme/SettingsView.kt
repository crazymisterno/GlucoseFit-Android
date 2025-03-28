package io.github.crazymisterno.GlucoseFit.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.codelab.android.datastore.Settings
import io.github.crazymisterno.GlucoseFit.R
import io.github.crazymisterno.GlucoseFit.data.SettingsProvider
import io.github.crazymisterno.GlucoseFit.data.SettingsViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(viewModel: SettingsViewModel) {
    val settings = viewModel.settings.collectAsState()
    val font = Font(R.font.inter_variablefont_opsz_wght)
    val interFamily = FontFamily(font)
    val brush = Brush.horizontalGradient(
        listOf<Color>(
            Color(0.33f, 0.62f, 0.68f),
            Color(0.6f, 0.89f, 0.75f)
        )
    )
    var genderDropDown by remember { mutableStateOf(false) }
    var activityDropDown by remember { mutableStateOf(false) }
    var goalDropDown by remember { mutableStateOf(false) }
    Column(
        Modifier
            .background(brush)
            .fillMaxSize()
            .verticalScroll(ScrollState(0), enabled = true)
            .padding(top = Dp(10f))
            .padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(10.dp))
                .background(Color(1f, 1f, 1f, 0.7f))
                .padding(Dp(15f)),
        ) {
            Text(
                text = "Settings",
                fontSize = TextUnit(34f, TextUnitType.Sp),
                fontFamily = interFamily,
                color = Color.Black,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Enable carb-only view",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = TextUnit(17f, TextUnitType.Sp),
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
                    fontWeight = FontWeight.SemiBold,
                    fontSize = TextUnit(17f, TextUnitType.Sp),
                )
                TextField(
                    value = settings.value.weight,
                    onValueChange = {newVal ->
                        viewModel.updateSettings(weight = newVal)
                    },
                    label = { Text("Enter Weight (lbs)") }
                )
            }

            Spacer(Modifier.padding(vertical = 5.dp))

            Column {
                Text(
                    "Height",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = TextUnit(17f, TextUnitType.Sp)
                )
                Row {
                    TextField(
                        value = settings.value.heightFeet,
                        onValueChange = {newVal ->
                            viewModel.updateSettings(heightFeet = newVal)
                        },
                        label = @Composable { Text("Feet") },
                        modifier = Modifier
                            .width(60.dp)
                    )
                    Text(
                        "ft",
                        modifier = Modifier
                            .offset(y = 27.dp)
                            .padding(horizontal = 4.dp),
                        fontSize = TextUnit(20f, TextUnitType.Sp)
                    )
                    TextField(
                        value = settings.value.heightInches,
                        onValueChange = {newVal ->
                            viewModel.updateSettings(heightInches = newVal)
                        },
                        label = @Composable { Text("Inches") },
                        modifier = Modifier
                            .width(70.dp),
                    )
                    Text(
                        "in",
                        modifier = Modifier
                            .offset(y = 27.dp)
                            .padding(horizontal = 4.dp),
                        fontSize = TextUnit(20f, TextUnitType.Sp)
                    )
                }
            }

            Spacer(Modifier.padding(vertical = 5.dp))

            Column {
                Text(
                    "Age",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = TextUnit(17f, TextUnitType.Sp)
                )
                TextField(
                    value = settings.value.age,
                    onValueChange = {newVal ->
                        viewModel.updateSettings(age = newVal)
                    },
                    label = @Composable { Text("Enter Age") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Spacer(Modifier.padding(4.dp))
            Column {
                Text(
                    "Gender",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = TextUnit(17f, TextUnitType.Sp)
                )
                ExposedDropdownMenuBox(
                    expanded = genderDropDown,
                    onExpandedChange = {expanded ->
                        genderDropDown = expanded
                    }
                ) {
                    OutlinedTextField(
                        value = settings.value.gender,
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
                        SettingsViewModel.genderOptions.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt) },
                                onClick = {
                                    viewModel.updateSettings(gender = opt)
                                    genderDropDown = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.padding(vertical = 4.dp))
            Column {
                Text(
                    "Activity Level",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = TextUnit(17f, TextUnitType.Sp)
                )
                ExposedDropdownMenuBox(
                    expanded = activityDropDown,
                    onExpandedChange = {expanded ->
                        activityDropDown = expanded
                    }
                ) {
                    OutlinedTextField(
                        value = settings.value.activityLevel,
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
                        SettingsViewModel.activityOptions.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt) },
                                onClick = {
                                    viewModel.updateSettings(activityLevel = opt)
                                    activityDropDown = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.padding(4.dp))
            Column {
                Text(
                    "Goal",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = TextUnit(17f, TextUnitType.Sp)
                )
                ExposedDropdownMenuBox(
                    expanded = goalDropDown,
                    onExpandedChange = { expanded ->
                        goalDropDown = true
                    }
                ) {
                    OutlinedTextField(
                        value = settings.value.goal,
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
                        SettingsViewModel.goalOptions.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt) },
                                onClick = {
                                    viewModel.updateSettings(goal = opt)
                                    goalDropDown = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.padding(4.dp))
            Column {
                Text(
                    "Insulin to carb ratio",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = TextUnit(17f, TextUnitType.Sp)
                )
                Row {
                    Text(
                        "1:",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = TextUnit(17f, TextUnitType.Sp),
                        modifier = Modifier.offset(y = 27.dp)
                    )
                    Spacer(Modifier.padding(2.dp))
                    TextField(
                        value = settings.value.insulinToCarbRatio,
                        onValueChange = {newVal ->
                            viewModel.updateSettings(newVal)
                        },
                        label = { Text("Ratio") }
                    )
                }
            }
            Spacer(Modifier.padding(4.dp))
            Column {
                Text(
                    "Correction Dose",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = TextUnit(17f, TextUnitType.Sp)
                )
                Row {
                    Text(
                        "1:",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = TextUnit(17f, TextUnitType.Sp),
                        modifier = Modifier
                            .offset(y = 27.dp)
                    )
                    Spacer(Modifier.padding(2.dp))
                    TextField(
                        value = settings.value.correctionDose,
                        onValueChange = {newVal ->
                            viewModel.updateSettings(correctionDose = newVal)
                        }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun Preview() {
    val model = SettingsViewModel(PreviewSettings())
    SettingsView(model)
}

class PreviewSettings : SettingsProvider {
    override val shared: Flow<Settings> = flowOf(
        Settings.newBuilder()
            .setWeight("165.0")
            .setHeightFeet("5.0")
            .setHeightInches("8.0")
            .setAge("23")
            .setGender("Male")
            .setActivityLevel("Sedentary")
            .setGoal("Maintain Weight")
            .setManualCalories("2000.0")
            .setInsulinToCarbRatio("4.0")
            .setCorrectionDose("3.0")
            .setTargetGlucose("100.0")
            .setCarbOnly(false)
            .build()
    )

    override suspend fun updateSettings(
        weight: String?,
        heightFeet: String?,
        heightInches: String?,
        age: String?,
        gender: String?,
        activityLevel: String?,
        goal: String?,
        manualCalories: String?,
        insulinToCarbRatio: String?,
        correctionDose: String?,
        targetGlucose: String?,
        carbOnly: Boolean?
    ) {
        return
    }
}