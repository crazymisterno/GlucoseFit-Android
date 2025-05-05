package io.github.crazymisterno.GlucoseFit.data.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.crazymisterno.GlucoseFit.data.settings.TimeConverter
import io.github.crazymisterno.GlucoseFit.data.settings.TimedSettings
import io.github.crazymisterno.GlucoseFit.data.settings.TimedSettingsAccess
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Singleton


class DateConverter {
    private val formatter = DateTimeFormatter.ISO_DATE

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String {
        return date.format(formatter)
    }

    @TypeConverter
    fun toLocalDate(string: String): LocalDate {
        return string.let { str -> LocalDate.parse(str, formatter) }
    }
}

@Database(entities = [MealLogEntry::class, SavedFoodItem::class, FoodItem::class, TimedSettings::class, DoseLogEntry::class], version = 1)
@TypeConverters(DateConverter::class, TimeConverter::class)
abstract class DataManager : RoomDatabase() {
    abstract fun mealAccess(): MealAccess

    abstract fun savedFoodAccess(): SavedFoodAccess

    abstract fun timedSettingAccess(): TimedSettingsAccess

    abstract fun doseLogAccess(): DoseLogAccess
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseDI {
    @Provides
    @Singleton
    fun diskDatabase(@ApplicationContext context: Context): DataManager {
        return Room.databaseBuilder(
            context,
            DataManager::class.java,
            "glucosefitdata.db"
        ).build()
    }
}