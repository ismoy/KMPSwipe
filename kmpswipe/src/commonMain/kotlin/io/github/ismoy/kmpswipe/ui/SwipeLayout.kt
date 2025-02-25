package io.github.ismoy.kmpswipe.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import io.github.ismoy.kmpswipe.models.SwipeDirection

@Composable
fun SwipeLayout(
    modifier: Modifier = Modifier,
    offsetX: Float,
    backgroundPaddingHorizontal: Dp,
    swipeDirections: Set<SwipeDirection>,
    contentModifier: Modifier,
    density: androidx.compose.ui.unit.Density,
    leftBackground: @Composable (offset: Dp) -> Unit,
    rightBackground: @Composable (offset: Dp) -> Unit,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        if (offsetX > 0 && SwipeDirection.Right in swipeDirections) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = backgroundPaddingHorizontal)
                    .matchParentSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                rightBackground(with(density) { offsetX.toDp() })
            }
        }

        if (offsetX < 0 && SwipeDirection.Left in swipeDirections) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = backgroundPaddingHorizontal)
                    .matchParentSize(),
                contentAlignment = Alignment.CenterEnd
            ) {
                leftBackground(with(density) { (-offsetX).toDp() })
            }
        }

        Box(modifier = contentModifier) {
            content()
        }
    }
}