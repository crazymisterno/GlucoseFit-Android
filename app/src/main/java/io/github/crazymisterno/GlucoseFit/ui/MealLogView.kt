package io.github.crazymisterno.GlucoseFit.ui

import androidx.compose.runtime.Composable
import io.github.crazymisterno.GlucoseFit.data.DataManager
import io.github.crazymisterno.GlucoseFit.data.MealWithFood

@Preview
@Composable
fun MealLogView(
    @PreviewParameter(PreviewIds::class) mealId: Int,
    db: DataViewModel = hiltViewModel()
) {
    Text("Meal Id: $mealId")
}