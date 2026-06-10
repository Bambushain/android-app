package app.bambushain.composables.calendar

import androidx.annotation.ColorRes
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import app.bambushain.R
import kotlinx.datetime.format.MonthNames

@ColorRes
internal fun colorYiqRes(data: String): Int {
    val color = Color(data.toColorInt())
    val yiq =
        ((color.red * 255 * 299) + (color.green * 255 * 587) + (color.blue * 255 * 114)) / 1000
    return if (yiq >= 128) {
        R.color.color_yiq_dark
    } else {
        R.color.color_yiq_light
    }
}

internal val germanMonthNames = MonthNames(
    "Januar",
    "Februar",
    "März",
    "April",
    "Mai",
    "Juni",
    "Juli",
    "August",
    "September",
    "Oktober",
    "November",
    "Dezember"
)