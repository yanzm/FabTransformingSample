package net.yanzm.fabtransformingsample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import net.yanzm.fabtransformingsample.transition.AndroidVersionAdapter;
import net.yanzm.fabtransformingsample.transition.FabTransformation;

public class MainActivity extends AppCompatActivity {

    private static final int HORIZONTAL_FACTOR = 2;
    private float diff;

    private ViewGroup sceneRoot;
    private View toolsContainer;
    private View tools;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolsContainer = findViewById(R.id.tools_container);
        tools = findViewById(R.id.tools);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        sceneRoot = (ViewGroup) findViewById(R.id.scene_root);
        sceneRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int[] toolsLocation = new int[2];
                toolsContainer.getLocationInWindow(toolsLocation);

                int[] fabLocation = new int[2];
                fab.getLocationInWindow(fabLocation);

                diff = (toolsLocation[1] + toolsContainer.getHeight() / 2)
                        - (fabLocation[1] + fab.getHeight() / 2);

                final float pivotX = fabLocation[0] + fab.getWidth() / 2 - toolsLocation[0] - diff * HORIZONTAL_FACTOR;
                toolsContainer.setPivotX(pivotX);
                tools.setPivotX(pivotX);

                sceneRoot.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFabMode(true, true);
            }
        });

        changeFabMode(false, false);

        // recycler view setup
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(new AndroidVersionAdapter());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    if (fab.getVisibility() != View.VISIBLE) {
                        changeFabMode(false, true);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (fab.getVisibility() != View.VISIBLE) {
            changeFabMode(false, true);
            return;
        }
        super.onBackPressed();
    }

    private void changeFabMode(boolean transformed, boolean animate) {
        if (animate) {
            final TransitionSet transition = new FabTransformation(transformed, fab.getHeight() / 2f);
            TransitionManager.beginDelayedTransition(sceneRoot, transition);
        }

        final float baseMargin = getResources().getDimension(R.dimen.fab_margin);
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) fab.getLayoutParams();
        params.bottomMargin = (int) (baseMargin - (transformed ? diff : 0));
        params.setMarginEnd((int) (baseMargin + (transformed ? diff * HORIZONTAL_FACTOR : 0)));
        fab.setLayoutParams(params);

        toolsContainer.setVisibility(transformed ? View.VISIBLE : View.INVISIBLE);
        tools.setVisibility(transformed ? View.VISIBLE : View.INVISIBLE);
        tools.setScaleX(transformed ? 1f : 0.8f);
        fab.setVisibility(transformed ? View.INVISIBLE : View.VISIBLE);
    }
}
