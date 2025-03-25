package io.github.crazymisterno.GlucoseFit.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import java.time.LocalDate

@Dao
interface MealAccess {
    @Insert
    fun insertMeal(meal: MealLogEntry): Long

    @Insert
    fun insertFood(food: FoodItem): Long

    @Delete
    fun deleteFood(food: FoodItem)

    @Transaction
    @Query("SELECT * FROM meals WHERE date = :date")
    fun getByDate(date: LocalDate): List<MealWithFood>

    @Transaction
    @Query("SELECT * FROM food WHERE mealId = :mealId AND name LIKE '%' || :query || '%'")
    fun searchFood(mealId: Int, query: String): List<FoodItem>
}

@Dao
interface SavedFoodAccess {
    @Insert
    fun insert(food: SavedFoodItem)

    @Delete
    fun delete(food: SavedFoodItem)

    @Query("SELECT * FROM savedFood WHERE name LIKE '%' || :query || '%'")
    fun search(query: String): List<SavedFoodItem>
}

@Dao
interface CalendarAccess {
    @Insert
    fun insert(entry: CalendarEntry)

    @Delete
    fun delete(entry: CalendarEntry)
}
