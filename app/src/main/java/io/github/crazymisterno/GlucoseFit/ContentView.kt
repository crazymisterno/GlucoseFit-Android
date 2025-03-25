package io.github.crazymisterno.GlucoseFit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun ContentView() {
    var selectedTab by remember { mutableIntStateOf(1) }

    val tabNames = listOf("Settings", "Home", "Dose Calculator", "Calendar")

    Column(modifier = Modifier.fillMaxSize()) {
        when (selectedTab) {

        }
        TabRow(selectedTabIndex = selectedTab) {
            tabNames.forEachIndexed { index, name ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(name) },
                )
            }
        }
    }
}