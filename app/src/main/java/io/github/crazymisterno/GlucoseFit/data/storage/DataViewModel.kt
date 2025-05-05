package io.github.crazymisterno.GlucoseFit.data.storage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.crazymisterno.GlucoseFit.data.settings.TimedSettings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
    private val database: DataManager
): ViewModel() {
    val savedFood: StateFlow<List<SavedFoodItem>> = database.savedFoodAccess().getAll()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            listOf()
        )

    fun autoPopulateMeals(date: LocalDate) {
        viewModelScope.launch {
            val meals = database.mealAccess().getOnceByDate(date)
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
                insertBlankMeals(breakfast, lunch, dinner, snack)
            }
        }
    }

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
            val toImport = FoodItem(food, mealId)
            database.mealAccess().insertFood(toImport)
        }
    }

    fun idSavedFood(id: Int): StateFlow<SavedFoodItem> {
        return database.savedFoodAccess().getById(id)
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                SavedFoodItem("", 0.0, 0.0)
            )
    }

    fun deleteFood(item: FoodItem) {
        viewModelScope.launch {
            database.mealAccess().deleteFood(item)
        }
    }

    fun saveFood(item: FoodItem) {
        val savedItem = SavedFoodItem(item)
        viewModelScope.launch {
            database.savedFoodAccess().insert(savedItem)
        }
    }

    fun deleteSavedFood(item: SavedFoodItem) {
        viewModelScope.launch {
            database.savedFoodAccess().delete(item)
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

    suspend fun addSettingAsync(settings: TimedSettings): Long {
        return database.timedSettingAccess().addNew(settings)
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

    fun getTimeSetting(): StateFlow<TimedSettings?> {
        Log.println(Log.INFO, "Message", "Rerunning query")
        return database.timedSettingAccess().findActive()
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                TimedSettings(LocalTime.now(), "", "", "")
            )
    }

    fun getTimeSetting(id: Int): StateFlow<TimedSettings> {
        return database.timedSettingAccess().getById(id)
            .distinctUntilChanged()
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                TimedSettings(LocalTime.now(), "", "", "")
            )
    }

    fun getAllTimeSettings(): StateFlow<List<TimedSettings>> {
        return database.timedSettingAccess().getAll()
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                listOf()
            )
    }

    suspend fun readTimeSettingsOnce(): List<TimedSettings> {
        return database.timedSettingAccess().checkOnce()
    }

    fun updateSetting(settings: TimedSettings) {
        viewModelScope.launch {
            database.timedSettingAccess().changeConfig(settings)
        }
    }

    fun logDose(units: Double, date: LocalDate) {
        viewModelScope.launch {
            database.doseLogAccess().newEntry(DoseLogEntry(date, units))
        }
    }

    fun logDose(units: Double, date: LocalDate, time: LocalTime) {
        viewModelScope.launch {
            database.doseLogAccess().newEntry(DoseLogEntry(
                date,
                time,
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
}