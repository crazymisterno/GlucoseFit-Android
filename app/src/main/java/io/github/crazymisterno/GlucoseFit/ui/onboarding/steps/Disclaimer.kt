package io.github.crazymisterno.GlucoseFit.ui.onboarding.steps

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.crazymisterno.GlucoseFit.ui.theme.DarkBgGreen
import io.github.crazymisterno.GlucoseFit.ui.theme.GlucoseFitMaterialTheme
import io.github.crazymisterno.GlucoseFit.ui.theme.LightBgBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Disclaimer(moveOn: () -> Unit) {
    var activateAcknowledge by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val containerAnimation = animateColorAsState(
        targetValue = if (activateAcknowledge) LightBgBlue else Color.Gray,
        animationSpec = tween(500)
    )
    val contentAnimation = animateColorAsState(
        targetValue = if (activateAcknowledge) DarkBgGreen else Color.White,
        animationSpec = tween(500)
    )

    LaunchedEffect(Unit) {
        scope.launch {
            activateAcknowledge = false
            delay(5000)
            activateAcknowledge = true
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Read Before Continuing",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 30.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        )
        Text(
            "GlucoseFit is intended for informational and educational purposes only. It does not provide medical advice, diagnosis, or treatment. Always consult with your doctor or diabetes care team before making any changes to your insulin regimen, diet, or treatment plan. Never disregard professional medical advice or delay seeking it because of information provided by this app. By using GlucoseFit, you acknowledge that you understand and agree to these terms.",
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(15.dp)
        )

            Button(
                onClick = { moveOn() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = containerAnimation.value,
                    contentColor = contentAnimation.value
                ),
                enabled = activateAcknowledge,
            ) {
                Text("I Understand and Agree")
            }
    }
}

@Preview
@Composable
fun PreviewDisclaimer() {
    GlucoseFitMaterialTheme {
        Disclaimer { }
    }
}