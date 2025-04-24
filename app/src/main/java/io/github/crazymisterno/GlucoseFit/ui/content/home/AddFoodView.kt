package io.github.crazymisterno.GlucoseFit.ui.content.home

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.crazymisterno.GlucoseFit.data.storage.DataViewModel
import io.github.crazymisterno.GlucoseFit.data.storage.FoodItem
import io.github.crazymisterno.GlucoseFit.ui.theme.textFieldColors

@Composable
fun AddFoodView(mealId: Int, db: DataViewModel = hiltViewModel(), close: () -> Unit) {
    var nameField by remember { mutableStateOf(TextFieldValue()) }
    var carbField by remember { mutableStateOf(TextFieldValue()) }
    var calField by  remember { mutableStateOf(TextFieldValue()) }
    val focusManager = LocalFocusManager.current
    val interaction = remember { MutableInteractionSource() }
    var errorAlert by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 15.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(interaction, null) {
                focusManager.clearFocus()
            }
    ) {
        Column(
            modifier = Modifier
                .padding(all = 15.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Text(
                "Add Food",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(5.dp))
            TextField(
                value = nameField,
                onValueChange = {
                    nameField = it
                },
                label = { Text("Food Name") },
                colors = textFieldColors(),
                modifier = Modifier.fillMaxWidth(),
            )
            TextField(
                value = carbField,
                onValueChange = {
                    carbField = it
                },
                label = { Text("Carbs (g)") },
                colors = textFieldColors(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = carbField.text.toDoubleOrNull() == null && carbField.text.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = calField,
                onValueChange = {
                    calField = it
                },
                label = { Text("Calories") },
                colors = textFieldColors(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = calField.text.toDoubleOrNull() == null && calField.text.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    if (
                        nameField.text.isEmpty() ||
                        carbField.text.toDoubleOrNull() == null ||
                        calField.text.toDoubleOrNull() == null
                    ) {
                        errorAlert = true
                    }
                    else {
                        val food = FoodItem(
                            mealId = mealId,
                            name = nameField.text,
                            carbs = carbField.text.toDoubleOrNull() ?: 0.0,
                            calories = calField.text.toDoubleOrNull() ?: 0.0
                        )
                        db.addFood(food)
                        close()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Add",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(15.dp)
                )
            }
            Button(
                onClick = {
                    if (
                        nameField.text.isEmpty() ||
                        carbField.text.toDoubleOrNull() == null ||
                        calField.text.toDoubleOrNull() == null
                    ) {
                        errorAlert = true
                    }
                    else {
                        val food = FoodItem(
                            mealId = mealId,
                            name = nameField.text,
                            carbs = carbField.text.toDoubleOrNull() ?: 0.0,
                            calories = calField.text.toDoubleOrNull() ?: 0.0
                        )
                        db.addFood(food)
                        db.saveFood(food)
                        close()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Yellow,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Save",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(15.dp)
                )
            }
        }
        if (errorAlert) {
            AlertDialog(
                onDismissRequest = { errorAlert = false },
                confirmButton = {
                    TextButton(
                        onClick = { errorAlert = false }
                    ) {
                        Text(
                            "Ok",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                title = { Text("Invalid inputs") },
                text = {
                    Column {
                        if (nameField.text.isEmpty())
                            Text("Food item must have a name")
                        if (carbField.text.toDoubleOrNull() == null)
                            Text("Carb input must be a valid number")
                        if (calField.text.toDoubleOrNull() == null)
                            Text("Calorie input must be a valid number")
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                textContentColor = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}