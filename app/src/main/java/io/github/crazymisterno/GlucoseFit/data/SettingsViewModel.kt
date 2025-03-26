package io.github.crazymisterno.GlucoseFit.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codelab.android.datastore.Settings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel (
    private val provider: SettingsProvider
) : ViewModel() {
    companion object {
        val genderOptions = listOf<String>("Male", "Female")
        val activityOptions = listOf<String>("Sedentary", "Lightly Active", "Active", "Very Active")
        val goalOptions = listOf<String>("Gain 1lb a week", "Lose 1lb a week", "Maintain Weight")
    }

    val settings: StateFlow<Settings> = provider.shared.map { preferences ->
        preferences
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        SettingsSerializer.defaultValue
    )

    fun updateSettings(
        weight: Double? = null,
        heightFeet: Double? = null,
        heightInches: Double? = null,
        age: Int? = null,
        gender: String? = null,
        activityLevel: String? = null,
        goal: String? = null,
        manualCalories: Double? = null,
        insulinToCarbRatio: Double? = null,
        correctionDose: Double? = null,
        targetGlucose: Double? = null,
        carbOnly: Boolean? = null
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
                carbOnly = carbOnly
            )
        }
    }
}