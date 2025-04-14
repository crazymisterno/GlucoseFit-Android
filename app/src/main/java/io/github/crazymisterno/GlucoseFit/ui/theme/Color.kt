package io.github.crazymisterno.GlucoseFit.ui.theme

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TimePickerColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LightBgBlue = Color(0.33f, 0.62f, 0.68f)
val LightBgGreen = Color(0.6f, 0.89f, 0.75f)
val DarkBgBlue = Color(0.18f, 0.23f, 0.28f)
val DarkBgGreen = Color(0.25f, 0.3f, 0.35f)

val LightCardBg = Color.White.copy(alpha = 0.7f)
val DarkCardBg = Color(0.25f, 0.25f, 0.3f)

val LightAccent = Color.Blue
val DarkAccent = Color.Blue.copy(alpha = 0.8f)

val LightSuccess = Color.Green
val DarkSuccess = Color.Green.copy(alpha = 0.8f)

@Composable
fun textFieldColors(): TextFieldColors {
    return TextFieldColors(
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        disabledTextColor = MaterialTheme.colorScheme.onSurface,
        errorTextColor = MaterialTheme.colorScheme.onSurface,
        focusedContainerColor = MaterialTheme.colorScheme.surfaceBright,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceBright,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceBright,
        errorContainerColor = MaterialTheme.colorScheme.surfaceBright,
        cursorColor = LightBgGreen,
        errorCursorColor = MaterialTheme.colorScheme.error,
        textSelectionColors = getSelectionColors(),
        focusedIndicatorColor = LightBgGreen,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
        disabledIndicatorColor = MaterialTheme.colorScheme.onSurface,
        errorIndicatorColor = MaterialTheme.colorScheme.error,
        focusedLeadingIconColor = LightBgBlue,
        unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
        errorLeadingIconColor = MaterialTheme.colorScheme.error,
        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurface,
        focusedTrailingIconColor = LightBgBlue,
        unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,
        errorTrailingIconColor = MaterialTheme.colorScheme.error,
        focusedLabelColor = LightBgBlue,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
        disabledLabelColor = MaterialTheme.colorScheme.onSurface,
        errorLabelColor = MaterialTheme.colorScheme.error,
        focusedPlaceholderColor = DarkBgBlue,
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurface,
        errorPlaceholderColor = MaterialTheme.colorScheme.error,
        focusedSupportingTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedSupportingTextColor = MaterialTheme.colorScheme.onSurface,
        disabledSupportingTextColor = MaterialTheme.colorScheme.onSurface,
        errorSupportingTextColor = MaterialTheme.colorScheme.onSurface,
        focusedPrefixColor = MaterialTheme.colorScheme.onSurface,
        unfocusedPrefixColor = MaterialTheme.colorScheme.onSurface,
        disabledPrefixColor = MaterialTheme.colorScheme.onSurface,
        errorPrefixColor = MaterialTheme.colorScheme.onSurface,
        focusedSuffixColor = MaterialTheme.colorScheme.onSurface,
        unfocusedSuffixColor = MaterialTheme.colorScheme.onSurface,
        disabledSuffixColor = MaterialTheme.colorScheme.onSurface,
        errorSuffixColor = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun getSelectionColors(): TextSelectionColors {
    return TextSelectionColors(
        handleColor = LightBgGreen,
        backgroundColor = LightBgBlue
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun datePickerColors(): DatePickerColors {
    return DatePickerColors(
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        headlineContentColor = MaterialTheme.colorScheme.onSurface,
        weekdayContentColor = MaterialTheme.colorScheme.onSurface,
        subheadContentColor = MaterialTheme.colorScheme.onSurface,
        navigationContentColor = MaterialTheme.colorScheme.onSurface,
        yearContentColor = MaterialTheme.colorScheme.onSurface,
        disabledYearContentColor = MaterialTheme.colorScheme.onSurface,
        selectedYearContentColor = MaterialTheme.colorScheme.inverseOnSurface,
        currentYearContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledSelectedYearContentColor = MaterialTheme.colorScheme.onSurface,
        selectedYearContainerColor = LightBgBlue,
        disabledSelectedYearContainerColor = Color.Transparent,
        dayContentColor = MaterialTheme.colorScheme.onSurface,
        disabledDayContentColor = MaterialTheme.colorScheme.onSurface,
        selectedDayContentColor = MaterialTheme.colorScheme.inverseOnSurface,
        disabledSelectedDayContainerColor = Color.Transparent,
        selectedDayContainerColor = LightBgBlue,
        todayContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        todayDateBorderColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        dayInSelectionRangeContainerColor = MaterialTheme.colorScheme.surface,
        dayInSelectionRangeContentColor = MaterialTheme.colorScheme.onSurface,
        dividerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        disabledSelectedDayContentColor = MaterialTheme.colorScheme.onSurface,
        dateTextFieldColors = textFieldColors()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun timePickerColors(): TimePickerColors {
    return TimePickerColors(
        clockDialColor = MaterialTheme.colorScheme.surfaceBright,
        clockDialSelectedContentColor = Color.White,
        clockDialUnselectedContentColor = MaterialTheme.colorScheme.onSurface,
        selectorColor = LightBgBlue,
        containerColor = MaterialTheme.colorScheme.surface,
        periodSelectorBorderColor = LightBgGreen,
        periodSelectorSelectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
        periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurface,
        periodSelectorSelectedContainerColor = LightBgBlue,
        periodSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surfaceBright,
        timeSelectorSelectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
        timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurface,
        timeSelectorSelectedContainerColor = LightBgBlue,
        timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surfaceBright
    )
}

@Composable
fun switchColors(): SwitchColors {
    return SwitchColors(
        checkedThumbColor = LightBgBlue,
        uncheckedThumbColor = MaterialTheme.colorScheme.inverseOnSurface,
        checkedTrackColor = LightBgGreen,
        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceBright,
        checkedBorderColor = LightBgBlue,
        uncheckedBorderColor = MaterialTheme.colorScheme.inverseOnSurface,
        checkedIconColor = LightBgGreen,
        uncheckedIconColor = MaterialTheme.colorScheme.onSurface,
        disabledCheckedIconColor = Color.LightGray,
        disabledCheckedThumbColor = Color.DarkGray,
        disabledCheckedTrackColor = Color.LightGray,
        disabledCheckedBorderColor = Color.DarkGray,
        disabledUncheckedIconColor = Color.LightGray,
        disabledUncheckedThumbColor = Color.DarkGray,
        disabledUncheckedTrackColor = Color.LightGray,
        disabledUncheckedBorderColor = Color.DarkGray
    )
}

@Composable
fun buttonColors(): ButtonColors {
    return ButtonColors(
        containerColor = LightBgBlue,
        contentColor = DarkBgGreen,
        disabledContainerColor = Color.Gray,
        disabledContentColor = Color.White
    )
}