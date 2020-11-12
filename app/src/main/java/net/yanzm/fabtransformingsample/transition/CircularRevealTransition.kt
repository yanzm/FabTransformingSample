package net.yanzm.fabtransformingsample.transition

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.transition.TransitionValues
import android.transition.Visibility
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import java.util.ArrayList
import kotlin.math.hypot
import kotlin.math.sqrt

class CircularRevealTransition : Visibility {

    private val startRadius: Float

    constructor(startRadius: Float) {
        this.startRadius = startRadius
    }

    constructor(mode: Int, startRadius: Float) {
        this.startRadius = startRadius
        setMode(mode)
    }

    override fun onAppear(
        sceneRoot: ViewGroup,
        view: View,
        startValues: TransitionValues,
        endValues: TransitionValues
    ): Animator? {
        val w = view.width.toFloat()
        val h = view.height.toFloat()
        val endRadius = hypot(w, h)
        val centerX = view.pivotX.toInt()
        val centerY = view.pivotY.toInt()
        view.alpha = 0f
        val anim = ViewAnimationUtils.createCircularReveal(
            view, centerX, centerY, startRadius, endRadius
        ).apply {
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    super.onAnimationStart(animation)
                    view.alpha = 1f
                }
            })
        }
        return NoPauseAnimator(anim)
    }

    override fun onDisappear(
        sceneRoot: ViewGroup,
        view: View,
        startValues: TransitionValues,
        endValues: TransitionValues
    ): Animator? {
        val w = view.width.toFloat()
        val h = view.height.toFloat()
        val endRadius = sqrt(w * w + h * h)
        val centerX = view.pivotX.toInt()
        val centerY = view.pivotY.toInt()
        val anim = ViewAnimationUtils.createCircularReveal(
            view, centerX, centerY, endRadius, startRadius
        )
        return NoPauseAnimator(anim)
    }

    /**
     *  We don't want to override pause or resume methods
     * because we don't want them to affect originalAnimator.
     * public void pause();
     * public void resume();
     * public void addPauseListener(AnimatorPauseListener listener);
     * public void removePauseListener(AnimatorPauseListener listener);
     */
    private class NoPauseAnimator(private val originalAnimator: Animator) : Animator() {

        private val listeners = mutableMapOf<AnimatorListener, AnimatorListenerWrapper>()

        override fun addListener(listener: AnimatorListener) {
            val wrapper = AnimatorListenerWrapper(this, listener)
            if (!listeners.containsKey(listener)) {
                listeners[listener] = wrapper
                originalAnimator.addListener(wrapper)
            }
        }

        override fun cancel() {
            originalAnimator.cancel()
        }

        override fun end() {
            originalAnimator.end()
        }

        override fun getDuration(): Long {
            return originalAnimator.duration
        }

        override fun getInterpolator(): TimeInterpolator {
            return originalAnimator.interpolator
        }

        override fun getListeners(): ArrayList<AnimatorListener> {
            return ArrayList(listeners.keys)
        }

        override fun getStartDelay(): Long {
            return originalAnimator.startDelay
        }

        override fun isPaused(): Boolean {
            return originalAnimator.isPaused
        }

        override fun isRunning(): Boolean {
            return originalAnimator.isRunning
        }

        override fun isStarted(): Boolean {
            return originalAnimator.isStarted
        }

        override fun removeAllListeners() {
            super.removeAllListeners()
            listeners.clear()
            originalAnimator.removeAllListeners()
        }

        override fun removeListener(listener: AnimatorListener) {
            val wrapper: AnimatorListener? = listeners[listener]
            if (wrapper != null) {
                listeners.remove(listener)
                originalAnimator.removeListener(wrapper)
            }
        }

        override fun setDuration(durationMS: Long): Animator {
            originalAnimator.duration = durationMS
            return this
        }

        override fun setInterpolator(timeInterpolator: TimeInterpolator) {
            originalAnimator.interpolator = timeInterpolator
        }

        override fun setStartDelay(delayMS: Long) {
            originalAnimator.startDelay = delayMS
        }

        override fun setTarget(target: Any?) {
            originalAnimator.setTarget(target)
        }

        override fun setupEndValues() {
            originalAnimator.setupEndValues()
        }

        override fun setupStartValues() {
            originalAnimator.setupStartValues()
        }

        override fun start() {
            originalAnimator.start()
        }

        private class AnimatorListenerWrapper(
            private val animator: Animator,
            private val originalListener: AnimatorListener
        ) : AnimatorListener {
            override fun onAnimationStart(animator: Animator) {
                originalListener.onAnimationStart(this.animator)
            }

            override fun onAnimationEnd(animator: Animator) {
                originalListener.onAnimationEnd(this.animator)
            }

            override fun onAnimationCancel(animator: Animator) {
                originalListener.onAnimationCancel(this.animator)
            }

            override fun onAnimationRepeat(animator: Animator) {
                originalListener.onAnimationRepeat(this.animator)
            }
        }
    }
}
