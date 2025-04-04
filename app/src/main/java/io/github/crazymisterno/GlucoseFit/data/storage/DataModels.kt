package io.github.crazymisterno.GlucoseFit.data.storage

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDate
import java.time.LocalDateTime

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

@Entity(tableName = "doseLog")
data class DoseLogEntry(
    val time: LocalDateTime,
    val dose: Double,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

data class MealWithFood(
    @Embedded val meal: MealLogEntry,
    @Relation(
        parentColumn = "id",
        entityColumn = "mealId"
    )
    val food: List<FoodItem>
)