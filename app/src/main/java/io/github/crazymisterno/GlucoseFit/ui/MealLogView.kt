package io.github.crazymisterno.GlucoseFit.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.crazymisterno.GlucoseFit.data.DataViewModel
import io.github.crazymisterno.GlucoseFit.data.FoodItem
import io.github.crazymisterno.GlucoseFit.dev.PreviewIds
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MealLogView(
    @PreviewParameter(PreviewIds::class) mealId: Int,
    db: DataViewModel = hiltViewModel(),
    navigate: (Int, Int) -> Unit
) {
    val meal by remember { db.idMeal(mealId) }.collectAsState()
    Log.println(Log.INFO, "Message", "Recomposing")
    Column(
        modifier = Modifier
            .background(Brush.horizontalGradient(listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.secondary
            )))
            .fillMaxSize()
            .padding(top = 15.dp)
            .padding(horizontal = 15.dp)
            .navigationBarsPadding()
    ) {
        Text(
            meal.meal.name + " for " +
            meal.meal.date.month?.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.US) + " " +
            meal.meal.date.dayOfMonth + ", " +
            meal.meal.date.year,
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 40.sp
        )
        Spacer(Modifier.height(20.dp))
        FoodList(meal.food)
        Spacer(Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(Color.Green)
                .clickable {
                    navigate(0, mealId)
                },
        ) {
            Text(
                "Add Food",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
            )
        }
        Spacer(Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(Color(255, 148, 0))
                .clickable {
                    navigate(1, mealId)
                }
        ) {
            Text(
                "Add from Saved Foods",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun FoodList(list: List<FoodItem>, db: DataViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
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
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .padding(bottom = 15.dp)
        ) {
            items(list, { food -> food.id }) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .padding(vertical = 10.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(MaterialTheme.colorScheme.surface),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        Modifier
                            .padding(10.dp),
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            item.name,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            item.carbs.toString() + "g carbs, " +
                            item.calories + " cal",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.Gray
                        )
                    }
                    Row(
                        Modifier
                            .padding(10.dp)
                    ) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "Delete Button",
                            tint = Color.Red,
                            modifier = Modifier
                                .clickable {
                                    db.deleteFood(item)
                                }
                        )
                        Spacer(Modifier.width(5.dp))
                        Icon(
                            Icons.Filled.AddCircle,
                            contentDescription = "Save Button",
                            tint = Color.Green,
                            modifier = Modifier
                                .clickable {
                                    db.saveFood(item)
                                }
                        )
                    }
                }
            }
        }
    }
}