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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
                    value = TextFieldValue(
                        settings.value.weight.toString(),
                    ),
                    onValueChange = {newVal ->
                        viewModel.updateSettings(weight = newVal.text.toDouble())
                    },
                    label = { Text("Enter Weight (lbs)") },
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
                        value = TextFieldValue(settings.value.heightFeet.toString()),
                        onValueChange = {newVal ->
                            if (newVal.text.isDigitsOnly())
                                viewModel.updateSettings(heightFeet = newVal.text.toDouble())
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
                        value = TextFieldValue(
                            settings.value.heightInches.toString(),
                        ),
                        onValueChange = {newVal ->
                            if (newVal.text.isDigitsOnly())
                                viewModel.updateSettings(heightInches = newVal.text.toDouble())
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
                    value = TextFieldValue(
                        settings.value.age.toString(),
                    ),
                    onValueChange = {newVal ->
                        viewModel.updateSettings(age = newVal.text.toInt())
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
                DropdownMenu(
                    expanded = genderDropDown,
                    onDismissRequest = { genderDropDown = false }
                ) {
                    SettingsViewModel.genderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = @Composable { Text(option) },
                            onClick = {
                                viewModel.updateSettings(gender = option)
                                genderDropDown = false
                            }
                        )

                    }
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
            .setWeight(165.0)
            .setHeightFeet(5.0)
            .setHeightInches(8.0)
            .setAge(23)
            .setGender("Male")
            .setActivityLevel("Sedentary")
            .setGoal("Maintain Weight")
            .setManualCalories(2000.0)
            .setInsulinToCarbRatio(4.0)
            .setCorrectionDose(3.0)
            .setTargetGlucose(100.0)
            .setCarbOnly(false)
            .build()
    )

    override suspend fun updateSettings(
        weight: Double?,
        heightFeet: Double?,
        heightInches: Double?,
        age: Int?,
        gender: String?,
        activityLevel: String?,
        goal: String?,
        manualCalories: Double?,
        insulinToCarbRatio: Double?,
        correctionDose: Double?,
        targetGlucose: Double?,
        carbOnly: Boolean?
    ) {
        return
    }
}