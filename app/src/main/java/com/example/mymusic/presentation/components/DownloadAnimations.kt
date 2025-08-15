package com.example.mymusic.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

/**
 * Animation constants and utilities for download states
 */
object DownloadAnimations {
    
    // Duration constants
    const val FAST_DURATION = 150
    const val NORMAL_DURATION = 300
    const val SLOW_DURATION = 500
    const val VERY_SLOW_DURATION = 1000
    
    // Scale constants
    const val PRESSED_SCALE = 0.9f
    const val HOVER_SCALE = 1.05f
    const val SUCCESS_SCALE = 1.2f
    const val PULSE_MIN_SCALE = 0.8f
    const val PULSE_MAX_SCALE = 1.2f
    
    // Rotation constants
    const val FULL_ROTATION = 360f
    const val HALF_ROTATION = 180f
    const val QUARTER_ROTATION = 90f
    
    // Translation constants
    const val SHAKE_AMOUNT = 8f
    const val BOUNCE_AMOUNT = 1.1f
}

/**
 * Common animation specs for download components
 */
object DownloadAnimationSpecs {
    
    val fastEasing: Easing = FastOutSlowInEasing
    val slowEasing: Easing = FastOutSlowInEasing
    val linearEasing: Easing = LinearEasing
    val normalEasing: Easing = FastOutSlowInEasing
    
    val fastTween = tween<Float>(DownloadAnimations.FAST_DURATION, easing = fastEasing)
    val normalTween = tween<Float>(DownloadAnimations.NORMAL_DURATION, easing = normalEasing)
    val slowTween = tween<Float>(DownloadAnimations.SLOW_DURATION, easing = slowEasing)
    val verySlowTween = tween<Float>(DownloadAnimations.VERY_SLOW_DURATION, easing = slowEasing)
}

/**
 * Reusable animation composables for download states
 */
@Composable
fun rememberDownloadPulseAnimation(
    minScale: Float = DownloadAnimations.PULSE_MIN_SCALE,
    maxScale: Float = DownloadAnimations.PULSE_MAX_SCALE,
    duration: Int = DownloadAnimations.SLOW_DURATION
): Float {
    val infiniteTransition = rememberInfiniteTransition(label = "download_pulse")
    return infiniteTransition.animateFloat(
        initialValue = minScale,
        targetValue = maxScale,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    ).value
}

@Composable
fun rememberDownloadRotationAnimation(
    targetRotation: Float = DownloadAnimations.FULL_ROTATION,
    duration: Int = DownloadAnimations.VERY_SLOW_DURATION
): Float {
    val infiniteTransition = rememberInfiniteTransition(label = "download_rotation")
    return infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = targetRotation,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    ).value
}

@Composable
fun rememberDownloadShimmerAnimation(
    duration: Int = DownloadAnimations.VERY_SLOW_DURATION
): Float {
    val infiniteTransition = rememberInfiniteTransition(label = "download_shimmer")
    return infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_offset"
    ).value
}

@Composable
fun rememberDownloadBounceAnimation(
    isActive: Boolean,
    bounceAmount: Float = DownloadAnimations.BOUNCE_AMOUNT,
    duration: Int = DownloadAnimations.SLOW_DURATION
): Float {
    return animateFloatAsState(
        targetValue = if (isActive) bounceAmount else 1f,
        animationSpec = tween(duration, easing = FastOutSlowInEasing),
        label = "bounce_scale"
    ).value
}

@Composable
fun rememberDownloadShakeAnimation(
    isShaking: Boolean,
    shakeAmount: Float = DownloadAnimations.SHAKE_AMOUNT,
    duration: Int = DownloadAnimations.NORMAL_DURATION
): Float {
    return animateFloatAsState(
        targetValue = if (isShaking) 1f else 0f,
        animationSpec = tween(duration, easing = FastOutSlowInEasing),
        label = "shake_offset"
    ).value
}

/**
 * Animation states for download components
 */
sealed class DownloadAnimationState {
    object Idle : DownloadAnimationState()
    object Loading : DownloadAnimationState()
    object Success : DownloadAnimationState()
    object Error : DownloadAnimationState()
    object Pending : DownloadAnimationState()
}

/**
 * Animation configuration for different download states
 */
data class DownloadAnimationConfig(
    val scale: Float = 1f,
    val alpha: Float = 1f,
    val rotation: Float = 0f,
    val translationX: Float = 0f,
    val translationY: Float = 0f,
    val duration: Int = DownloadAnimations.NORMAL_DURATION,
    val easing: Easing = FastOutSlowInEasing
)

/**
 * Get animation config for specific download state
 */
fun getDownloadAnimationConfig(state: DownloadAnimationState): DownloadAnimationConfig {
    return when (state) {
        is DownloadAnimationState.Idle -> DownloadAnimationConfig()
        is DownloadAnimationState.Loading -> DownloadAnimationConfig(
            scale = DownloadAnimations.PULSE_MAX_SCALE,
            duration = DownloadAnimations.SLOW_DURATION
        )
        is DownloadAnimationState.Success -> DownloadAnimationConfig(
            scale = DownloadAnimations.SUCCESS_SCALE,
            duration = DownloadAnimations.FAST_DURATION
        )
        is DownloadAnimationState.Error -> DownloadAnimationConfig(
            translationX = DownloadAnimations.SHAKE_AMOUNT,
            duration = DownloadAnimations.NORMAL_DURATION
        )
        is DownloadAnimationState.Pending -> DownloadAnimationConfig(
            alpha = 0.6f,
            duration = DownloadAnimations.SLOW_DURATION
        )
    }
}
