package io.github.crazymisterno.GlucoseFit.data.storage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.crazymisterno.GlucoseFit.data.settings.TimedSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
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

    fun importFood(food: SavedFoodItem, mealId: Int) {
        viewModelScope.launch {
            val toImport = FoodItem(
                mealId = mealId,
                name = food.name,
                carbs = food.carbs,
                calories = food.calories
            )
            database.mealAccess().insertFood(toImport)
        }
    }

    fun idSavedFood(id: Int): StateFlow<SavedFoodItem> {
        return database.savedFoodAccess().getById(id)
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                SavedFoodItem(name = "", calories = 0.0, carbs = 0.0)
            )
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

    var latestId = MutableStateFlow<Long?>(null)

    fun addTimeSetting(setting: TimedSettings) {
        viewModelScope.launch {
            latestId.value = database.timedSettingAccess().addNew(setting)
        }
    }

    fun removeTimeSetting(setting: TimedSettings) {
        viewModelScope.launch {
            database.timedSettingAccess().delete(setting)
        }
    }

    fun removeTimeSetting(id: Int) {
        viewModelScope.launch {
            database.timedSettingAccess().deleteById(id)
        }
    }

    fun getTimeSetting(): StateFlow<TimedSettings> {
        return database.timedSettingAccess().getCurrentSetting()
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                TimedSettings(LocalTime.now(), "", "", "")
            )
    }

    fun getTimeSetting(id: Int): StateFlow<TimedSettings> {
        return database.timedSettingAccess().getById(id)
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                TimedSettings(LocalTime.now(), "", "", "")
            )
    }

    fun getTimeSetting(time: LocalTime): StateFlow<TimedSettings> {
        return database.timedSettingAccess().getByTime(time)
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                TimedSettings(LocalTime.now(), "", "", "")
            )
    }

    fun getAllTimeSettings(): StateFlow<List<TimedSettings>> {
        return  database.timedSettingAccess().getAll()
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                listOf()
            )
    }

    fun updateSetting(settings: TimedSettings) {
        viewModelScope.launch {
            database.timedSettingAccess().changeConfig(settings)
        }
    }

    fun logDose(units: Double) {
        viewModelScope.launch {
            database.doseLogAccess().newEntry(DoseLogEntry(
                LocalDate.now(),
                LocalTime.now(),
                units
            ))
        }
    }

    fun removeDoseLog(log: DoseLogEntry) {
        viewModelScope.launch {
            database.doseLogAccess().removeEntry(log)
        }
    }

    fun getDoseLogs(date: LocalDate): StateFlow<List<DoseLogEntry>> {
        return database.doseLogAccess().getByDate(date)
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                listOf()
            )
    }

    fun idDoseLog(id: Int): StateFlow<DoseLogEntry> {
        return database.doseLogAccess().getById(id)
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                DoseLogEntry(LocalDate.now(), LocalTime.now(), 0.0)
            )
    }
}