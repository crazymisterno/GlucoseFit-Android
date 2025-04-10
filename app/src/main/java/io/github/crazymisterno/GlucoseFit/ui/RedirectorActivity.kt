package io.github.crazymisterno.GlucoseFit.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.crazymisterno.GlucoseFit.ui.content.MainActivity
import io.github.crazymisterno.GlucoseFit.data.settings.SettingsViewModel
import io.github.crazymisterno.GlucoseFit.ui.onboarding.OnBoarding
import io.github.crazymisterno.GlucoseFit.ui.theme.GlucoseFitMaterialTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RedirectorActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val setting: SettingsViewModel = hiltViewModel()
            LaunchedEffect(Unit) {
                lifecycleScope.launch {
                    delay(500)
                    val initSettings = setting.settings.value.setUpComplete

                    val activity = if (initSettings) {
                        MainActivity::class.java
                    } else {
                        OnBoarding::class.java
                    }

                    startActivity(Intent(this@RedirectorActivity, activity))
                }
            }
            GlucoseFitMaterialTheme {
                Box(
                    Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )))
                )
            }
        }
    }
}