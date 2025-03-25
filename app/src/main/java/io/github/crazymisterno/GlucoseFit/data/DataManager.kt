package io.github.crazymisterno.GlucoseFit.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.LocalDate
import java.time.format.DateTimeFormatter


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

@Database(entities = [MealLogEntry::class, SavedFoodItem::class, FoodItem::class, CalendarEntry::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class DataManager : RoomDatabase() {
    abstract fun mealAccess(): MealAccess

    abstract fun savedFoodAccess(): SavedFoodAccess

    abstract fun calendarAccess(): CalendarAccess
}