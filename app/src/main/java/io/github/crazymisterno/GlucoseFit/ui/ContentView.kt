package io.github.crazymisterno.GlucoseFit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.tooling.preview.Preview
import java.time.LocalDate

@Preview
@Composable
fun ContentView() {
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
            0 -> SettingsView()
            1 -> HomeView(LocalDate.now())
            2 -> DoseCalculatorView()
            3 -> CalendarView()
        }
    }
}