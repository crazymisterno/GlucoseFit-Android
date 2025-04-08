package io.github.crazymisterno.GlucoseFit.ui.content.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import io.github.crazymisterno.GlucoseFit.ui.content.DoseLog
import io.github.crazymisterno.GlucoseFit.ui.content.home.HomeView
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailAtDay(date: LocalDate) {
    val tabNames = listOf("Meal Log", "Dose Log")
    var pagerState = rememberPagerState(0) { tabNames.size }
    val scope = rememberCoroutineScope()
    Column {
        SecondaryTabRow(pagerState.currentPage) {
            tabNames.forEachIndexed { index, name ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    selectedContentColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    unselectedContentColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    text = { Text(name) }
                )
            }
        }

        HorizontalPager(pagerState) { state ->
            when (state) {
                0 -> HomeView(date)
                1 -> DoseLog(date)
            }
        }
    }
}