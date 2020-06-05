package kim.dongun.viewZoom

import android.graphics.PointF
import android.view.MotionEvent
import kotlin.math.atan2

class RotationGestureDetector(private val listener: OnRotationGestureListener) {
    interface OnRotationGestureListener {
        fun onRotation(rotationDetector: RotationGestureDetector?): Boolean
    }

    open class SimpleOnRotationGestureListener : OnRotationGestureListener {
        override fun onRotation(rotationDetector: RotationGestureDetector?): Boolean = false
    }

    private val INVALID_POINTER_INDEX = -1

    private val firstFinger: PointF = PointF()
    private val secondFinger: PointF = PointF()

    private var pointerIndex1: Int = INVALID_POINTER_INDEX
    private var pointerIndex2: Int = INVALID_POINTER_INDEX

    private var angle: Float = 0f
    private var isFirstTouch = false

    fun getAngle(): Float = angle

    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                firstFinger.set(event.x, event.y)
                pointerIndex1 = event.findPointerIndex(event.getPointerId(0))
                angle = 0f
                isFirstTouch = true
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                secondFinger.set(event.x, event.y)
                pointerIndex2 = event.findPointerIndex(event.getPointerId(event.actionIndex))
                angle = 0f
                isFirstTouch = true
            }
            MotionEvent.ACTION_MOVE -> {
                if (pointerIndex1 != INVALID_POINTER_INDEX && pointerIndex2 != INVALID_POINTER_INDEX && event.pointerCount > pointerIndex2) {
                    val newFirstFinger =
                        PointF(event.getX(pointerIndex1), event.getY(pointerIndex1))
                    val newSecondFinger =
                        PointF(event.getX(pointerIndex2), event.getY(pointerIndex2))

                    if (isFirstTouch) {
                        angle = 0f
                        isFirstTouch = false
                    } else {
                        calculateAngleBetweenLines(
                            oldFirstFinger = firstFinger, oldSecondFinger = secondFinger,
                            newFirstFinger = newFirstFinger, newSecondFinger = newSecondFinger
                        )
                    }
                    listener.onRotation(this)
                    firstFinger.set(newFirstFinger)
                    secondFinger.set(newSecondFinger)
                }
            }
            MotionEvent.ACTION_UP -> {
                pointerIndex1 = INVALID_POINTER_INDEX
            }
            MotionEvent.ACTION_POINTER_UP -> {
                pointerIndex2 = INVALID_POINTER_INDEX
            }
        }
        return true
    }

    private fun calculateAngleBetweenLines(
        oldFirstFinger: PointF,
        oldSecondFinger: PointF,
        newFirstFinger: PointF,
        newSecondFinger: PointF
    ): Float =
        calculateAngleDelta(
            angleFrom = Math.toDegrees(
                atan2(
                    (oldFirstFinger.y - oldSecondFinger.y),
                    (oldFirstFinger.x - oldSecondFinger.x)
                ).toDouble()
            ).toFloat(),
            angleTo = Math.toDegrees(
                atan2(
                    (newFirstFinger.y - newSecondFinger.y),
                    (newFirstFinger.x - newSecondFinger.x)
                ).toDouble()
            ).toFloat()
        )

    private fun calculateAngleDelta(angleFrom: Float, angleTo: Float): Float {
        angle = angleTo % 360f - angleFrom % 360f

        if (angle < -180f)
            angle += 360f
        else if (angle > 180)
            angle -= 360f

        return angle
    }
}