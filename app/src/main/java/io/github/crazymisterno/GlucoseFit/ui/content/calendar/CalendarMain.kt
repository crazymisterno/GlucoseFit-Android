package io.github.crazymisterno.GlucoseFit.ui.content.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.crazymisterno.GlucoseFit.ui.theme.datePickerColors
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarMain(navigate: (Int, Int, Int) -> Unit) {
    var state = rememberDatePickerState(initialSelectedDateMillis = Date.from(Instant.now()).time)
    var scroll = rememberScrollState()
    Column(
        Modifier
            .fillMaxSize()
            .background(Brush.horizontalGradient(listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.secondary
            )))
            .verticalScroll(scroll)
    ) {
        Text(
            text = "Calendar",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally)
        )

        DatePicker(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clip(RoundedCornerShape(15.dp)),
            colors = datePickerColors()
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color.Blue)
                .clickable {
                    state.selectedDateMillis?.let {
                        val date = Instant.ofEpochMilli(it)
                            .atZone(ZoneOffset.UTC)
                            .withZoneSameLocal(ZoneId.systemDefault())
                            .toLocalDate()
                        navigate(date.year, date.month.value, date.dayOfMonth)
                    }
                }
        ) {
            Text(
                "View Details",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            )
        }
    }
}