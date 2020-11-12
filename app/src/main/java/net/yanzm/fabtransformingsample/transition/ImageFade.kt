package net.yanzm.fabtransformingsample.transition

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.transition.TransitionValues
import android.transition.Visibility
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class ImageFade(mode: Int) : Visibility() {

    init {
        setMode(mode)
    }

    override fun onAppear(
        sceneRoot: ViewGroup,
        view: View,
        startValues: TransitionValues,
        endValues: TransitionValues
    ): Animator? {
        val imageView = view as? ImageView ?: return null
        val drawable = imageView.drawable ?: return null
        imageView.alpha = 0f
        drawable.alpha = 0
        return ValueAnimator.ofInt(0, 255).apply {
            addUpdateListener {
                drawable.alpha = it.animatedValue as Int
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    super.onAnimationStart(animation)
                    view.setAlpha(1f)
                }
            })
        }
    }

    override fun onDisappear(
        sceneRoot: ViewGroup,
        view: View,
        startValues: TransitionValues,
        endValues: TransitionValues
    ): Animator? {
        val imageView = view as? ImageView ?: return null
        val drawable = imageView.drawable ?: return null
        return ValueAnimator.ofInt(255, 0).apply {
            addUpdateListener {
                drawable.alpha = it.animatedValue as Int
            }
        }
    }
}
