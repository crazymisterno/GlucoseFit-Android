package io.github.crazymisterno.GlucoseFit.dev

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import java.time.LocalDate

class PreviewDate : PreviewParameterProvider<LocalDate> {
    override val values: Sequence<LocalDate> = sequenceOf(
        LocalDate.now()
    )
}