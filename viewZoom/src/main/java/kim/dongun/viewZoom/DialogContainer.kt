package kim.dongun.viewZoom

import android.app.Dialog
import android.view.ViewGroup

/**
 * 뷰 줌 컨테이너(root context dialog)
 *
 * @param dialog
 */
open class DialogContainer(private val dialog: Dialog) : TargetContainer {
    override fun getDecorView(): ViewGroup? = dialog.window?.decorView as ViewGroup
}