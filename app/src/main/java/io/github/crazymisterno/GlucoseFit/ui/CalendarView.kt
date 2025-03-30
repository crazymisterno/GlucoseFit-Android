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
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.crazymisterno.GlucoseFit.data.DataViewModel

@Preview
@Composable
fun CalendarView(db: DataViewModel = hiltViewModel()) {

}