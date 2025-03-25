package io.github.crazymisterno.GlucoseFit.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Settings(
    private val context: Context,
    var weight: Double,
    val heightFeet: Double,
    val heightInches: Double,
    val age: Int,
    val gender: String,
    val activityLevel: String,
    val goal: String,
    val manualCalories: Double,
    val insulinToCarbRatio: Double,
    val correctionDose: Double,
    val targetGlucose: Double,
    var carbOnly: Boolean
) {
    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        val Weight = doublePreferencesKey("weight")
        val HeightFeet = doublePreferencesKey("heightFeet")
        val HeightInches = doublePreferencesKey("heightInches")
        val Age = intPreferencesKey("age")
        val Gender = stringPreferencesKey("gender")
        val ActivityLevel = stringPreferencesKey("activityLevel")
        val Goal = stringPreferencesKey("goal")
        val ManualCalories = doublePreferencesKey("manualCalories")
        val InsulinToCarbRatio = doublePreferencesKey("insulinToCarbRatio")
        val CorrectionDose = doublePreferencesKey("correctionDose")
        val TargetGlucose = doublePreferencesKey("targetGlucose")
        val CarbOnly = booleanPreferencesKey("carbOnly")
    }
    constructor(context: Context) : this(context, 0.0, 0.0, 0.0, 0, "", "", "", 0.0, 1.0, 1.0, 0.0, false)

    val data: Flow<Settings> = context.dataStore.data.map { preferences ->
        val weight = preferences[Weight] ?: 0.0
        val heightFeet = preferences[HeightFeet] ?: 0.0
        val heightInches = preferences[HeightInches] ?: 0.0
        val age = preferences[Age] ?: 0
        val gender = preferences[Gender] ?: "Male"
        val activityLevel = preferences[ActivityLevel] ?: "Sedentary"
        val goal = preferences[Goal] ?: "Maintain weight"
        val manCalories = preferences[ManualCalories] ?: 0.0
        val insulinToCarb = preferences[InsulinToCarbRatio] ?: 1.0
        val correctionDose = preferences[CorrectionDose] ?: 1.0
        val targetGlucose = preferences[TargetGlucose] ?: 0.0
        val carbs = preferences[CarbOnly] ?: false

        Settings(
            weight = weight,
            heightFeet = heightFeet,
            heightInches = heightInches,
            age = age,
            gender = gender,
            activityLevel = activityLevel,
            goal = goal,
            manualCalories = manCalories,
            insulinToCarbRatio = insulinToCarb,
            correctionDose = correctionDose,
            targetGlucose = targetGlucose,
            carbOnly = carbs,
            context = context
        )
    }
}