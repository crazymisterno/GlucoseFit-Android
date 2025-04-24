package io.github.crazymisterno.GlucoseFit.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.crazymisterno.GlucoseFit.ui.content.MainActivity
import io.github.crazymisterno.GlucoseFit.data.settings.SettingsViewModel
import io.github.crazymisterno.GlucoseFit.ui.onboarding.steps.Disclaimer
import io.github.crazymisterno.GlucoseFit.ui.onboarding.steps.Finish
import io.github.crazymisterno.GlucoseFit.ui.onboarding.steps.InitalSetup
import io.github.crazymisterno.GlucoseFit.ui.onboarding.steps.TimedSettingsSetup
import io.github.crazymisterno.GlucoseFit.ui.onboarding.steps.WelcomeScreen
import io.github.crazymisterno.GlucoseFit.ui.theme.GlucoseFitMaterialTheme
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnBoarding : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val setting: SettingsViewModel = hiltViewModel()
            GlucoseFitMaterialTheme {
                OnBoardingMain {
                    lifecycleScope.launch {
                        setting.updateSettings(setupFinished = true)
                    }
                    startActivity(Intent(this@OnBoarding, MainActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }
        }
    }
}

@Composable
fun OnBoardingMain(done: () -> Unit) {
    var step by remember { mutableIntStateOf(0) }
    val scroll = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Column(
        Modifier
            .fillMaxSize()
            .background(Brush.horizontalGradient(listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.secondary
            )))
            .clickable {
                focusManager.clearFocus()
            },
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedContent(
            targetState = step,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.surface)
                .verticalScroll(scroll),
            transitionSpec = {
                (fadeIn(tween(200)) + expandVertically(tween(200))) togetherWith
                        (fadeOut(tween(200)) + shrinkVertically(tween(200)))
            }
        ) {
            when (it) {
                0 -> Disclaimer { step++ }
                1 -> WelcomeScreen { step++ }
                2 -> InitalSetup { step++ }
                3 -> TimedSettingsSetup { step++ }
                4 -> Finish { done() }
            }
        }
    }
}