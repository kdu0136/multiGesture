package kim.dongun.viewZoom

import android.view.View

/**
 * 뷰 줌 인터페이스
 */
interface ZoomListener {
    /**
     * 뷰 줌 시작
     */
    fun onStartZoom(view: View)

    /**
     * 뷰 줌 끝
     */
    fun onEndZoom(view: View)
}