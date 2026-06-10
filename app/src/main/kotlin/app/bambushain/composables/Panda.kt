package app.bambushain.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import app.bambushain.R
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

@Composable
fun Panda(modifier: Modifier = Modifier, date: LocalDate, tabletOnly: Boolean = true) {
    val windowInfo = currentWindowAdaptiveInfo()
    val isTablet = remember {
        windowInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)
    }

    if (isTablet) {
        when (date.month) {
            in setOf(
                Month.DECEMBER,
                Month.JANUARY,
                Month.FEBRUARY
            )
                -> {
                Image(
                    ImageBitmap.imageResource(R.drawable.winter),
                    contentDescription = "Winter",
                    modifier = modifier
                        .widthIn(max = 512.dp)
                )
            }

            in setOf(
                Month.MARCH,
                Month.APRIL,
                Month.MAY
            )
                -> {
                Image(
                    ImageBitmap.imageResource(R.drawable.spring),
                    contentDescription = "Frühling",
                    modifier = modifier
                        .widthIn(max = 512.dp)
                )
            }

            in setOf(
                Month.JUNE,
                Month.JULY,
                Month.AUGUST
            )
                -> {
                Image(
                    ImageBitmap.imageResource(R.drawable.summer),
                    contentDescription = "Sommer",
                    modifier = modifier
                        .widthIn(max = 512.dp)
                )
            }

            in setOf(
                Month.SEPTEMBER,
                Month.OCTOBER,
                Month.NOVEMBER
            )
                -> {
                Image(
                    ImageBitmap.imageResource(R.drawable.autumn),
                    contentDescription = "Herbst",
                    modifier = modifier
                        .widthIn(max = 512.dp)
                )
            }

            else -> {}
        }
    }
}