package io.github.crazymisterno.GlucoseFit.ui.content.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.crazymisterno.GlucoseFit.data.storage.DataViewModel
import io.github.crazymisterno.GlucoseFit.data.storage.FoodItem
import io.github.crazymisterno.GlucoseFit.data.storage.SavedFoodItem
import io.github.crazymisterno.GlucoseFit.ui.theme.textFieldColors

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SavedFoodView(mealId: Int, db: DataViewModel = hiltViewModel(), dialog: (SavedFoodItem) -> Unit, close: () -> Unit) {
    val focusManager = LocalFocusManager.current
    val interaction = remember { MutableInteractionSource() }
    var search by remember { mutableStateOf(TextFieldValue()) }
    val savedList by remember { db.savedFood }.collectAsState()
    val searchResult by db.searchSavedItems(search.text).collectAsState()
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(interaction, null) {
                focusManager.clearFocus()
            }
    ) {
        Text(
            "Saved Food",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 15.dp)
        )
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            label = { Text("Search Food") },
            colors = textFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
        )
        Column(
            Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.surfaceDim)
        ) {
            val loadList = if (search.text.isEmpty()) {
                savedList
            } else {
                searchResult
            }
            if (loadList.isEmpty()) {
                Text(
                    "Nothing here",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                )
            }
            LazyColumn(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxHeight(0.8f)
            ) {
                itemsIndexed(loadList) { index, item ->
                    ListItem(
                        headlineContent = { Text(item.name) },
                        modifier = Modifier.combinedClickable(
                            onClick = {
                                val imported = FoodItem(
                                    mealId = mealId,
                                    name = item.name,
                                    carbs = item.carbs,
                                    calories = item.calories
                                )
                                db.addFood(imported)
                                close()
                            },
                            onLongClick = {
                                dialog(item)
                            }
                        ),
                    )
                    if (index < loadList.size - 1)
                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}

