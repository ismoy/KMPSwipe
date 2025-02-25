package io.github.ismoy.kmpswipe.controllers

import androidx.compose.ui.unit.Dp
import io.github.ismoy.kmpswipe.models.SwipeDirection
import io.github.ismoy.kmpswipe.models.SwipeState
import kotlin.math.abs

class SwipeController(
    private val onSwipeComplete: (SwipeDirection) -> Unit,
    private val onSwipe: (SwipeDirection, Dp) -> Unit,
    private val onSwipeStateChange: (SwipeState) -> Unit,
    private val onSwipeVelocity: (Float) -> Unit,
    private val swipeDirections: Set<SwipeDirection>
) {
     private var currentDirection = SwipeDirection.None

    private var completedDirection = SwipeDirection.None

    var currentSwipeState = SwipeState.Start
        private set

    private var persistentSwipeState = SwipeState.Cancelled

    private var swipingStateEmitted = false

    fun onDragStart() {
        currentSwipeState = SwipeState.Start
        onSwipeStateChange(SwipeState.Start)
        swipingStateEmitted = false
    }

    fun onDragEnd(offsetX: Float, threshold: Float) {
        if (abs(offsetX) > threshold) {
            completedDirection = if (offsetX > 0) SwipeDirection.Right else SwipeDirection.Left
            onSwipeComplete(completedDirection)
            currentSwipeState = SwipeState.End
            persistentSwipeState = SwipeState.End
            onSwipeStateChange(SwipeState.End)
        } else {
            currentSwipeState = persistentSwipeState
            onSwipeStateChange(persistentSwipeState)
        }
    }

    fun onHorizontalDrag(dragAmount: Float, currentOffset: Float, threshold: Float, density: androidx.compose.ui.unit.Density, velocityX: Float): Float? {
        val direction = if (currentOffset + dragAmount > 0) SwipeDirection.Right else SwipeDirection.Left

        if (direction !in swipeDirections) {
            return null
        }

        currentDirection = direction

        val newOffset = (currentOffset + dragAmount).coerceIn(
            -threshold,
            threshold
        )

        with(density) {
            onSwipe(direction, newOffset.toDp())
        }

        if (!swipingStateEmitted) {
            currentSwipeState = SwipeState.Swiping
            onSwipeStateChange(SwipeState.Swiping)
            swipingStateEmitted = true
        }

        onSwipeVelocity(velocityX)

        return newOffset
    }

    fun resetDirection() {
        currentDirection = SwipeDirection.None
    }

    fun getEffectiveDirection(): SwipeDirection {
        return if (currentSwipeState == SwipeState.Swiping) currentDirection else completedDirection
    }
}