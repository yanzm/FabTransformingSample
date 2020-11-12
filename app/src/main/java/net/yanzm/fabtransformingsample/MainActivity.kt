package net.yanzm.fabtransformingsample

import android.os.Bundle
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import net.yanzm.fabtransformingsample.transition.AndroidVersionAdapter
import net.yanzm.fabtransformingsample.transition.FabTransformation

class MainActivity : AppCompatActivity() {

    private var diff = 0f

    private lateinit var sceneRoot: ViewGroup
    private lateinit var toolsContainer: View
    private lateinit var tools: View
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolsContainer = findViewById(R.id.tools_container)
        tools = findViewById(R.id.tools)
        fab = findViewById(R.id.fab)
        sceneRoot = findViewById(R.id.scene_root)

        sceneRoot.doOnLayout {
            val toolsLocation = IntArray(2).also {
                toolsContainer.getLocationInWindow(it)
            }
            val fabLocation = IntArray(2).also {
                fab.getLocationInWindow(it)
            }

            // diff between center of toolsContainer and center of fabLocation on Y-axis
            diff =
                toolsLocation[1] + toolsContainer.height / 2f - (fabLocation[1] + fab.height / 2f)

            val pivotX =
                fabLocation[0] + fab.width / 2 - toolsLocation[0] - diff * HORIZONTAL_FACTOR
            toolsContainer.pivotX = pivotX
            tools.pivotX = pivotX
        }

        fab.setOnClickListener {
            changeFabMode(transformed = true, animate = true)
        }

        changeFabMode(transformed = false, animate = false)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.apply {
            setHasFixedSize(true)
            adapter = AndroidVersionAdapter()
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState != RecyclerView.SCROLL_STATE_IDLE && fab.visibility != View.VISIBLE) {
                        changeFabMode(transformed = false, animate = true)
                    }
                }
            })
        }
    }

    override fun onBackPressed() {
        if (fab.visibility != View.VISIBLE) {
            changeFabMode(transformed = false, animate = true)
            return
        }
        super.onBackPressed()
    }

    private fun changeFabMode(transformed: Boolean, animate: Boolean) {
        if (animate) {
            val transition: TransitionSet = FabTransformation(transformed, fab.height / 2f)
            TransitionManager.beginDelayedTransition(sceneRoot, transition)
        }
        val baseMargin = resources.getDimension(R.dimen.fab_margin)
        val params = fab.layoutParams as FrameLayout.LayoutParams
        params.bottomMargin = (baseMargin - if (transformed) diff else 0f).toInt()
        params.marginEnd = (baseMargin + if (transformed) diff * HORIZONTAL_FACTOR else 0f).toInt()
        fab.layoutParams = params

        toolsContainer.visibility = if (transformed) View.VISIBLE else View.INVISIBLE
        tools.visibility = if (transformed) View.VISIBLE else View.INVISIBLE
        tools.scaleX = if (transformed) 1f else 0.8f
        fab.visibility = if (transformed) View.INVISIBLE else View.VISIBLE
    }

    companion object {
        private const val HORIZONTAL_FACTOR = 2
    }
}
