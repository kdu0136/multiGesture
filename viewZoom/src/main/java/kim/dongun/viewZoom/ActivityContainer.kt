package kim.dongun.viewZoom

import android.app.Activity
import android.view.ViewGroup

/**
 * 뷰 줌 컨테이너(root context activity)
 *
 * @param activity
 */
class ActivityContainer(private val activity: Activity) : TargetContainer {
    override fun getDecorView(): ViewGroup? = activity.window?.decorView as ViewGroup
}