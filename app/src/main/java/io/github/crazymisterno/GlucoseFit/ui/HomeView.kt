package io.github.crazymisterno.GlucoseFit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.toRoute
import androidx.room.Room
import com.zedalpha.shadowgadgets.compose.clippedShadow
import com.zedalpha.shadowgadgets.compose.shadowCompat
import io.github.crazymisterno.GlucoseFit.data.DataManager
import io.github.crazymisterno.GlucoseFit.data.MealLogEntry
import io.github.crazymisterno.GlucoseFit.data.MealWithFood
import io.github.crazymisterno.GlucoseFit.data.SettingsViewModel
import io.github.crazymisterno.GlucoseFit.ui.theme.GlucoseFitMaterialTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
    val method: Int,
    val mealId: Int
)

var queryAdded = false

@Composable
fun HomeView(settings: SettingsViewModel, db: DataManager, date: LocalDate) {
    val context = rememberCoroutineScope()
    val meals by db.mealAccess().getByDate(date).collectAsStateWithLifecycle(listOf())
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
        LaunchedEffect(1) {
            context.launch {
                db.mealAccess().insertAllMeals(breakfast, lunch, dinner, snack)
            }
        }
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
                HomeRoot(date, meals, settings.finalCalories, loggedCalories) { id ->
                    navigator.navigate(route = Meal(id))
                }
            }
            composable<Meal> { entry ->
                val meal = entry.toRoute<Meal>()
                val logEntry: MealWithFood;
                runBlocking { logEntry = db.mealAccess().getById(meal.id) }
                MealLogView(logEntry, db)
            }
            composable<AddFood> { entry ->
                val props = entry.toRoute<AddFood>()
                if (props.method == 0) {
                    val logEntry: MealWithFood;
                    runBlocking {
                        logEntry = db.mealAccess().getById(props.mealId)
                    }
                    AddFoodView(logEntry, db)
                }
            }
        }
    }

    NavHost(navigator, graph)
}

@Composable
fun HomeRoot(
    date: LocalDate,
    meals: List<MealWithFood>,
    finalCals: Int,
    currentCals: Int,
    navigate: (Int) -> Unit
) {
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
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "Calories",
                fontSize = 40.sp,
                fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                textAlign = TextAlign.Center,
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
                modifier = Modifier.fillMaxWidth()
            )
        }
        meals.forEach { meal ->
            MealSection(meal) {
                navigate(meal.meal.id)
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
            .padding(15.dp)
            .fillMaxWidth()
            .clippedShadow(
                20.dp,
                RoundedCornerShape(15.dp)
            )
            .background(MaterialTheme.colorScheme.surface)
            .clickable {
                pressed()
            }
    ) {
        Text(
            meal.meal.name,
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
            fontSize = 30.sp,
        )
        Text(
            "Carbs: $totalCarbs",
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
            fontSize = 22.sp
        )
    }
}

@Preview
@Composable
fun PreviewHome() {
    val date = LocalDate.now()
    val db = Room.inMemoryDatabaseBuilder(
        LocalContext.current,
        DataManager::class.javaObjectType
    ).build()
    val settings = SettingsViewModel(PreviewSettings())
    GlucoseFitMaterialTheme {
        HomeView(settings, db, date)
    }
}