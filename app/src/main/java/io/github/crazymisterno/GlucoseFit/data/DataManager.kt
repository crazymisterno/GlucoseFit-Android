package io.github.crazymisterno.GlucoseFit.data

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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Qualifier


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

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class InMemoryDb

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class InStorageDb

@Module
@InstallIn(SingletonComponent::class)
object DatabaseDI {
    // Use this annotation for in-memory database for development
    @InMemoryDb
    @Provides
    fun memDatabase(@ApplicationContext context: Context): DataManager {
        return Room.inMemoryDatabaseBuilder(
            context,
            DataManager::class.java,
        ).build()
    }

    // Use this annotation for on-disk database for production
    @InStorageDb
    @Provides
    fun diskDatabase(@ApplicationContext context: Context): DataManager {
        return Room.databaseBuilder(
            context,
            DataManager::class.java,
            "glucosefitdata.db"
        ).build()
    }
}