package net.yanzm.fabtransformingsample.transition

import android.graphics.Path
import android.transition.PathMotion

class FabTransitionPathMotion : PathMotion() {

    override fun getPath(startX: Float, startY: Float, endX: Float, endY: Float): Path {
        val diffX = endX - startX
        val diffY = endY - startY

        return Path().apply {
            moveTo(startX, startY)
            if (diffX < 0 && diffY > 0) {
                quadTo(startX, endY, endX, endY)
            } else {
                quadTo(endX, startY, endX, endY)
            }
        }
    }
}
