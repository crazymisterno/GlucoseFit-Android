package io.github.crazymisterno.GlucoseFit.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDate

@Entity(tableName = "meals")
data class MealLogEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val date: LocalDate
)

@Entity(
    tableName = "food",
    foreignKeys = [ForeignKey(
        entity = MealLogEntry::class,
        parentColumns = ["id"],
        childColumns = ["mealId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    indices = [Index("mealId")]
)
data class FoodItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mealId: Int,
    val name: String,
    val carbs: Double,
    val calories: Double
)

@Entity(tableName = "savedFood")
data class SavedFoodItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val carbs: Double,
    val calories: Double
)

@Entity(tableName = "calendar")
data class CalendarEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: LocalDate,
    val category: String,
    val value: Double,
    val notes: String
)

data class MealWithFood(
    @Embedded val meal: MealLogEntry,
    @Relation(
        parentColumn = "id",
        entityColumn = "mealId"
    )
    val food: List<FoodItem>
)