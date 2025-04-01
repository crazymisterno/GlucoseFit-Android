package io.github.crazymisterno.GlucoseFit.data.storage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
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


    val savedFood: StateFlow<List<SavedFoodItem>> = database.savedFoodAccess().getAll()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            listOf()
        )

    fun mealsForDay(date: LocalDate): StateFlow<List<MealWithFood>> {
        return database.mealAccess().getByDate(date)
            .stateIn(viewModelScope, SharingStarted.Eagerly, listOf())
    }

    fun idMeal(id: Int): StateFlow<MealWithFood> {
        return database.mealAccess().getById(id).distinctUntilChanged()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                MealWithFood(MealLogEntry(name = "", date = LocalDate.now()), listOf())
            )
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

    fun addFood(item: FoodItem) {
        viewModelScope.launch {
            database.mealAccess().insertFood(item)
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

    fun searchSavedItems(query: String): StateFlow<List<SavedFoodItem>> {
        return database.savedFoodAccess().search(query)
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                listOf()
            )
    }
}