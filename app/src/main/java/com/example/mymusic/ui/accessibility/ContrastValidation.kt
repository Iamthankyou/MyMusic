package com.example.mymusic.ui.accessibility

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import kotlin.math.max
import kotlin.math.min

/**
 * Utility functions for validating color contrast ratios according to WCAG guidelines
 */
object ContrastValidation {
    
    /**
     * WCAG AA minimum contrast ratios
     */
    const val MIN_CONTRAST_NORMAL_TEXT = 4.5
    const val MIN_CONTRAST_LARGE_TEXT = 3.0
    
    /**
     * Calculates the contrast ratio between two colors
     * Formula: (L1 + 0.05) / (L2 + 0.05) where L1 is the lighter luminance and L2 is the darker
     */
    fun calculateContrastRatio(color1: Color, color2: Color): Double {
        val luminance1 = color1.luminance()
        val luminance2 = color2.luminance()
        
        val lighter = max(luminance1, luminance2)
        val darker = min(luminance1, luminance2)
        
        return (lighter + 0.05) / (darker + 0.05)
    }
    
    /**
     * Checks if two colors meet WCAG AA contrast requirements for normal text
     */
    fun meetsNormalTextContrast(foreground: Color, background: Color): Boolean {
        return calculateContrastRatio(foreground, background) >= MIN_CONTRAST_NORMAL_TEXT
    }
    
    /**
     * Checks if two colors meet WCAG AA contrast requirements for large text
     */
    fun meetsLargeTextContrast(foreground: Color, background: Color): Boolean {
        return calculateContrastRatio(foreground, background) >= MIN_CONTRAST_LARGE_TEXT
    }
    
    /**
     * Gets a description of the contrast level
     */
    fun getContrastLevel(ratio: Double): String {
        return when {
            ratio >= 7.0 -> "AAA (Enhanced)"
            ratio >= 4.5 -> "AA (Standard)"
            ratio >= 3.0 -> "AA Large Text Only"
            else -> "Insufficient"
        }
    }
    
    /**
     * Validates theme colors and returns a report
     */
    fun validateThemeContrast(
        primary: Color,
        onPrimary: Color,
        background: Color,
        onBackground: Color,
        surface: Color,
        onSurface: Color
    ): ContrastReport {
        return ContrastReport(
            primaryContrast = calculateContrastRatio(onPrimary, primary),
            backgroundContrast = calculateContrastRatio(onBackground, background),
            surfaceContrast = calculateContrastRatio(onSurface, surface)
        )
    }
}

data class ContrastReport(
    val primaryContrast: Double,
    val backgroundContrast: Double,
    val surfaceContrast: Double
) {
    val isCompliant: Boolean
        get() = primaryContrast >= ContrastValidation.MIN_CONTRAST_NORMAL_TEXT &&
                backgroundContrast >= ContrastValidation.MIN_CONTRAST_NORMAL_TEXT &&
                surfaceContrast >= ContrastValidation.MIN_CONTRAST_NORMAL_TEXT
    
    override fun toString(): String {
        return """
            Contrast Report:
            - Primary/OnPrimary: ${String.format("%.2f", primaryContrast)} (${ContrastValidation.getContrastLevel(primaryContrast)})
            - Background/OnBackground: ${String.format("%.2f", backgroundContrast)} (${ContrastValidation.getContrastLevel(backgroundContrast)})
            - Surface/OnSurface: ${String.format("%.2f", surfaceContrast)} (${ContrastValidation.getContrastLevel(surfaceContrast)})
            - Overall Compliance: ${if (isCompliant) "✓ PASS" else "✗ FAIL"}
        """.trimIndent()
    }
}
