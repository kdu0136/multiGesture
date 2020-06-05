package kim.dongun.viewZoom

import android.view.View

/**
 * 뷰 줌 타겟 터치 인터페이스
 */
interface TouchListener {
    /**
     * 이미지 타겟 터치
     */
    fun onTouch(v: View)

    /**
     * 이미지 타겟 더블 터치
     */
    fun onDoubleTouch(v: View)

    /**
     * 이미지 타겟 롱 터치
     */
    fun onLongTouch(v: View)
}