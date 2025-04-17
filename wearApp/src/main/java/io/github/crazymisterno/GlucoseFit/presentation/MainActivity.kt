/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package io.github.crazymisterno.GlucoseFit.presentation

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import io.github.crazymisterno.GlucoseFit.presentation.data.MessagingManager
import io.github.crazymisterno.GlucoseFit.presentation.ui.theme.GlucoseFitTheme
import io.github.crazymisterno.GlucoseFit.presentation.ui.Navigator
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var messenger: MessagingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        messenger.start()
        lifecycleScope.launch {
            messenger.findPhone()
        }

        setContent {
            GlucoseFitTheme {
                Navigator()
            }
        }
    }
}

enum class NavTarget {
    Add,
    Import,
    Log,
    Select
}

