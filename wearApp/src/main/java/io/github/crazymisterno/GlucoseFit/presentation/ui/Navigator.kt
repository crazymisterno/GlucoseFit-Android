package io.github.crazymisterno.GlucoseFit.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import io.github.crazymisterno.GlucoseFit.presentation.NavTarget
import io.github.crazymisterno.GlucoseFit.presentation.ui.add.AddFoodNavigator
import io.github.crazymisterno.GlucoseFit.presentation.ui.theme.LightBgGreen

@Composable
fun Navigator() {
    val navigator = rememberNavController()
    val graph = navigator.createGraph(startDestination = "home") {
        composable("home") {
            Home { target ->
                when (target) {
                    NavTarget.Add -> navigator.navigate("add")
                    NavTarget.Import -> navigator.navigate("import")
                    NavTarget.Log -> navigator.navigate("log")
                    NavTarget.Select -> navigator.navigate("select")
                }
            }
        }

        dialog("select") {
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                        .background(MaterialTheme.colors.surface)
                ) {
                    Text(
                        "Select a strategy",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp)
                        ,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { navigator.navigate("add") },
                            modifier = Modifier
                                .width(80.dp)
                                .border(2.dp, Color.Gray, RoundedCornerShape(15.dp)),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = LightBgGreen
                            ),
                            shape = RoundedCornerShape(15.dp)
                        ) {
                            Text(
                                "Add food",
                                textAlign = TextAlign.Center
                            )
                        }
                        Button(
                            onClick = { navigator.navigate("import") },
                            modifier = Modifier
                                .width(80.dp)
                                .border(2.dp, Color.Gray, RoundedCornerShape(15.dp)),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = LightBgGreen
                            ),
                            shape = RoundedCornerShape(15.dp)
                        ) {
                            Text(
                                "Import",
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        composable("add") {
            AddFoodNavigator {
                navigator.popBackStack()
            }
        }

        composable("import") {

        }

        composable("log") {

        }
    }

    NavHost(navigator, graph)
}