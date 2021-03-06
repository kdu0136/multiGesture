package kim.dongun.multiGesture

import android.view.View

interface TouchListener {
    fun onSingleTouch(view: View)
    fun onDoubleTouch(view: View)
    fun onLongTouch(view: View)
    fun onStartTouch(view: View)
    fun onEndTouch(view: View)
}