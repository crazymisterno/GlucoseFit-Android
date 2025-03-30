package io.github.crazymisterno.GlucoseFit.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import io.github.crazymisterno.GlucoseFit.data.DataManager
import io.github.crazymisterno.GlucoseFit.ui.theme.GlucoseFitMaterialTheme
import java.time.LocalDate

@Composable
fun CalendarView(db: DataManager) {

}

@Preview
@Composable
fun PreviewCalendarView() {
    val db = Room.inMemoryDatabaseBuilder(
        LocalContext.current,
        DataManager::class.javaObjectType
    ).build()
    GlucoseFitMaterialTheme {
        CalendarView(db)
    }
}