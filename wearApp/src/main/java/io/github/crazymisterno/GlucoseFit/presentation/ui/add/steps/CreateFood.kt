package io.github.crazymisterno.GlucoseFit.presentation.ui.add.steps

import android.app.RemoteInput
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.input.RemoteInputIntentHelper
import io.github.crazymisterno.GlucoseFit.presentation.ui.theme.LightBgGreen

@Composable
fun CreateFood(moveOn: (String, Double, Double) -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.i("Message", "Handling result")
        val inputResults = RemoteInput.getResultsFromIntent(result.data)
        val name = inputResults?.getCharSequence("name")?.toString()
        val carbs = inputResults?.getCharSequence("carbs")?.toString()?.toDoubleOrNull()
        val calories = inputResults?.getCharSequence("cals")?.toString()?.toDoubleOrNull()

        if (name != null && carbs != null && calories != null) {
            moveOn(name, carbs, calories)
        }
        else {
            Log.i("Message", "One of the inputs was null")
        }
    }

    Column(verticalArrangement = Arrangement.Center) {
        Text(
            "Provide food details in the following prompts",
            style = MaterialTheme.typography.title2,
            color = MaterialTheme.colors.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(10.dp))
        Button(
            onClick = {
                val intent = RemoteInputIntentHelper.createActionRemoteInputIntent()

                val remoteInputs = listOf(
                    RemoteInput.Builder("name")
                        .setLabel("Enter name")
                        .build(),
                    RemoteInput.Builder("carbs")
                        .setLabel("Enter carb content")
                        .build(),
                    RemoteInput.Builder("cals")
                        .setLabel("Enter calorie content")
                        .build()
                )

                RemoteInputIntentHelper.putRemoteInputsExtra(
                    intent,
                    remoteInputs
                )

                launcher.launch(intent)
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = LightBgGreen
            ),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
        ) {
            Text("Enter food details")
        }
    }
}