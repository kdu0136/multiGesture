package kim.dongun.viewZoom

import android.view.ViewGroup

/**
 * 뷰 줌 타겟 컨테이너 인터페이스
 */
interface TargetContainer {
    /**
     * 뷰 줌 타겟 decor view(top-level window decor view) getter
     */
    fun getDecorView(): ViewGroup?
}