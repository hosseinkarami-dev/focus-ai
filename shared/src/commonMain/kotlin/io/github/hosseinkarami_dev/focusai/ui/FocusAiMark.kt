package io.github.hosseinkarami_dev.focusai.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp

/** A compact focus-reticle mark for the FocusAI brand. */
@androidx.compose.runtime.Composable
fun FocusAiMark(size: Dp, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size / 3))
            .background(Brush.linearGradient(listOf(Color(0xFF413AAF), Color(0xFF766FE8))))
            .semantics { contentDescription = "FocusAI focus mark" }
    ) {
        Canvas(Modifier.size(size)) {
            val side = this.size.minDimension
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val white = Color(0xFFF7FBF5)
            val mint = Color(0xFFD5D2FF)
            val peach = Color(0xFFFFB49C)
            val stroke = side * .055f

            drawArc(
                color = mint,
                startAngle = 214f,
                sweepAngle = 292f,
                useCenter = false,
                topLeft = Offset(side * .19f, side * .19f),
                size = Size(side * .62f, side * .62f),
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
            drawCircle(color = white, radius = side * .205f, center = center, style = Stroke(width = stroke))
            drawCircle(color = peach, radius = side * .073f, center = center)

            drawLine(white, Offset(center.x, side * .105f), Offset(center.x, side * .17f), strokeWidth = stroke, cap = StrokeCap.Round)
            drawLine(white, Offset(center.x, side * .83f), Offset(center.x, side * .895f), strokeWidth = stroke, cap = StrokeCap.Round)
            drawLine(white, Offset(side * .105f, center.y), Offset(side * .17f, center.y), strokeWidth = stroke, cap = StrokeCap.Round)
            drawLine(white, Offset(side * .83f, center.y), Offset(side * .895f, center.y), strokeWidth = stroke, cap = StrokeCap.Round)
        }
    }
}
