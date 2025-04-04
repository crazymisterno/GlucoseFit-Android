package io.github.crazymisterno.GlucoseFit.ui.content

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.crazymisterno.GlucoseFit.data.storage.DataViewModel
import io.github.crazymisterno.GlucoseFit.data.settings.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoseLog(model: SettingsViewModel = hiltViewModel(), db: DataViewModel = hiltViewModel()) {

}