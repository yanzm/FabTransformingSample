package net.yanzm.fabtransformingsample.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import java.util.ArrayList;

public class CircularRevealTransition extends Visibility {

    private final float startRadius;

    public CircularRevealTransition(float startRadius) {
        this.startRadius = startRadius;
    }

    public CircularRevealTransition(int mode, float startRadius) {
        this.startRadius = startRadius;
        setMode(mode);
    }

    @Override
    public Animator onAppear(ViewGroup sceneRoot, final View view, TransitionValues startValues,
                             TransitionValues endValues) {

        float endRadius = (float) (Math.hypot(view.getWidth(), view.getHeight()));

        int centerX = (int) view.getPivotX();
        int centerY = (int) view.getPivotY();

        view.setAlpha(0f);
        final Animator anim = ViewAnimationUtils.createCircularReveal(
                view, centerX, centerY, startRadius, endRadius);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setAlpha(1f);
            }
        });
        return new NoPauseAnimator(anim);
    }

    @Override
    public Animator onDisappear(ViewGroup sceneRoot, final View view, TransitionValues startValues,
                                TransitionValues endValues) {
        float endRadius = (float) (Math.sqrt(view.getWidth() * view.getWidth() + view.getHeight() * view.getHeight()));

        int centerX = (int) view.getPivotX();
        int centerY = (int) view.getPivotY();

        final Animator anim = ViewAnimationUtils.createCircularReveal(
                view, centerX, centerY, endRadius, startRadius);
        return new NoPauseAnimator(anim);
    }

    private static class NoPauseAnimator extends Animator {

        private final Animator originalAnimator;
        private final ArrayMap<AnimatorListener, AnimatorListenerWrapper> listeners = new ArrayMap<>();

        NoPauseAnimator(Animator animator) {
            originalAnimator = animator;
        }

        @Override
        public void addListener(Animator.AnimatorListener listener) {
            final AnimatorListenerWrapper wrapper = new AnimatorListenerWrapper(this, listener);
            if (!listeners.containsKey(listener)) {
                listeners.put(listener, wrapper);
                originalAnimator.addListener(wrapper);
            }
        }

        @Override
        public void cancel() {
            originalAnimator.cancel();
        }

        @Override
        public void end() {
            originalAnimator.end();
        }

        @Override
        public long getDuration() {
            return originalAnimator.getDuration();
        }

        @Override
        public TimeInterpolator getInterpolator() {
            return originalAnimator.getInterpolator();
        }

        @Override
        public ArrayList<AnimatorListener> getListeners() {
            return new ArrayList<>(listeners.keySet());
        }

        @Override
        public long getStartDelay() {
            return originalAnimator.getStartDelay();
        }

        @Override
        public boolean isPaused() {
            return originalAnimator.isPaused();
        }

        @Override
        public boolean isRunning() {
            return originalAnimator.isRunning();
        }

        @Override
        public boolean isStarted() {
            return originalAnimator.isStarted();
        }

        @Override
        public void removeAllListeners() {
            super.removeAllListeners();
            listeners.clear();
            originalAnimator.removeAllListeners();
        }

        @Override
        public void removeListener(AnimatorListener listener) {
            AnimatorListener wrapper = listeners.get(listener);
            if (wrapper != null) {
                listeners.remove(listener);
                originalAnimator.removeListener(wrapper);
            }
        }

    /* We don't want to override pause or resume methods
     * because we don't want them to affect originalAnimator.
    public void pause();
    public void resume();
    public void addPauseListener(AnimatorPauseListener listener);
    public void removePauseListener(AnimatorPauseListener listener);
     */

        @Override
        public Animator setDuration(long durationMS) {
            originalAnimator.setDuration(durationMS);
            return this;
        }

        @Override
        public void setInterpolator(TimeInterpolator timeInterpolator) {
            originalAnimator.setInterpolator(timeInterpolator);
        }

        @Override
        public void setStartDelay(long delayMS) {
            originalAnimator.setStartDelay(delayMS);
        }

        @Override
        public void setTarget(Object target) {
            originalAnimator.setTarget(target);
        }

        @Override
        public void setupEndValues() {
            originalAnimator.setupEndValues();
        }

        @Override
        public void setupStartValues() {
            originalAnimator.setupStartValues();
        }

        @Override
        public void start() {
            originalAnimator.start();
        }

        private static class AnimatorListenerWrapper implements Animator.AnimatorListener {
            private final Animator animator;
            private final Animator.AnimatorListener originalListener;

            AnimatorListenerWrapper(Animator animator,
                                    Animator.AnimatorListener listener) {
                this.animator = animator;
                originalListener = listener;
            }

            @Override
            public void onAnimationStart(Animator animator) {
                originalListener.onAnimationStart(this.animator);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                originalListener.onAnimationEnd(this.animator);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                originalListener.onAnimationCancel(this.animator);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                originalListener.onAnimationRepeat(this.animator);
            }
        }
    }
}
