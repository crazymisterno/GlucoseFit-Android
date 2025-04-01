package io.github.crazymisterno.GlucoseFit.ui.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.crazymisterno.GlucoseFit.data.storage.DataViewModel
import io.github.crazymisterno.GlucoseFit.data.settings.SettingsViewModel

@Preview
@Composable
fun DoseCalculatorView(model: SettingsViewModel = hiltViewModel(), db: DataViewModel = hiltViewModel()) {
    val relevantMeals = db.mealsForToday.collectAsState(listOf())
}