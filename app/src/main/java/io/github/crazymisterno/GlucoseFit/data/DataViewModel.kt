package io.github.crazymisterno.GlucoseFit.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
    @InMemoryDb
    private val database: DataManager
): ViewModel() {
    val mealsForToday: StateFlow<List<MealWithFood>> =
        database.mealAccess().getByDate(LocalDate.now()).map { data ->
            data
        }.stateIn(viewModelScope, SharingStarted.Lazily, listOf())


    val savedFood: Flow<List<SavedFoodItem>> = database.savedFoodAccess().getAll()

    fun mealsForDay(date: LocalDate): StateFlow<List<MealWithFood>> {
        return database.mealAccess().getByDate(date)
            .stateIn(viewModelScope, SharingStarted.Eagerly, listOf())
    }

    suspend fun idMeal(id: Int): MealWithFood {
        return database.mealAccess().getById(id)
    }

    fun insertMeals(vararg meals: MealWithFood) {
        viewModelScope.launch {
            meals.forEach { meal ->
                database.mealAccess().insertMeal(meal.meal)
                database.mealAccess().insertFood(*meal.food.toTypedArray())
            }
        }
    }

    fun insertBlankMeals(vararg meals: MealLogEntry) {
        viewModelScope.launch {
            database.mealAccess().insertAllMeals(*meals)
        }
    }

    fun deleteFood(item: FoodItem) {
        viewModelScope.launch {
            database.mealAccess().deleteFood(item)
        }
    }

    fun saveFood(item: FoodItem) {
        val savedItem = SavedFoodItem(
            name = item.name,
            carbs = item.carbs,
            calories = item.calories
        )
        viewModelScope.launch {
            database.savedFoodAccess().insert(savedItem)
        }
    }

    fun deleteSavedFood(item: SavedFoodItem) {
        viewModelScope.launch {
            database.savedFoodAccess().delete(item)
        }
    }

    fun deleteSavedFood(item: FoodItem) {
        val savedItem = SavedFoodItem(
            name = item.name,
            carbs = item.carbs,
            calories = item.calories
        )
        viewModelScope.launch {
            database.savedFoodAccess().delete(savedItem)
        }
    }

    fun searchSavedItems(query: String): Flow<List<SavedFoodItem>> {
        return database.savedFoodAccess().search(query)
    }
}