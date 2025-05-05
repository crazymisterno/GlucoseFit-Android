package io.github.crazymisterno.GlucoseFit.data.storage

import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
    val mealId: Int,
    val name: String,
    val carbs: Double,
    val calories: Double,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) {
    constructor(from: SavedFoodItem, mealId: Int) : this(mealId, from.name, from.carbs, from.calories)

    @Composable
    fun Display(modifier: Modifier, delete: () -> Unit, save: () -> Unit) {
        ListItem(
            headlineContent = { Text(this.name) },
            supportingContent = { Text("${this.carbs}g carbs, ${this.calories} cal") },
            trailingContent = {
                Row {
                    Icon(
                        Icons.Filled.Delete,
                        "Delete item",
                        tint = Color.Red,
                        modifier = Modifier
                            .clickable {
                                delete()
                            }
                    )
                    Spacer(Modifier.width(5.dp))
                    Icon(
                        Icons.Filled.AddCircle,
                        "Save item",
                        tint = Color.Green,
                        modifier = Modifier
                            .clickable {
                                save()
                            }
                    )
                }
            },
            modifier = modifier
        )

    }
}

@Entity(tableName = "savedFood")
data class SavedFoodItem(
    val name: String,
    val carbs: Double,
    val calories: Double,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) {
    constructor(from: FoodItem) : this(from.name, from.carbs, from.calories)

    @Composable
    fun Display(modifier: Modifier, import: () -> Unit, detail: () -> Unit) {
        ListItem(
            headlineContent = { Text(this.name) },
            supportingContent = { Text("${this.carbs}g carbs, ${this.calories} Calories")},
            modifier = modifier
                .combinedClickable(
                    onClick = {
                        import()
                    },
                    onLongClick = {
                        detail()
                    }
                ),
        )
    }
}

@Entity(tableName = "doseLog")
data class DoseLogEntry(
    val date: LocalDate,
    val time: LocalTime,
    val dose: Double,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) {
    constructor(date: LocalDate, dose: Double) : this(date, LocalTime.now(), dose)

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