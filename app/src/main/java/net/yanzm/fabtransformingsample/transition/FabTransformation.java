package net.yanzm.fabtransformingsample.transition;

import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.transition.Visibility;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import net.yanzm.fabtransformingsample.R;

public class FabTransformation extends TransitionSet {

    public FabTransformation(boolean isChecked, float startRadius) {
        setOrdering(ORDERING_SEQUENTIAL);
        if (isChecked) {
            final Transition fabTransition = new TransitionSet()
                    .setDuration(100)
                    .setOrdering(ORDERING_TOGETHER)
                    .addTransition(new ChangeBounds())
                    .addTransition(new ImageFade(Visibility.MODE_OUT))
                    .setInterpolator(new AccelerateInterpolator());
            fabTransition.setPathMotion(new FabTransitionPathMotion());

            final TransitionSet toolsTransition = new TransitionSet()
                    .setDuration(200)
                    .setOrdering(ORDERING_TOGETHER)
                    .addTransition(new CircularRevealTransition(Visibility.MODE_IN, startRadius))
                    .addTransition(new ChangeTransform().addTarget(R.id.tools))
                    .addTransition(new Fade(Fade.IN).addTarget(R.id.tools));

            addTransition(fabTransition)
                    .addTransition(toolsTransition);

        } else {
            final Transition fabTransition = new TransitionSet()
                    .setDuration(100)
                    .setOrdering(ORDERING_TOGETHER)
                    .addTransition(new ChangeBounds())
                    .addTransition(new ImageFade(Visibility.MODE_IN))
                    .setInterpolator(new DecelerateInterpolator());
            fabTransition.setPathMotion(new FabTransitionPathMotion());

            final TransitionSet toolsTransition = new TransitionSet()
                    .setDuration(200)
                    .setOrdering(ORDERING_TOGETHER)
                    .addTransition(new CircularRevealTransition(Visibility.MODE_OUT, startRadius))
                    .addTransition(new ChangeTransform().addTarget(R.id.tools))
                    .addTransition(new Fade(Fade.OUT).addTarget(R.id.tools));

            addTransition(toolsTransition)
                    .addTransition(fabTransition);
        }
    }
}
