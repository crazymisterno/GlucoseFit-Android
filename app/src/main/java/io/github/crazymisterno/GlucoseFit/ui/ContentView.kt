package io.github.crazymisterno.GlucoseFit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import io.github.crazymisterno.GlucoseFit.data.DataManager
import io.github.crazymisterno.GlucoseFit.data.SettingsViewModel
import java.time.LocalDate

@Composable
fun ContentView(model: SettingsViewModel, db: DataManager) {
    var selectedTab by remember { mutableIntStateOf(1) }

    val tabNames = listOf("Settings", "Home", "Dose Calculator", "Calendar")
    Column {
        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(listOf<Color>(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.9f)
                    ))
                )
        ) {
            tabNames.forEachIndexed { index, name ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(name) },
                    selectedContentColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    unselectedContentColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    modifier = Modifier.statusBarsPadding()
                        .background(Brush.horizontalGradient(listOf<Color>(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.0f),
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.0f)
                        )))
                )
            }
        }
        when (selectedTab) {
            0 -> SettingsView(model)
            1 -> HomeView(model, db, LocalDate.now())
            2 -> DoseCalculatorView(model, db, LocalDate.now())
            3 -> CalendarView(db)
        }
    }
}


@Preview
@Composable
fun PreviewContent() {
    val model = SettingsViewModel(PreviewSettings())
    val db = Room.inMemoryDatabaseBuilder(
        LocalContext.current,
        DataManager::class.javaObjectType
    ).build()
    ContentView(model, db)
}