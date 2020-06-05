package kim.dongun.viewZoom

import android.graphics.PointF
import android.view.MotionEvent

/**
 * Motion 기능 object
 */
object MotionUtils {
    /**
     * event 발생 한 point 중심 좌표로 설정
     *
     * @param point point 중심 좌표
     * @param event 발생 이벤트
     */
    fun midPointOfEvent(point: PointF, event: MotionEvent) {
        if (event.pointerCount == 2) {
            val x = event.getX(0) + event.getX(1)
            val y = event.getY(0) + event.getY(1)
            point.set(x / 2, y / 2)
        }
    }
}

fun PointF.midPointOfEvent(event: MotionEvent) {
    if (event.pointerCount > 1) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        set(x / 2, y / 2)
    }
}