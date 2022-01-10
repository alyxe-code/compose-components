package com.alyxe.tbiunicorn.experemental.calculator

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alyxe.tbiunicorn.experemental.ui.theme.TbiTheme

@Composable
private fun SwitchInternal(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    track: DrawScope.() -> Unit,
    thumb: DrawScope.() -> Unit,
) {
    Canvas(
        modifier = modifier.clickable { onCheckedChange(!checked) }
    ) {
        track()
        thumb()
    }
}

@Composable
private fun Switch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    colors: SwitchColors,
) {
    val trackBrush = rememberTrackBrush(checked = checked, colors = colors)
    val borderBrush = rememberBorderBrush(checked = checked, colors = colors)
    val thumbBrush = rememberThumbBrush(checked = checked, colors = colors)

    SwitchInternal(
        modifier = modifier.size(
            width = 54.dp,
            height = 32.dp
        ),
        checked = checked,
        onCheckedChange = onCheckedChange,
        track = {
            drawTrack(
                trackBrush = trackBrush,
                borderBrush = borderBrush,
                cornerRadius = CornerRadius(100f, 100f),
            )
        },
        thumb = {
            drawThumb(
                thumbBrush = thumbBrush,
                checked = checked,
            )
        },
    )
}

@Composable
private fun Switch_Sample() {
    Column {
        Switch(
            checked = true,
            onCheckedChange = {},
            colors = defaultSwitchColors(),
        )

        Switch(
            checked = false,
            onCheckedChange = {},
            colors = defaultSwitchColors(),
        )
    }
}

@Preview
@Composable
private fun Switch_Light_Preview() {
    TbiTheme(useDarkTheme = false) {
        Switch_Sample()
    }
}

private fun DrawScope.drawTrack(
    trackBrush: Brush,
    borderBrush: Brush,
    cornerRadius: CornerRadius,
) {
    drawRoundRect(
        brush = borderBrush,
        cornerRadius = cornerRadius,
        style = Stroke(width = BORDER_SIZE, cap = StrokeCap.Round),
    )
    drawRoundRect(
        brush = trackBrush,
        topLeft = Offset(x = BORDER_SIZE, y = BORDER_SIZE),
        size = computeTrackSize(),
        cornerRadius = cornerRadius,
    )
}

private const val BORDER_SIZE = 1f

private fun DrawScope.computeTrackSize(): Size {
    return Size(
        width = this.size.width - BORDER_SIZE * 2f,
        height = this.size.height - BORDER_SIZE * 2f,
    )
}

private fun DrawScope.drawThumb(thumbBrush: Brush, checked: Boolean) {
    val radius = computeThumbRadius()
    drawCircle(
        brush = thumbBrush,
        radius = computeThumbRadius(),
        center = computeThumbCenter(
            radius = radius,
            checked = checked
        ),
    )
}

/**
 * According to math on height
 * Radius - TopBorder - BottomBorder
 */
private fun DrawScope.computeThumbRadius(): Float {
    return this.size.height / 2f - BORDER_SIZE * 4f
}

private fun DrawScope.computeThumbCenter(radius: Float, checked: Boolean): Offset {
    val y = this.size.height / 2f
    return if (checked) {
        Offset(
            x = this.size.width - radius - BORDER_SIZE * 2f,
            y = y,
        )
    } else {
        Offset(
            x = radius + BORDER_SIZE * 2f,
            y = y,
        )
    }
}

@Stable
data class SwitchColors(
    override val thumbActive: Color,
    override val thumbInactive: Color,
    override val trackActive: Color,
    override val trackInactive: Color,
    override val borderActive: Color,
    override val borderInactive: Color,
) : SwitchThumbColors, SwitchTrackColors

@Stable
interface SwitchThumbColors {
    val thumbActive: Color
    val thumbInactive: Color
}

@Stable
interface SwitchTrackColors {
    val trackActive: Color
    val trackInactive: Color
    val borderActive: Color
    val borderInactive: Color
}

@Composable
private fun defaultSwitchColors(): SwitchColors {
    return SwitchColors(
        thumbActive = TbiTheme.colors.contentAccentSecondary,
        thumbInactive = TbiTheme.colors.contentAccentSecondary,
        trackActive = TbiTheme.colors.buttonPrimaryBg,
        trackInactive = TbiTheme.colors.backgroundSecondary,
        borderActive = TbiTheme.colors.buttonPrimaryBg,
        borderInactive = TbiTheme.colors.contentQuaternary,
    )
}

@Composable
private fun rememberTrackBrush(checked: Boolean, colors: SwitchTrackColors): Brush {
    return remember(checked, colors) {
        SolidColor(
            value = if (checked) {
                colors.trackActive
            } else {
                colors.trackInactive
            }
        )
    }
}

@Composable
private fun rememberBorderBrush(checked: Boolean, colors: SwitchTrackColors): Brush {
    return remember(checked, colors) {
        SolidColor(
            value = if (checked) {
                colors.borderActive
            } else {
                colors.borderInactive
            }
        )
    }
}

@Composable
private fun rememberThumbBrush(checked: Boolean, colors: SwitchThumbColors): Brush {
    return remember(checked, colors) {
        SolidColor(
            value = if (checked) {
                colors.thumbActive
            } else {
                colors.thumbInactive
            }
        )
    }
}

