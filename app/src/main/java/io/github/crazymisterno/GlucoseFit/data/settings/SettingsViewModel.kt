package io.github.crazymisterno.GlucoseFit.data.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.crazymisterno.GlucoseFit.data.proto.Settings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    fun updateSettings(
        manualCalories: String? = null,
        carbOnly: Boolean? = null,
        setupFinished: Boolean? = null
    ) {
        viewModelScope.launch {
            provider.updateSettings(
                manualCalories = manualCalories,
                carbOnly = carbOnly,
                setupComplete = setupFinished
            )
        }
    }

    var manualCaloriesValue by mutableStateOf(TextFieldValue(
        text = settings.value.manualCalories,
        selection = TextRange(settings.value.manualCalories.length)
    ))
}