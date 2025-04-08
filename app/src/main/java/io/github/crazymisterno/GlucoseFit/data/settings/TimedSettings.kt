package io.github.crazymisterno.GlucoseFit.data.settings

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverter
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "timedSettings")
data class TimedSettings(
    var startTime: LocalTime,
    var insulinToCarbRatio: String,
    var correctionDose: String,
    var targetGlucose: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

@Dao
interface TimedSettingsAccess {
    @Insert
    suspend fun addNew(config: TimedSettings): Long

    @Delete
    suspend fun delete(settings: TimedSettings)

    @Query("DELETE FROM timedSettings where id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM timedSettings")
    fun getAll(): Flow<List<TimedSettings>>

    @Query("""
        SELECT * FROM timedSettings 
        WHERE startTime <= current_time
        ORDER BY startTime DESC
        LIMIT 1
    """)
    fun getCurrentSetting(): Flow<TimedSettings>

    @Query("""
        SELECT * FROM timedSettings
        WHERE startTime <= :time
        ORDER BY startTime DESC
        LIMIT 1
    """)
    fun getByTime(time: LocalTime): Flow<TimedSettings>

    @Update
    suspend fun changeConfig(settings: TimedSettings)

    @Query("SELECT * FROM timedSettings WHERE id = :id")
    fun getById(id: Int): Flow<TimedSettings>
}

class TimeConverter {
    private val formatter = DateTimeFormatter.ISO_TIME
    @TypeConverter
    fun fromLocalTime(time: LocalTime): String {
        return time.format(formatter)
    }

    @TypeConverter
    fun toLocalTime(string: String): LocalTime {
        return LocalTime.parse(string, formatter)
    }
}
