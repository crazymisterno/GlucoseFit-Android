package io.github.crazymisterno.GlucoseFit.ui.theme

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import io.github.crazymisterno.GlucoseFit.R
import io.github.crazymisterno.GlucoseFit.data.Settings
import kotlinx.coroutines.launch

@Composable
fun SettingsView(context: Context) {
    val scope = rememberCoroutineScope()
    var config by rememberSaveable { mutableStateOf(Settings(context).data) }
    val font = Font(
        R.font.inter_variablefont_opsz_wght,
        weight = FontWeight.Normal,
        style = FontStyle.Normal
    )
    val interFamily = FontFamily(font)
    val brush = Brush.horizontalGradient(
        listOf<Color>(
            Color(0.33f, 0.62f, 0.68f),
            Color(0.6f, 0.89f, 0.75f)
        )
    )

    Column(
        Modifier
            .background(brush)
            .fillMaxSize()
            .verticalScroll(ScrollState(0), enabled = true)
            .padding(top = Dp(10f))
            .padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(10.dp))
                .background(Color(1f, 1f, 1f, 0.7f))
                .padding(Dp(15f))
        ) {
            Text(
                text = "Settings",
                fontSize = TextUnit(34f, TextUnitType.Sp),
                fontFamily = interFamily,
                color = Color.Black,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Enable carb-only view",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = TextUnit(17f, TextUnitType.Sp),
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                )
                Switch(
                    checked = config.collectAsState(Settings(context)).value.carbOnly,
                    onCheckedChange = { newVal ->
                        scope.launch {
                            config.collect { collector ->
                                collector.carbOnly = true
                            }
                        }
                    },
                )
            }
            Column {
                Text(
                    "Weight (lbs)",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = TextUnit(17f, TextUnitType.Sp),
                )
                TextField(
                    value = TextFieldValue(config.collectAsState(Settings(context)).value.weight.toString()),
                    onValueChange = { newVal: TextFieldValue ->
                        scope.launch {
                            config.collect { collector ->
                                collector.weight = newVal.text.toDouble()
                            }
                        }
                    },
                    label = @Composable { Text("Enter Weight (lbs)") }
                )
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    val localContext = LocalContext.current
    SettingsView(localContext)
}