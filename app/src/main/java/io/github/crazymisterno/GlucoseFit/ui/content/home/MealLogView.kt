package io.github.crazymisterno.GlucoseFit.ui.content.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.crazymisterno.GlucoseFit.data.storage.DataViewModel
import io.github.crazymisterno.GlucoseFit.data.storage.FoodItem
import java.time.format.DateTimeFormatter

@Composable
fun MealLogView(
    mealId: Int,
    db: DataViewModel = hiltViewModel(),
    navigate: (Int, Int) -> Unit
) {
    val meal by remember { db.idMeal(mealId) }.collectAsState()
    val formatter = DateTimeFormatter.ofPattern("MMM d, uuuu")
    Column(
        modifier = Modifier
            .background(Brush.horizontalGradient(listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.secondary
            )))
            .fillMaxSize()
            .padding(top = 15.dp)
            .padding(horizontal = 15.dp)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            "${meal.meal.name} for ${meal.meal.date.format(formatter)}",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp,
            modifier = Modifier.fillMaxWidth(),
        )
        FoodList(meal.food)
        Button(
            onClick = { navigate(0, mealId) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Green,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Add Food",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(5.dp)
            )
        }
        Button(
            onClick = { navigate(1, mealId)},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(255, 148, 0),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Add from Saved Foods",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(5.dp)
            )
        }
    }
}

@Composable
fun FoodList(list: List<FoodItem>, db: DataViewModel = hiltViewModel()) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.surfaceDim)
    ) {
        Text(
            "Food List",
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .padding(top = 15.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
        if (list.isEmpty()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
                    .padding(vertical = 10.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Text(
                    "No food logged for this meal",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                )
            }
        }
        else {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .padding(bottom = 15.dp)
                    .clip(RoundedCornerShape(15.dp))
            ) {
                itemsIndexed(list) { index, item ->
                    item.Display(
                        modifier = Modifier
                            .clip(
                                if (index == 0 && list.size == 1)
                                    RoundedCornerShape(15.dp)
                                else if (index == 0)
                                    RoundedCornerShape(topEnd = 15.dp, topStart = 15.dp)
                                else if (index == list.size - 1)
                                    RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)
                                else
                                    RoundedCornerShape(0.dp)
                            ),
                        {
                            db.deleteFood(item)
                            Toast
                                .makeText(context, "Food deleted", Toast.LENGTH_SHORT)
                                .show()
                        },
                        {
                            db.saveFood(item)
                            Toast
                                .makeText(context, "Food saved", Toast.LENGTH_SHORT)
                                .show()
                        }
                    )
                    if (index < list.size - 1)
                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}