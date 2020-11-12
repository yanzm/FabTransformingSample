package net.yanzm.fabtransformingsample.transition

import android.transition.ChangeBounds
import android.transition.ChangeTransform
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionSet
import android.transition.Visibility
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import net.yanzm.fabtransformingsample.R

class FabTransformation(transformed: Boolean, startRadius: Float) : TransitionSet() {
    init {
        val fabTransition: Transition = TransitionSet()
            .setDuration(100)
            .setOrdering(ORDERING_TOGETHER)
            .addTransition(ChangeBounds())
            .apply {
                val visibility = if (transformed) Visibility.MODE_OUT else Visibility.MODE_IN
                addTransition(ImageFade(visibility))

                interpolator = if (transformed) {
                    AccelerateInterpolator()
                } else {
                    DecelerateInterpolator()
                }
                pathMotion = FabTransitionPathMotion()
            }

        val toolsTransition = TransitionSet()
            .setDuration(200)
            .setOrdering(ORDERING_TOGETHER)
            .addTransition(ChangeTransform().addTarget(R.id.tools))
            .apply {
                val visibility = if (transformed) Visibility.MODE_IN else Visibility.MODE_OUT
                addTransition(CircularRevealTransition(visibility, startRadius))

                val fade = if (transformed) Fade.IN else Fade.OUT
                addTransition(Fade(fade).addTarget(R.id.tools))
            }

        ordering = ORDERING_SEQUENTIAL

        if (transformed) {
            addTransition(fabTransition)
            addTransition(toolsTransition)
        } else {
            addTransition(toolsTransition)
            addTransition(fabTransition)
        }
    }
}
