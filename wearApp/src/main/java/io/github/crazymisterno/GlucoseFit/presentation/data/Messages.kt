package io.github.crazymisterno.GlucoseFit.presentation.data

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Serializable
data class MealLogEntry(
    val name: String,
    @Serializable(with = DateSerializer::class)
    val date: LocalDate,
    val id: Int = 0
)

@Serializable
data class FoodItem(
    val mealId: Int,
    val name: String,
    val carbs: Double,
    val calories: Double,
    val id: Int = 0
)

@Serializable
data class SavedFoodItem(
    val name: String,
    val carbs: Double,
    val calories: Double
)

@Serializable
data class DoseLogEntry(
    val units: Double,
    @Serializable(with = TimeSerializer::class)
    val time: LocalTime,
    val id: Int = 0
)