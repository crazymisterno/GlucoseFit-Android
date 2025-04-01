package io.github.crazymisterno.GlucoseFit.ui.content.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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

@Composable
fun AddFoodView(mealId: Int, db: DataViewModel = hiltViewModel(), close: () -> Unit) {
    var nameField by remember { mutableStateOf(TextFieldValue()) }
    var carbField by remember { mutableStateOf(TextFieldValue()) }
    var calField by  remember { mutableStateOf(TextFieldValue()) }
    val focusManager = LocalFocusManager.current
    val interaction = remember { MutableInteractionSource() }
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
                .padding(all = 15.dp)
        ) {
            Text(
                "Add Food",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(20.dp))
            TextField(
                value = nameField,
                onValueChange = {
                    nameField = it
                },
                label = { Text("Food Name") },
                modifier = Modifier.fillMaxWidth(),
                isError = nameField.text.isEmpty()
            )
            Spacer(Modifier.height(15.dp))
            TextField(
                value = carbField,
                onValueChange = {
                    carbField = it
                },
                label = { Text("Carbs (g)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = carbField.text.toDoubleOrNull() == null
            )
            Spacer(Modifier.height(15.dp))
            TextField(
                value = calField,
                onValueChange = {
                    calField = it
                },
                label = { Text("Calories") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = calField.text.toDoubleOrNull() == null
            )
            Spacer(Modifier.padding(15.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.Green)
                    .clickable {
                        val food = FoodItem(
                            mealId = mealId,
                            name = nameField.text,
                            carbs = carbField.text.toDoubleOrNull() ?: 0.0,
                            calories = calField.text.toDoubleOrNull() ?: 0.0
                        )
                        db.addFood(food)
                        close()
                    }
            ) {
                Text(
                    "Add",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp)
                )
            }
            Spacer(Modifier.height(15.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.Yellow)
                    .clickable {
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
            ) {
                Text(
                    "Save",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp)
                )
            }
        }
    }
}