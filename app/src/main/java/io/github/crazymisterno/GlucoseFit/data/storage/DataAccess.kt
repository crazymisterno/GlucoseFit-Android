package io.github.crazymisterno.GlucoseFit.data.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface MealAccess {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMeal(meal: MealLogEntry): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllMeals(vararg meals: MealLogEntry): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFood(vararg food: FoodItem): List<Long>

    @Delete
    suspend fun deleteFood(food: FoodItem)

    @Transaction
    @Query("SELECT * FROM meals WHERE date = :date")
    fun getByDate(date: LocalDate): Flow<List<MealWithFood>>

    @Transaction
    @Query("SELECT * FROM meals WHERE id = :id LIMIT 1")
    fun getById(id: Int): Flow<MealWithFood>

    @Transaction
    @Query("SELECT * FROM food WHERE mealId = :mealId AND name LIKE '%' || :query || '%'")
    fun searchFood(mealId: Int, query: String): Flow<List<FoodItem>>
}

@Dao
interface SavedFoodAccess {
    @Insert
    suspend fun insert(food: SavedFoodItem)

    @Delete
    suspend fun delete(food: SavedFoodItem)

    @Query("SELECT * FROM savedFood WHERE name LIKE '%' || :query || '%'")
    fun search(query: String): Flow<List<SavedFoodItem>>

    @Query("SELECT * FROM savedFood")
    fun getAll(): Flow<List<SavedFoodItem>>
}

@Dao
interface CalendarAccess {
    @Insert
    suspend fun insert(entry: CalendarEntry)

    @Delete
    suspend fun delete(entry: CalendarEntry)
}
