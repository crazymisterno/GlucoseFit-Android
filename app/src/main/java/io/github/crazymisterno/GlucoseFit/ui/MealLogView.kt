package io.github.crazymisterno.GlucoseFit.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.crazymisterno.GlucoseFit.data.DataViewModel
import io.github.crazymisterno.GlucoseFit.dev.PreviewIds

@Preview
@Composable
fun MealLogView(
    @PreviewParameter(PreviewIds::class) mealId: Int,
    db: DataViewModel = hiltViewModel()
) {
    Text("Meal Id: $mealId")
}