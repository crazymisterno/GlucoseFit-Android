package io.github.crazymisterno.GlucoseFit.presentation.ui.add.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import io.github.crazymisterno.GlucoseFit.presentation.data.FoodItem
import io.github.crazymisterno.GlucoseFit.presentation.data.MealLogEntry
import java.time.format.DateTimeFormatter

@Composable
fun Confirm(
    meal: MealLogEntry,
    food: FoodItem,
    action: (Boolean) -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("MMM d, uuuu")

    Column {
        Text(
            "Is this correct?",
            style = MaterialTheme.typography.title2,
            color = MaterialTheme.colors.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Row {
            Column {
                Text("Add to:")
                Text(meal.name)
                Text(meal.date.format(formatter))
            }
            Column {
                Text(food.name)
                Text("${food.carbs}")
                Text("${food.calories}")
            }
        }
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { action(true) } ) {
                Text(
                    "Yes",
                    textAlign = TextAlign.Center
                )
            }

            Button(onClick = { action(false) } ) {
                Text(
                    "No",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}