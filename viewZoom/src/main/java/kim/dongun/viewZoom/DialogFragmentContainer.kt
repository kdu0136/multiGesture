package kim.dongun.viewZoom

import androidx.fragment.app.DialogFragment

/**
 * 뷰 줌 컨테이너(root context dialog fragment)
 *
 * @param dialogFragment
 */
class DialogFragmentContainer(dialogFragment: DialogFragment) : DialogContainer(dialog = dialogFragment.dialog!!)