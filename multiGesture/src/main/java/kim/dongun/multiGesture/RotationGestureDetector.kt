package kim.dongun.multiGesture

import android.graphics.PointF
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class RotationGestureDetector(
    private val listener: OnRotationGestureListener,
    private val view: View
) {
    interface OnRotationGestureListener {
        fun onRotation(rotationDetector: RotationGestureDetector?): Boolean
    }

    open class SimpleOnRotationGestureListener :
        OnRotationGestureListener {
        override fun onRotation(rotationDetector: RotationGestureDetector?): Boolean = false
    }

    private val INVALID_POINTER_INDEX = -1

    private val startFinger: PointF = PointF()
    private val finishFinger: PointF = PointF()

    private var ptrId1: Int = INVALID_POINTER_INDEX
    private var ptrId2: Int = INVALID_POINTER_INDEX

    private var angle: Float = 0f

    fun getAngle(): Float = angle

    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                ptrId1 = event.getPointerId(event.actionIndex)
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                ptrId2 = event.getPointerId(event.actionIndex)

                startFinger.getRawPoint(event = event, index = ptrId1)
                finishFinger.getRawPoint(event = event, index = ptrId2)
            }
            MotionEvent.ACTION_MOVE -> {
                if (ptrId1 != INVALID_POINTER_INDEX && ptrId2 != INVALID_POINTER_INDEX) {
                    val newStartFinger = PointF()
                    val newFinishFinger = PointF()

                    newStartFinger.getRawPoint(event = event, index = ptrId1)
                    newFinishFinger.getRawPoint(event = event, index = ptrId2)

                    angle = angleBetweenLines(
                        oldStartFinger = startFinger, oldFinishFinger = finishFinger,
                        newFStartFinger = newStartFinger, newFinishFinger = newFinishFinger
                    )

                    listener.onRotation(this)
                }
            }
            MotionEvent.ACTION_UP -> {
                ptrId1 = INVALID_POINTER_INDEX
            }
            MotionEvent.ACTION_POINTER_UP -> {
                ptrId2 = INVALID_POINTER_INDEX
            }
            MotionEvent.ACTION_CANCEL -> {
                ptrId1 = INVALID_POINTER_INDEX
                ptrId2 = INVALID_POINTER_INDEX
            }
        }
        return true
    }

    /**
     * set point value consider current view rotation
     */
    private fun PointF.getRawPoint(event: MotionEvent, index: Int) {
        val location = intArrayOf(0, 0)
        view.getLocationOnScreen(location)

        var x = event.getX(index)
        var y = event.getY(index)

        val angle = Math.toDegrees(atan2(y, x).toDouble()) + view.rotation

        val length = PointF.length(x, y)

        x = (length * cos(Math.toRadians(angle))).toFloat() + location[0]
        y = (length * sin(Math.toRadians(angle))).toFloat() + location[1]

        set(x, y)
    }

    /**
     * return angle between two lines
     */
    private fun angleBetweenLines(
        oldStartFinger: PointF,
        oldFinishFinger: PointF,
        newFStartFinger: PointF,
        newFinishFinger: PointF
    ): Float {
        val angle1 = atan2(
            (oldFinishFinger.y - oldStartFinger.y),
            (oldFinishFinger.x - oldStartFinger.x)
        ).toDouble()
        val angle2 = atan2(
            (newFinishFinger.y - newFStartFinger.y),
            (newFinishFinger.x - newFStartFinger.x)
        ).toDouble()

        var angle = (Math.toDegrees(angle1 - angle2).toFloat()) % 360
        if (angle < -180f) angle += 360f
        if (angle > 180f) angle -= 360f
        return angle
    }
}