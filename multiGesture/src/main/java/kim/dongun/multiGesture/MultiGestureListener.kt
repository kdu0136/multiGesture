package kim.dongun.multiGesture

import android.annotation.SuppressLint
import android.graphics.PointF
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/**
 * MultiGesture (move, rotate, scale)
 */
class MultiGestureListener(
    private val target: View,
    private val gestureConfig: MultiGestureConfig,
    private val touchListener: TouchListener?
) : View.OnTouchListener {
    private enum class State { IDLE, ONE_POINT, TWO_POINT } // action 상태

    private var state: State =
        State.IDLE
    private var startRot: Float = 0f // init rotate value when first touch
    private var scaleFactor = 1f // current scale

    private val initMidPoint: PointF = PointF() // first touch pinch mid point
    private val midPoint: PointF = PointF()  // current pinch mid point

    private val gestureListener: GestureDetector.SimpleOnGestureListener =
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                touchListener?.onSingleTouch(view = target)
                return super.onSingleTapUp(e)
            }

            override fun onDoubleTap(e: MotionEvent?): Boolean {
                touchListener?.onDoubleTouch(view = target)
                return super.onDoubleTap(e)
            }

            override fun onLongPress(e: MotionEvent?) {
                touchListener?.onLongTouch(view = target)
                super.onLongPress(e)
            }

            override fun onScroll(
                e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float
            ): Boolean {
                if (state == State.ONE_POINT || state == State.TWO_POINT) { // action 상태가 zoom 일 경우 zoomable view 위치 이동
                    val origin = PointF(target.x + target.width / 2, target.y + target.height / 2)
                    val rotateInitPoint =
                        rotatePoint(
                            point = initMidPoint,
                            origin = origin,
                            rotation = target.rotation.toDouble()
                        )
                    val rotateMidPoint =
                        rotatePoint(
                            point = midPoint,
                            origin = origin,
                            rotation = target.rotation.toDouble()
                        )

                    target.x += rotateMidPoint.x - rotateInitPoint.x
                    target.y += rotateMidPoint.y - rotateInitPoint.y
                }
                return false
            }
        }

    private val scaleListener: ScaleGestureDetector.SimpleOnScaleGestureListener =
        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                if (state == State.TWO_POINT && detector != null) {
                    scaleFactor *= detector.scaleFactor
                    scaleFactor = max(
                        gestureConfig.minScaleFactor,
                        min(scaleFactor, gestureConfig.maxScaleFactor)
                    )

                    target.scaleX = scaleFactor
                    target.scaleY = scaleFactor
                }
                return false
            }
        }

    private val rotateListener: RotationGestureDetector.SimpleOnRotationGestureListener =
        object : RotationGestureDetector.SimpleOnRotationGestureListener() {
            override fun onRotation(rotationDetector: RotationGestureDetector?): Boolean {
                if (rotationDetector != null && state == State.TWO_POINT) {
                    target.rotation = startRot - rotationDetector.getAngle()
                }
                return false
            }
        }

    private val gestureDetector: GestureDetector by lazy {
        GestureDetector(
            target.context,
            gestureListener,
            null,
            true
        )
    }

    private val scaleDetector: ScaleGestureDetector by lazy {
        ScaleGestureDetector(
            target.context,
            scaleListener
        )
    }

    private val rotateDetector: RotationGestureDetector by lazy {
        RotationGestureDetector(
            listener = rotateListener,
            view = target
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        if (event.pointerCount > 2) return true

        if (gestureConfig.isScaleEnabled)
            scaleDetector.onTouchEvent(event)

        if (gestureConfig.isRotateEnabled)
            rotateDetector.onTouchEvent(event)

        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                state = State.ONE_POINT
                initMidPoint.midPointOfEvent(event = event)
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                state = State.TWO_POINT
                initMidPoint.midPointOfEvent(event = event)

                startRot = target.rotation
            }
            MotionEvent.ACTION_MOVE -> {
                if (state == State.ONE_POINT || state == State.TWO_POINT)
                    midPoint.midPointOfEvent(event = event)
            }
            MotionEvent.ACTION_POINTER_UP,
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> { // 한개 또는 두개 포인트에 대한 up 또는 액션 cancel
                when (state) {
                    State.IDLE -> {
                    }
                    State.ONE_POINT,
                    State.TWO_POINT -> state =
                        State.IDLE
                }
            }
        }

        if (gestureConfig.isSimpleGestureEnabled)
            gestureDetector.onTouchEvent(event)
        return true
    }

    private fun PointF.midPointOfEvent(event: MotionEvent) {
        when (event.pointerCount) {
            1 -> set(event.x, event.y)
            2 -> {
                val x = event.getX(0) + event.getX(1)
                val y = event.getY(0) + event.getY(1)
                set(x / 2, y / 2)
            }
        }
    }

    /**
     * return point value consider current view rotation and origin
     *
     * @param point current point
     * @param point view origin
     * @param rotation view rotation
     */
    private fun rotatePoint(point: PointF, origin: PointF, rotation: Double): PointF {
        val radians = Math.toRadians(rotation)
        val x = point.x - origin.x
        val y = point.y - origin.y
        val newX = ((x * cos(radians)) - (y * sin(radians))) + origin.x
        val newY = ((x * sin(radians)) + (y * cos(radians))) + origin.y
        return PointF(newX.toFloat(), newY.toFloat())
    }
}