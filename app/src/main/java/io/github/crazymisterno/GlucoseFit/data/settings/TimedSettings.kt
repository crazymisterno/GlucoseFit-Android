package io.github.crazymisterno.GlucoseFit.data.settings

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverter
import androidx.room.Update
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalTime

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

    @Query("SELECT * FROM timedSettings ORDER BY startTime")
    fun getAll(): Flow<List<TimedSettings>>

    @Query("SELECT * FROM timedSettings")
    suspend fun checkOnce(): List<TimedSettings>

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
    suspend fun getByTime(time: LocalTime): TimedSettings?

    @Query("""
        SELECT * FROM timedSettings
        ORDER BY startTime DESC
        LIMIT 1
    """)
    suspend fun getLastSetting(): TimedSettings?

    fun findActive(time: LocalTime): Flow<TimedSettings?> {
        return flow {
            val firstQuery = getByTime(time)
            if (firstQuery == null) {
                emit(getLastSetting())
            }
            else {
                emit(firstQuery)
            }
            delay(1000)
        }
    }

    fun findActive(): Flow<TimedSettings?> {
        return flow {
            while (true) {
                val firstQuery = getByTime(LocalTime.now())
                if (firstQuery == null) {
                    emit(getLastSetting())
                }
                else {
                    emit(firstQuery)
                }
            }
        }
    }

    @Update
    suspend fun changeConfig(settings: TimedSettings)

    @Query("SELECT * FROM timedSettings WHERE id = :id")
    fun getById(id: Int): Flow<TimedSettings>
}

class TimeConverter {
    @TypeConverter
    fun fromLocalTime(time: LocalTime): Int {
        return time.toSecondOfDay()
    }

    @TypeConverter
    fun toLocalTime(second: Int): LocalTime {
        return LocalTime.ofSecondOfDay(second.toLong())
    }
}
