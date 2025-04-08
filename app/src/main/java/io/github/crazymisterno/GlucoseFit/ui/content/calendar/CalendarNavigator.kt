package io.github.crazymisterno.GlucoseFit.ui.content.calendar

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
private object Picker
@Serializable
private data class LogAtDay(
    val year: Int,
    val month: Int,
    val day: Int
)

@Composable
fun CalendarNavigator() {
    val navigator = rememberNavController()
    val graph = navigator.createGraph(startDestination = Picker) {
        composable<Picker> {
            CalendarMain { year, month, day ->
                navigator.navigate(LogAtDay(year, month, day))
            }
        }

        composable<LogAtDay> {
            val props = it.toRoute<LogAtDay>()
            DetailAtDay(LocalDate.of(props.year, props.month, props.day))
        }
    }

    NavHost(navigator, graph)
}