package io.github.crazymisterno.GlucoseFit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.toRoute
import com.zedalpha.shadowgadgets.compose.clippedShadow
import io.github.crazymisterno.GlucoseFit.data.DataViewModel
import io.github.crazymisterno.GlucoseFit.data.MealLogEntry
import io.github.crazymisterno.GlucoseFit.data.MealWithFood
import io.github.crazymisterno.GlucoseFit.data.SettingsViewModel
import io.github.crazymisterno.GlucoseFit.dev.PreviewDate
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Serializable
object Home
@Serializable
data class Meal(
    val id: Int
)
@Serializable
data class AddFood(
    val mealId: Int
)
@Serializable
data class ImportSaved(
    val mealId: Int
)

@Preview
@Composable
fun HomeView(
    @PreviewParameter(PreviewDate::class) date: LocalDate,
    db: DataViewModel = hiltViewModel()
) {
    val meals by db.mealsForToday.collectAsState()

    if (meals.isEmpty()) {
        val breakfast = MealLogEntry(
            name = "Breakfast",
            date = date
        )
        val lunch = MealLogEntry(
            name = "Lunch",
            date = date
        )
        val dinner = MealLogEntry(
            name = "Dinner",
            date = date
        )
        val snack = MealLogEntry(
            name = "Snack",
            date = date
        )
        db.insertBlankMeals(breakfast, lunch, dinner, snack)
    }

    var calories = 0
    meals.forEach { meal ->
        meal.food.forEach { item ->
            calories += item.calories.toInt()
        }
    }
    var loggedCalories by remember { mutableIntStateOf(calories) }

    val navigator = rememberNavController()
    val graph = remember(navigator) {
        navigator.createGraph(startDestination = Home
        ) {
            composable<Home> {
                HomeRoot(date, meals) { id ->
                    navigator.navigate(Meal(id))
                }
            }
            composable<Meal> { entry ->
                val meal = entry.toRoute<Meal>()
                MealLogView(meal.id) { method, id ->
                    when (method) {
                        0 -> navigator.navigate(AddFood(id))
                        1 -> navigator.navigate(ImportSaved(id))
                    }
                }
            }
            dialog<AddFood> { entry ->
                val props = entry.toRoute<AddFood>()
                AddFoodView(props.mealId) {
                    navigator.popBackStack()
                }
            }
            dialog<ImportSaved> { entry ->
                val props = entry.toRoute<ImportSaved>()
                SavedFoodView(props.mealId) {
                    navigator.popBackStack()
                }
            }
        }
    }

    NavHost(navigator, graph)
}

@Composable
fun HomeRoot(
    date: LocalDate,
    mealList: List<MealWithFood>,
    settings: SettingsViewModel = hiltViewModel(),
    db: DataViewModel = hiltViewModel(),
    navigate: (Int) -> Unit
) {
    var currentCals = 0;
    mealList.forEach { meal ->
        meal.food.forEach { item ->
            currentCals += item.calories.toInt()
        }
    }
    val finalCals = settings.finalCalories

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
                date.month.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.US) + " " +
                date.dayOfMonth + ", " +
                date.year,
                fontSize = 24.sp,
                fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "Calories",
                fontSize = 40.sp,
                fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "$finalCals - $currentCals",
                fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                (finalCals - currentCals).toString(),
                fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                fontSize = 40.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(Modifier.height(15.dp))
        LazyColumn(
            Modifier.fillMaxHeight()
        ) {
            items(mealList, key = {meal -> meal.meal.id}) { meal ->
                MealSection(meal) {
                    navigate(meal.meal.id)
                }
                Spacer(Modifier.height(15.dp))
            }
        }
    }
}

@Composable
fun MealSection(meal: MealWithFood, pressed: () -> Unit) {
    var totalCarbs = 0.0
    meal.food.forEach { item ->
        totalCarbs += item.carbs
    }
    Column(
        Modifier
            .fillMaxWidth()
            .clippedShadow(
                20.dp,
                RoundedCornerShape(15.dp)
            )
            .background(MaterialTheme.colorScheme.surface)
            .clickable {
                pressed()
            }
            .padding(10.dp)
    ) {
        Text(
            meal.meal.name,
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            "Carbs: $totalCarbs",
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}