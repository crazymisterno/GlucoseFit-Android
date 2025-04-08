package io.github.crazymisterno.GlucoseFit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import io.github.crazymisterno.GlucoseFit.ui.content.DoseLog
import io.github.crazymisterno.GlucoseFit.ui.content.calendar.CalendarNavigator
import io.github.crazymisterno.GlucoseFit.ui.content.home.HomeView
import io.github.crazymisterno.GlucoseFit.ui.content.settings.SettingsNavigator
import kotlinx.coroutines.launch
import java.time.LocalDate

@Preview
@Composable
fun ContentView() {
    val tabNames = listOf("Settings", "Home", "Dose Log", "Calendar")
    var pagerState = rememberPagerState(1) { tabNames.size }
    val scope = rememberCoroutineScope()
    Column {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
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
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
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
        HorizontalPager(state = pagerState) { state ->
            when (state) {
                0 -> SettingsNavigator()
                1 -> HomeView(LocalDate.now())
                2 -> DoseLog(LocalDate.now())
                3 -> CalendarNavigator()
            }
        }
    }
}