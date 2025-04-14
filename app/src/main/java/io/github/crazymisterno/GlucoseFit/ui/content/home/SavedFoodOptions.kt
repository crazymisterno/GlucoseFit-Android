package io.github.crazymisterno.GlucoseFit.ui.content.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.crazymisterno.GlucoseFit.data.storage.DataViewModel

@Composable
fun SavedFoodOptions(foodId: Int, mealId: Int, db: DataViewModel = hiltViewModel(), close: () -> Unit) {
    val food by remember { db.idSavedFood(foodId) }.collectAsState()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Text(
            "Options for " + food.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 15.dp)
        )

        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.surfaceBright)
                .clickable {
                    db.importFood(food, mealId)
                    Toast
                        .makeText(context, "Food imported", Toast.LENGTH_SHORT)
                        .show()
                    close()
                }
        ) {
            Icon(
                Icons.Filled.AddCircle,
                "Add",
                tint = Color.Green,
                modifier = Modifier
                    .padding(15.dp)
            )
            Text(
                "Add to food list",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(15.dp)
            )
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.surfaceBright)
                .clickable {
                    db.deleteSavedFood(food)
                    Toast
                        .makeText(context, "Food item deleted", Toast.LENGTH_SHORT)
                        .show()
                    close()
                }
        ) {
            Icon(
                Icons.Filled.Delete,
                "Delete",
                tint = Color.Red,
                modifier = Modifier
                    .padding(15.dp)
            )
            Text(
                "Delete from saved food list",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(15.dp)
            )
        }
    }
}
