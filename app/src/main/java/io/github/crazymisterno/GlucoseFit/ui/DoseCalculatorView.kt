package io.github.crazymisterno.GlucoseFit.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.crazymisterno.GlucoseFit.data.DataViewModel
import io.github.crazymisterno.GlucoseFit.data.SettingsViewModel
import io.github.crazymisterno.GlucoseFit.ui.theme.GlucoseFitMaterialTheme
import java.time.LocalDate


@Preview
@Composable
fun DoseCalculatorView(model: SettingsViewModel = hiltViewModel(), db: DataViewModel = hiltViewModel()) {
    val relevantMeals = db.mealsForToday.collectAsState(listOf())
}