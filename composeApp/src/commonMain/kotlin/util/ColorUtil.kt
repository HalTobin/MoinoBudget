package util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

fun Color.toHue(): Float = argbToHue(this.toArgb())

fun argbToHue(color: Int): Float {
    // Extract RGB components from ARGB Int (ignoring alpha for hue calculation)
    val r = ((color shr 16) and 0xFF) / 255.0f
    val g = ((color shr 8) and 0xFF) / 255.0f
    val b = (color and 0xFF) / 255.0f

    // Find min and max values of R, G, B
    val max = maxOf(r, g, b)
    val min = minOf(r, g, b)
    val delta = max - min

    // Initialize hue
    var hue = 0f

    if (delta == 0f) {
        hue = 0f // Gray (no color)
    } else {
        // Calculate hue based on which channel is max
        when (max) {
            r -> hue = ((g - b) / delta) % 6
            g -> hue = ((b - r) / delta) + 2
            b -> hue = ((r - g) / delta) + 4
        }
        // Convert to degrees on the color wheel
        hue *= 60f

        // Make sure hue is positive
        if (hue < 0) {
            hue += 360f
        }
    }

    return hue
}