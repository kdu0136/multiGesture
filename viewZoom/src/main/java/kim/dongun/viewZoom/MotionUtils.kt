package kim.dongun.viewZoom

import android.content.res.Resources
import android.graphics.PointF
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import kotlin.math.roundToInt

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
        if (event.pointerCount == 1) {
            point.set(event.x, event.y)
        } else {
            val x = event.getX(0) + event.getX(1)
            val y = event.getY(0) + event.getY(1)
            point.set(x / 2, y / 2)
        }
    }
}

fun PointF.midPointOfEvent(event: MotionEvent) {
    when (event.pointerCount) {
        1 -> set(event.x, event.y)
        2 -> {
            val x = event.getX(0) + event.getX(1)
            val y = event.getY(0) + event.getY(1)
            set(x / 2, y / 2)
        }
    }
}

fun PointF.midPointOfEvent(sp: PointF, fp: PointF?) {
    if (fp == null) {
        set(sp.x, sp.y)
    } else {
        val x = sp.x + fp.x
        val y = sp.y + fp.y
        set(x / 2, y / 2)
    }
}

val Float.pxToSp: Int get() = (this / Resources.getSystem().displayMetrics.scaledDensity).roundToInt()