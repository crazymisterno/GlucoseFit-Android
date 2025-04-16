package io.github.crazymisterno.GlucoseFit.data.storage

import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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
    val date: LocalDate,
    val time: LocalTime,
    val dose: Double,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) {
    @Composable
    fun Display(modifier: Modifier, db: DataViewModel = hiltViewModel()) {
        val formatter = if (DateFormat.is24HourFormat(LocalContext.current)) {
            DateTimeFormatter.ofPattern("H:mm")
        }
        else {
            DateTimeFormatter.ofPattern("h:mm a")
        }

        ListItem(
            headlineContent = { Text("${this.dose} units") },
            overlineContent = { Text(this.time.format(formatter)) },
            trailingContent = { Icon(
                Icons.Filled.Delete,
                "Delete",
                tint = Color.Red,
                modifier = modifier
                    .clickable {
                        db.removeDoseLog(this)
                    }
            ) },
            modifier = modifier
        )
    }
}

data class MealWithFood(
    @Embedded val meal: MealLogEntry,
    @Relation(
        parentColumn = "id",
        entityColumn = "mealId"
    )
    val food: List<FoodItem>
)