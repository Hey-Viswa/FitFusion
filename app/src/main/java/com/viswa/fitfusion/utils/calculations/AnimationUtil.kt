package com.viswa.fitfusion.utils.calculations

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

class AnimationUtil {


    // Slide-in animation with optional fade-in effect
    fun slideInFromContainer(
        direction: AnimatedContentTransitionScope.SlideDirection,
        animationDuration: Int = 700
    ): EnterTransition {
        val slideOffset = when (direction) {
            AnimatedContentTransitionScope.SlideDirection.Left -> { fullWidth: Int -> fullWidth }
            AnimatedContentTransitionScope.SlideDirection.Right -> { fullWidth: Int -> -fullWidth }
            else -> { fullWidth: Int -> 0 }
        }

        return slideInHorizontally(
            initialOffsetX = slideOffset,
            animationSpec = tween(animationDuration)
        ) + fadeIn(animationSpec = tween(animationDuration))
    }

    // Slide-out animation with optional fade-out effect
    fun slideOutToContainer(
        direction: AnimatedContentTransitionScope.SlideDirection,
        animationDuration: Int = 700
    ): ExitTransition {
        val slideOffset = when (direction) {
            AnimatedContentTransitionScope.SlideDirection.Left -> { fullWidth: Int -> -fullWidth }
            AnimatedContentTransitionScope.SlideDirection.Right -> { fullWidth: Int -> fullWidth }
            else -> { fullWidth: Int -> 0 }
        }

        return slideOutHorizontally(
            targetOffsetX = slideOffset,
            animationSpec = tween(animationDuration)
        ) + fadeOut(animationSpec = tween(animationDuration))
    }

    // Scale In Animation
    fun scaleIntoContainer(
        direction: ScaleTransitionDirection = ScaleTransitionDirection.INWARDS,
        initialScale: Float = if (direction == ScaleTransitionDirection.OUTWARDS) 0.9f else 1.1f
    ): EnterTransition {
        return scaleIn(
            animationSpec = tween(220, delayMillis = 90),
            initialScale = initialScale
        ) + fadeIn(animationSpec = tween(220, delayMillis = 90))
    }

    // Scale Out Animation
    fun scaleOutOfContainer(
        direction: ScaleTransitionDirection = ScaleTransitionDirection.OUTWARDS,
        targetScale: Float = if (direction == ScaleTransitionDirection.INWARDS) 0.9f else 1.1f
    ): ExitTransition {
        return scaleOut(
            animationSpec = tween(
                durationMillis = 220,
                delayMillis = 90
            ), targetScale = targetScale
        ) + fadeOut(tween(delayMillis = 90))
    }
}