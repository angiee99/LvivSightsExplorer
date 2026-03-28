package com.angelina.lvivexplorer.core.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

private val fixedCategoryColors = mapOf(
    "cathedral" to Color(0xFF6A1B9A),
    "theater" to Color(0xFF1565C0),
    "museum" to Color(0xFF2E7D32),
    "monument" to Color(0xFFD84315),
    "monastery" to Color(0xFF00838F)
)

fun categoryColor(category: String): Color {
    val normalized = category.trim().lowercase()
    return fixedCategoryColors[normalized] ?: fallbackColor(normalized)
}

fun categoryArgb(category: String): Int = categoryColor(category).toArgb()

private fun fallbackColor(category: String): Color {
    val palette = listOf(
        Color(0xFFAD1457),
        Color(0xFF283593),
        Color(0xFF00695C),
        Color(0xFFEF6C00),
        Color(0xFF4527A0),
        Color(0xFF0277BD)
    )
    val index = (category.hashCode().ushr(1) % palette.size)
    return palette[index]
}
