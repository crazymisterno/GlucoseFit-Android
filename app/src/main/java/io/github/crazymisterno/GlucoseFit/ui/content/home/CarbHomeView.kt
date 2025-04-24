package io.github.crazymisterno.GlucoseFit.ui.content.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zedalpha.shadowgadgets.compose.clippedShadow
import io.github.crazymisterno.GlucoseFit.data.storage.MealWithFood
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CarbHomeRoot(
    date: LocalDate,
    mealList: List<MealWithFood>,
    navigate: (Int) -> Unit
) {
    var carbs = 0.0
    mealList.forEach { meal ->
        meal.food.forEach { food ->
            carbs += food.carbs
        }
    }
    val formatter = DateTimeFormatter.ofPattern("MMM d, uuuu")

    Column(modifier = Modifier
        .background(Brush.horizontalGradient(listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary
        )))
        .fillMaxSize()
        .padding(top = 15.dp)
        .padding(horizontal = 15.dp)
        .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .clippedShadow(
                    20.dp,
                    RoundedCornerShape(15.dp),
                )
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(15.dp)
        ) {
            Text(
                date.format(formatter),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "Total Carbs",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "${carbs}g",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(Modifier.height(15.dp))
        LazyColumn(
            Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(mealList, key = {meal -> meal.meal.id}) { meal ->
                MealSection(meal) {
                    navigate(meal.meal.id)
                }
            }
        }
    }
}