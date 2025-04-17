package io.github.crazymisterno.GlucoseFit.presentation.ui.add.steps

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import io.github.crazymisterno.GlucoseFit.presentation.data.MealLogEntry
import io.github.crazymisterno.GlucoseFit.presentation.data.MessagingViewModel
import io.github.crazymisterno.GlucoseFit.presentation.ui.theme.LightBgGreen
import java.time.format.DateTimeFormatter

@Composable
fun SelectMeal(messenger: MessagingViewModel = hiltViewModel(), moveOn: (MealLogEntry) -> Unit) {
    var meals by remember { mutableStateOf(listOf<MealLogEntry>()) }
    var pagerState = rememberPagerState { meals.size }
    val formatter = DateTimeFormatter.ofPattern("MMM d, uuuu")

    LaunchedEffect(Unit) {
        Log.println(Log.INFO, "Message", "Running request")
        meals = messenger.requestMeals()
    }

    HorizontalPager(pagerState) { page ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                meals[page].name,
                style = MaterialTheme.typography.title2,
                color = MaterialTheme.colors.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))
            Text(
                meals[page].date.format(formatter),
                color = MaterialTheme.colors.onSurface,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = { moveOn(meals[page]) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = LightBgGreen
                ),
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .border(2.dp, Color.Gray, RoundedCornerShape(15.dp))
            ) {
                Text("Select")
            }
        }
    }
}
