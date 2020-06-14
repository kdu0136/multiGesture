package kim.dongun.multiGesture

import android.view.View

interface TransformListener {
    fun onMove(view: View)
    fun onRotate(view: View)
    fun onScale(view: View)
}