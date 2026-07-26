package com.gentech.chipcheck.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/** Rectangular guide to help a technician frame the BGA laser marking under the camera. */
@Composable
fun GuideOverlay(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val guideWidth = size.width * 0.7f
        val guideHeight = size.height * 0.25f
        val left = (size.width - guideWidth) / 2f
        val top = (size.height - guideHeight) / 2f

        drawRect(
            color = Color.White,
            topLeft = Offset(left, top),
            size = Size(guideWidth, guideHeight),
            style = Stroke(width = 3.dp.toPx())
        )
    }
}
