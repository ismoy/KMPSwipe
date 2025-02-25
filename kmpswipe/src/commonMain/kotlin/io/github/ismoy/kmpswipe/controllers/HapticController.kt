package io.github.ismoy.kmpswipe.controllers

import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

class HapticController(
    private val hapticFeedback: HapticFeedback,
    private val enabled: Boolean
) {
    fun performHapticFeedback() {
        if (enabled) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }
}