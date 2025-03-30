package io.github.crazymisterno.GlucoseFit.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import io.github.crazymisterno.GlucoseFit.data.DataManager
import io.github.crazymisterno.GlucoseFit.data.SettingsViewModel
import io.github.crazymisterno.GlucoseFit.ui.theme.GlucoseFitMaterialTheme
import java.time.LocalDate

@Composable
fun DoseCalculatorView(model: SettingsViewModel, db: DataManager, date: LocalDate) {
    val relevantMeals = db.mealAccess().getByDate(date)
}

@Preview
@Composable
fun PreviewDoseCalc() {
    val model = SettingsViewModel(PreviewSettings())
    val db = Room.inMemoryDatabaseBuilder(
        LocalContext.current,
        DataManager::class.javaObjectType
    ).build()
    GlucoseFitMaterialTheme {
        DoseCalculatorView(model, db, LocalDate.now())
    }
}