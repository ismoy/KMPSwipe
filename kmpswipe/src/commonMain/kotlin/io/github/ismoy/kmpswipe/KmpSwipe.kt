package io.github.ismoy.kmpswipe
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.github.ismoy.kmpswipe.controllers.AnimationController
import io.github.ismoy.kmpswipe.controllers.HapticController
import io.github.ismoy.kmpswipe.controllers.SwipeController
import io.github.ismoy.kmpswipe.models.SwipeDirection
import io.github.ismoy.kmpswipe.models.SwipeState
import io.github.ismoy.kmpswipe.ui.SwipeLayout
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun KmpSwipe(
    modifier: Modifier = Modifier,
    onSwipeComplete: (SwipeDirection) -> Unit,
    onSwipe: (SwipeDirection, Dp) -> Unit = { _, _ -> },
    onSwipeStateChange: (SwipeState) -> Unit = {},
    swipeThreshold: Dp = 100.dp,
    resistance: Float = 1f,
    springStiffness: Float = 500f,
    swipeLimitMultiplier: Float = 1.5f,
    backgroundPaddingHorizontal: Dp = 6.dp,
    vibrationEnabled: Boolean = true,
    dampingRatio: Float = Spring.DampingRatioMediumBouncy,
    leftBackground: @Composable (offset: Dp) -> Unit = {},
    rightBackground: @Composable (offset: Dp) -> Unit = {},
    enabled: Boolean = true,
    swipeDirections: Set<SwipeDirection> = setOf(SwipeDirection.Left, SwipeDirection.Right),
    onSwipeVelocity: (Float) -> Unit = {},
    dynamicSwipeThreshold: ((Dp) -> Dp)? = null,
    content: @Composable (swipeState: SwipeState, swipeDirection: SwipeDirection) -> Unit,
) {
    val density = LocalDensity.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    val swipeController = remember {
        SwipeController(
            onSwipeComplete = onSwipeComplete,
            onSwipe = onSwipe,
            onSwipeStateChange = onSwipeStateChange,
            onSwipeVelocity = onSwipeVelocity,
            swipeDirections = swipeDirections
        )
    }

    val animationController = remember {
        AnimationController(
            springStiffness = springStiffness,
            dampingRatio = dampingRatio
        )
    }

    val hapticController = remember {
        HapticController(
            hapticFeedback = haptic,
            enabled = vibrationEnabled
        )
    }

    val offsetX = remember { animationController.createAnimatable() }

    val calculatedSwipeThresholdPx = remember(swipeThreshold, dynamicSwipeThreshold) {
        if (dynamicSwipeThreshold != null) {
            with(density) { dynamicSwipeThreshold(swipeThreshold).toPx() }
        } else {
            with(density) { swipeThreshold.toPx() }
        }
    }

    SwipeLayout(
        modifier = modifier,
        offsetX = offsetX.value,
        backgroundPaddingHorizontal = backgroundPaddingHorizontal,
        swipeDirections = swipeDirections,
        density = density,
        leftBackground = leftBackground,
        rightBackground = rightBackground,
        contentModifier = Modifier
            .offset { IntOffset(offsetX.value.toInt(), 0) }
            .pointerInput(enabled) {
                if (enabled) {
                    detectHorizontalDragGestures(
                        onDragStart = {
                            swipeController.onDragStart()
                        },
                        onDragEnd = {
                            scope.launch {
                                swipeController.onDragEnd(offsetX.value, calculatedSwipeThresholdPx)

                                if (abs(offsetX.value) > calculatedSwipeThresholdPx) {
                                    hapticController.performHapticFeedback()
                                }

                                animationController.animateToZero(offsetX).also {
                                    swipeController.resetDirection()
                                }
                            }
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            scope.launch {
                                val adjustedDragAmount = dragAmount / resistance
                                val newOffset = swipeController.onHorizontalDrag(
                                    adjustedDragAmount,
                                    offsetX.value,
                                    calculatedSwipeThresholdPx * swipeLimitMultiplier,
                                    density,
                                    change.positionChange().x
                                )

                                if (newOffset != null) {
                                    animationController.snapTo(offsetX, newOffset)
                                }
                            }
                        }
                    )
                }
            }
    ) {
        content(swipeController.currentSwipeState, swipeController.getEffectiveDirection())
    }
}