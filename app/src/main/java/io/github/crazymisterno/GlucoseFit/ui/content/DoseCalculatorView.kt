package io.github.crazymisterno.GlucoseFit.ui.content

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.crazymisterno.GlucoseFit.data.storage.DataViewModel
import io.github.crazymisterno.GlucoseFit.data.settings.SettingsViewModel
import io.github.crazymisterno.GlucoseFit.data.storage.MealLogEntry
import io.github.crazymisterno.GlucoseFit.data.storage.MealWithFood
import java.time.LocalDate
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DoseCalculatorView(model: SettingsViewModel = hiltViewModel(), db: DataViewModel = hiltViewModel()) {
    val relevantMeals by db.mealsForToday.collectAsState(listOf())
    var selectedMeal by remember { mutableStateOf(MealWithFood(
        MealLogEntry(name = "Select", date = LocalDate.now()),
        listOf()
    )) }
    var carbInput by remember { mutableStateOf("") }
    var glucoseInput by remember { mutableStateOf(TextFieldValue()) }
    var suggestedDose by remember { mutableIntStateOf(0) }
    var listOpen by remember { mutableStateOf(false) }
    var mealName = TextFieldValue(selectedMeal.meal.name)
    val interaction = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current
    var errorMsg by remember { mutableStateOf("") }
    var alert by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.horizontalGradient(listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.secondary
            )))
            .clickable(interaction, null) {
                focusManager.clearFocus()
            }
    ) {
        Text(
            "Insulin Dose Calculator",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.inverseSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Text(
                "Select a Meal",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            )
            ExposedDropdownMenuBox(
                expanded = listOpen,
                onExpandedChange = { listOpen = it }
            ) {
                OutlinedTextField(
                    value = mealName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Selected Meal") },
                    trailingIcon = {
                        Icon(
                            Icons.Filled.ArrowDropDown,
                            "Meal selection drop-down"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .padding(bottom = 15.dp)
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )

                ExposedDropdownMenu(
                    expanded = listOpen,
                    onDismissRequest = {
                        listOpen = false
                    }
                ) {
                    relevantMeals.forEach { meal ->
                        DropdownMenuItem(
                            text = { Text(meal.meal.name) },
                            onClick = {
                                selectedMeal = meal
                                var carbValue = 0.0
                                selectedMeal.food.forEach { food ->
                                    carbValue += food.carbs
                                }
                                carbInput = carbValue.toString()
                                listOpen = false
                            }
                        )
                    }
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
                "Carbs",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(15.dp)
            )
            TextField(
                value = carbInput,
                onValueChange = {
                    carbInput = it
                },
                label = { Text("Carbs") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            )
            Text(
                "Glucose Level (mg/dL)",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(15.dp)
            )
            TextField(
                value = glucoseInput,
                onValueChange = {
                    glucoseInput = it
                },
                label = { Text("Glucose Level") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.Blue)
                    .clickable {
                        try {
                            Log.println(Log.INFO, "Message", "Click processed")
                            suggestedDose = calculateDose(carbInput, glucoseInput.text, model)
                        } catch (e: Exception) {
                            errorMsg = e.message ?: ""
                            alert = true
                        }
                    }
            ) {
                Text(
                    "Calculate Dose",
                    color = Color.White,
                    modifier = Modifier
                        .padding(15.dp)
                )
            }
            Text(
                "Suggested Insulin Dose",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(15.dp)
            )
            Text(
                "$suggestedDose units",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            )
        }
        when {
            alert -> AlertDialog(
                title = @Composable { Text("Incorrect settings") },
                text = @Composable { Text("Your $errorMsg is set incorrectly. Please ensure it is a number") },
                onDismissRequest = { alert = false },
                dismissButton = @Composable {
                    TextButton(
                        onClick = { alert = false }
                    ) {
                        Text(
                            "Ok",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                confirmButton = {},
                containerColor = MaterialTheme.colorScheme.surface
            )
        }
    }
}

fun calculateDose(carbs: String, glucose: String, settings: SettingsViewModel): Int {
    Log.println(Log.INFO, "Message", "Function called")
    val insulinRatio = settings.settings.value.insulinToCarbRatio.toDoubleOrNull()
    val correction = settings.settings.value.correctionDose.toDoubleOrNull()
    val targetGlucose = settings.settings.value.targetGlucose.toDoubleOrNull()
    val carbsVal = carbs.toDoubleOrNull()
    val glucoseLevel = glucose.toDoubleOrNull()
    if (insulinRatio == null)
        throw IllegalStateException("insulin to carb ratio setting")
    if (correction == null)
        throw IllegalStateException("correction dose setting")
    if (targetGlucose == null)
        throw IllegalStateException("target glucose setting")
    if (carbsVal == null)
        throw IllegalStateException("carb input")
    if (glucoseLevel == null)
        throw IllegalStateException("glucose level")

    Log.println(Log.INFO, "Message", "Calculating dose")
    val carbDose = carbsVal / insulinRatio
    val correctionDose = max(0.0, (glucoseLevel - targetGlucose) / correction)
    Log.println(Log.INFO, "Message", "Result: ${carbDose + correctionDose}")
    return (carbDose + correctionDose).toInt()
}