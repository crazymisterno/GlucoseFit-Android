package io.github.crazymisterno.GlucoseFit.presentation.ui.add

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.MaterialTheme
import io.github.crazymisterno.GlucoseFit.presentation.data.FoodItem
import io.github.crazymisterno.GlucoseFit.presentation.data.MealLogEntry
import io.github.crazymisterno.GlucoseFit.presentation.data.MessagingViewModel
import io.github.crazymisterno.GlucoseFit.presentation.ui.add.steps.Confirm
import io.github.crazymisterno.GlucoseFit.presentation.ui.add.steps.CreateFood
import io.github.crazymisterno.GlucoseFit.presentation.ui.add.steps.SelectMeal

@Composable
fun AddFoodNavigator(messagingViewModel: MessagingViewModel = hiltViewModel(), close: () -> Unit) {
    var meal by remember { mutableStateOf<MealLogEntry?>(null) }
    var foodItem by remember { mutableStateOf<FoodItem?>(null) }

    var step by remember { mutableIntStateOf(0) }

    Box(
        Modifier
        .fillMaxSize()
        .background(Brush.horizontalGradient(listOf(
            MaterialTheme.colors.primary,
            MaterialTheme.colors.secondary
        )))
    ) {
        when (step) {
            0 -> SelectMeal { entry ->
                meal = entry
                step++
            }
            1 -> CreateFood { name, carbs, cals ->
                if (meal != null) {
                    foodItem = FoodItem(
                        meal!!.id,
                        name,
                        carbs,
                        cals
                    )
                    step++
                } else {
                    Log.i("Message", "Meal selection is null")
                }
            }
            2 -> {
                if (meal != null && foodItem != null)
                    Confirm(meal!!, foodItem!!) { answer ->
                        if (answer) {
                            messagingViewModel.logNewFood(foodItem!!)
                            close()
                        }
                        else {
                            step = 0
                        }
                    }
            }
        }
    }
}