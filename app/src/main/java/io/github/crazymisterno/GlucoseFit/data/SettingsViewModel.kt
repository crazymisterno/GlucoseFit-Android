package io.github.crazymisterno.GlucoseFit.data

import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.crazymisterno.GlucoseFit.data.proto.ActivityLevel
import io.github.crazymisterno.GlucoseFit.data.proto.GenderOption
import io.github.crazymisterno.GlucoseFit.data.proto.Goal
import io.github.crazymisterno.GlucoseFit.data.proto.Settings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val provider: SettingsProvider
) : ViewModel() {

    val settings: StateFlow<Settings> = provider.shared.map { preferences ->
        preferences
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        SettingsSerializer.defaultValue
    )
    var recommendedCalories by mutableIntStateOf(evaluateRecommendedCalories())
    var finalCalories by mutableIntStateOf(evaluateFinalCalories())

     fun evaluateRecommendedCalories(): Int {
        try {
            val weightLb = weightValue.text.toDoubleOrNull() ?: 0.0
            val heightFeet = heightFeetValue.text.toDoubleOrNull() ?: 0.0
            val heightInches = heightInchesValue.text.toDoubleOrNull() ?: 0.0
            val age = ageValue.text.toIntOrNull() ?: 0
            val weightKg = weightLb.times(0.453592)
            val heightCm = (heightFeet * 12.0 + heightInches) * 2.54
            val bmr = when (settings.value.gender) {
                GenderOption.Male -> (10 * weightKg) + (6.25 * heightCm) - (5 * age) + 5
                GenderOption.Female -> (10 * weightKg) + (6.25 * heightCm) - (5 * age) - 161
                GenderOption.UNRECOGNIZED -> 0.0
            }
            val activityMultiplier = when (settings.value.activityLevel) {
                ActivityLevel.Sedentary -> 1.2
                ActivityLevel.LightlyActive -> 1.375
                ActivityLevel.Active -> 1.55
                ActivityLevel.VeryActive -> 1.725
                ActivityLevel.UNRECOGNIZED -> 1.2
            }
            val tdee = bmr * activityMultiplier
            return when (settings.value.goal) {
                Goal.Maintain -> tdee.toInt()
                Goal.GainOne -> tdee.toInt() + 500
                Goal.LoseOne -> tdee.toInt() - 500
                Goal.UNRECOGNIZED -> tdee.toInt()
            }
        } catch (ex: Exception) {
            print(ex)
            return 0
        }
    }


    fun evaluateFinalCalories(): Int {
        val manualCalorieVal = settings.value.manualCalories.toIntOrNull() ?: 0
        if (manualCalorieVal > 0)
            return manualCalorieVal
        else
            return recommendedCalories
    }

    fun updateSettings(
        weight: String? = null,
        heightFeet: String? = null,
        heightInches: String? = null,
        age: String? = null,
        gender: GenderOption? = null,
        activityLevel: ActivityLevel? = null,
        goal: Goal? = null,
        manualCalories: String? = null,
        insulinToCarbRatio: String? = null,
        correctionDose: String? = null,
        targetGlucose: String? = null,
        carbOnly: Boolean? = null,
    ) {
        viewModelScope.launch {
            provider.updateSettings(
                weight = weight,
                heightFeet = heightFeet,
                heightInches = heightInches,
                age = age,
                gender = gender,
                activityLevel = activityLevel,
                goal = goal,
                manualCalories = manualCalories,
                insulinToCarbRatio = insulinToCarbRatio,
                correctionDose = correctionDose,
                targetGlucose = targetGlucose,
                carbOnly = carbOnly,
            )
        }
    }

    var weightValue by mutableStateOf(TextFieldValue(
        text = settings.value.weight,
        selection = TextRange(settings.value.weight.length)
    ))
    var heightFeetValue by mutableStateOf(TextFieldValue(
        text = settings.value.heightFeet,
        selection = TextRange(settings.value.heightFeet.length)
    ))
    var heightInchesValue by mutableStateOf( TextFieldValue(
        text = settings.value.heightInches,
        selection = TextRange(settings.value.heightInches.length)
    ))
    var ageValue by mutableStateOf(TextFieldValue(
        text = settings.value.age,
        selection = TextRange(settings.value.age.length)
    ))
    var manualCaloriesValue by mutableStateOf(TextFieldValue(
        text = settings.value.manualCalories,
        selection = TextRange(settings.value.manualCalories.length)
    ))
    var insulinToCarbRatioValue by mutableStateOf(TextFieldValue(
        text = settings.value.insulinToCarbRatio,
        selection = TextRange(settings.value.insulinToCarbRatio.length)
    ))
    var correctionDoseValue by mutableStateOf(TextFieldValue(
        text = settings.value.correctionDose,
        selection = TextRange(settings.value.correctionDose.length)
    ))
    var targetGlucoseValue by mutableStateOf(TextFieldValue(
        text = settings.value.targetGlucose,
        selection = TextRange(settings.value.targetGlucose.length)
    ))
}