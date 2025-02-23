import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs


@Composable
fun TailorSwipe(
    modifier: Modifier = Modifier,
    onSwipeComplete: (SwipeDirection) -> Unit,
    onSwipe: (direction: SwipeDirection, offset: Dp) -> Unit = { _, _ -> },
    swipeThreshold: Dp = 100.dp,
    resistance: Float = 1f,
    springStiffness: Float = 500f,
    swipeLimitMultiplier: Float = 1.5f,
    paddingLeftValue: Dp = 6.dp,
    paddingRightValue: Dp = 6.dp,
    vibrationEnabled: Boolean = true,
    dampingRatio:Float = Spring.DampingRatioMediumBouncy,
    leftBackground: @Composable (offset: Dp) -> Unit = {},
    rightBackground: @Composable (offset: Dp) -> Unit = {},
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    val haptic = LocalHapticFeedback.current
    val swipeThresholdPx = with(density) { swipeThreshold.toPx() }
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    var direction by remember { mutableStateOf(SwipeDirection.None) }

    Box(
        modifier = modifier
    ) {
        if (offsetX.value > 0) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = paddingRightValue, vertical = paddingRightValue)
                    .matchParentSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                rightBackground(with(density) { offsetX.value.toDp() })
            }
        }

        if (offsetX.value < 0) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = paddingLeftValue, vertical = paddingLeftValue)
                    .matchParentSize(),
                contentAlignment = Alignment.CenterEnd
            ) {
                leftBackground(with(density) { (-offsetX.value).toDp() })
            }
        }

        Box(
            modifier = modifier
                .offset { IntOffset(offsetX.value.toInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                if (abs(offsetX.value) > swipeThresholdPx) {
                                    val swipeDir =
                                        if (offsetX.value > 0) SwipeDirection.Right else SwipeDirection.Left
                                    if (vibrationEnabled){
                                        haptic.performHapticFeedback(
                                            if (swipeDir == SwipeDirection.Right)
                                            HapticFeedbackType.LongPress else HapticFeedbackType.LongPress
                                        )
                                    }
                                    onSwipeComplete(swipeDir)
                                }
                                offsetX.animateTo(
                                    targetValue = 0f,
                                    animationSpec = spring(
                                        dampingRatio = dampingRatio,
                                        stiffness = springStiffness
                                    )
                                )
                                direction = SwipeDirection.None
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                val newOffset = (offsetX.value + dragAmount / resistance).coerceIn(
                                    -swipeThresholdPx * swipeLimitMultiplier,
                                    swipeThresholdPx * swipeLimitMultiplier
                                )
                                offsetX.snapTo(newOffset)
                                direction =
                                    if (newOffset > 0) SwipeDirection.Right else SwipeDirection.Left
                                onSwipe(direction, with(density) { newOffset.toDp() })
                            }
                        }
                    )
                }
        ) {
            content()
        }
    }
}