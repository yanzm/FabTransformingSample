package net.yanzm.fabtransformingsample.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageFade extends Visibility {

    public ImageFade() {
    }

    public ImageFade(int mode) {
        setMode(mode);
    }

    @Override
    public Animator onAppear(ViewGroup sceneRoot, final View view, TransitionValues startValues,
                             TransitionValues endValues) {

        view.setAlpha(0f);

        if (!(view instanceof ImageView)) {
            return null;
        }

        final ImageView iv = (ImageView) view;
        final Drawable drawable = iv.getDrawable();

        if (drawable == null) {
            return null;
        }

        drawable.setAlpha(0);
        final ValueAnimator anim = ValueAnimator.ofInt(0, 255);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                final int alpha = (int) valueAnimator.getAnimatedValue();
                drawable.setAlpha(alpha);
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setAlpha(1f);
            }
        });
        return anim;
    }

    @Override
    public Animator onDisappear(ViewGroup sceneRoot, final View view, TransitionValues startValues,
                                TransitionValues endValues) {
        if (!(view instanceof ImageView)) {
            return null;
        }

        final ImageView iv = (ImageView) view;
        final Drawable drawable = iv.getDrawable();

        if (drawable == null) {
            return null;
        }

        final ValueAnimator anim = ValueAnimator.ofInt(255, 0);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                final int alpha = (int) valueAnimator.getAnimatedValue();
                drawable.setAlpha(alpha);
            }
        });
        return anim;
    }
}
