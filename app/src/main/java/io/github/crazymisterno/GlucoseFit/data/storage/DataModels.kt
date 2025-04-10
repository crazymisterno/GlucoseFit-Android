package io.github.crazymisterno.GlucoseFit.data.storage

import android.text.format.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    fun Display(db: DataViewModel = hiltViewModel(), delete: () -> Unit) {
        val formatter = if (DateFormat.is24HourFormat(LocalContext.current)) {
            DateTimeFormatter.ofPattern("H:mm")
        }
        else {
            DateTimeFormatter.ofPattern("h:mm a")
        }
        val timedSetting by remember { db.getTimeSetting(time) }.collectAsState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .padding(vertical = 5.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.surface),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.padding(10.dp)) {
                Text(
                    time.format(formatter),
                    modifier = Modifier
                        .padding(5.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "$dose units",
                    modifier = Modifier
                        .padding(5.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Row(
                Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    if (timedSetting != null) {
                        Text(
                            "Insulin ratio: 1:${timedSetting!!.insulinToCarbRatio}",
                            modifier = Modifier
                                .padding(5.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "Correction dose: 1:${timedSetting!!.correctionDose}",
                            modifier = Modifier
                                .padding(5.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "Target Glucose: ${timedSetting!!.targetGlucose}",
                            modifier = Modifier
                                .padding(5.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    else {
                        Text(
                            "No setting",
                            modifier = Modifier
                                .padding(5.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Icon(
                    Icons.Filled.Delete,
                    "Delete entry",
                    tint = Color.Red,
                    modifier = Modifier
                        .padding(15.dp)
                        .clickable {
                            delete()
                        }
                )
            }
        }
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