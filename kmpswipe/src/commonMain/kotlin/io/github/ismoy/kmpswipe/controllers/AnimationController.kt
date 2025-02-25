package io.github.ismoy.kmpswipe.controllers

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.spring

class AnimationController(
    private val springStiffness: Float,
    private val dampingRatio: Float
) {
    fun createAnimatable(): Animatable<Float, AnimationVector1D> {
        return Animatable(0f)
    }

    suspend fun animateToZero(animatable: Animatable<Float, AnimationVector1D>): Any {
        return animatable.animateTo(
            targetValue = 0f,
            animationSpec = spring(
                dampingRatio = dampingRatio,
                stiffness = springStiffness
            )
        )
    }

    suspend fun snapTo(animatable: Animatable<Float, AnimationVector1D>, value: Float) {
        animatable.snapTo(value)
    }
}