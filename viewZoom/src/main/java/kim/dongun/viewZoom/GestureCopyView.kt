package kim.dongun.viewZoom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.widget.FrameLayout
import kotlin.math.*

class GestureCopyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val DOUBLE_TAP_ZOOM_DURATION = 200
    private var scaleFactor = 1f // zoom scale 값
    private val minScaleFactor = 1f // min 줌 값
    private var maxScaleFactor = 2f // max 줌 값

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.text_tag, this, false)
        addView(view)
    }

    private val gestureListener: SimpleOnGestureListener = object : SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            Log.d("Test/Log", "View Coordinate ($x, $y)")
            return super.onSingleTapUp(e)
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            e?.run {
//                zoomImageToPosition(getDoubleTapTargetScale(), e.x, e.y, DOUBLE_TAP_ZOOM_DURATION)
            }
            return super.onDoubleTap(e)
        }

        override fun onScroll(
            e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float
        ): Boolean {
            if (status == Status.ONE_POINT || status == Status.TWO_POINT) { // action 상태가 zoom 일 경우 zoomable view 위치 이동
                this@GestureCopyView.x += midPoint.x - initMidPoint.x
                this@GestureCopyView.y += midPoint.y - initMidPoint.y
            }
            return false
        }
    }

    private val scaleListener: SimpleOnScaleGestureListener =
        object : SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                if (status == Status.TWO_POINT && detector != null) {
                    scaleFactor *= detector.scaleFactor
                    scaleFactor = max(minScaleFactor, min(scaleFactor, maxScaleFactor))

                    scaleX = scaleFactor
                    scaleY = scaleFactor
                }
                return false
            }
        }

    private val rotateListener: RotationGestureDetector.SimpleOnRotationGestureListener =
        object : RotationGestureDetector.SimpleOnRotationGestureListener() {
            override fun onRotation(rotationDetector: RotationGestureDetector?): Boolean {
                if (rotationDetector != null && status == Status.TWO_POINT) {
//                    Log.d("Test/Log", rotationDetector.getAngle().toString())
                    rotation = startRot - rotationDetector.getAngle()
                }
                return false
            }
        }

    private val gestureDetector: GestureDetector by lazy {
        GestureDetector(
            getContext(),
            gestureListener,
            null,
            true
        )
    }

    private val scaleDetector: ScaleGestureDetector by lazy {
        ScaleGestureDetector(
            getContext(),
            scaleListener
        )
    }
    private val rotateDetector: RotationGestureDetector by lazy {
        RotationGestureDetector(
            listener = rotateListener,
            view = this
        )
    }

    private val initMidPoint: PointF = PointF()
    private val midPoint: PointF = PointF()

    var isRotateEnabled: Boolean = false
    var isScaleEnabled: Boolean = true

    enum class Status { IDLE, ONE_POINT, TWO_POINT }

    private var status: Status = Status.ONE_POINT

    private var startRot: Float = 0f

    private val INVALID_POINTER_INDEX = -1
    private var ptrId1: Int = INVALID_POINTER_INDEX
    private var ptrId2: Int = INVALID_POINTER_INDEX

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.run {
            if (event.pointerCount > 2) return true

            when (action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    ptrId1 = event.getPointerId(event.actionIndex)
                    status = Status.ONE_POINT

//                    val startFinger = PointF()
//                    getRawPoint(event = event, index = ptrId1, point = startFinger)
//                    initMidPoint.midPointOfEvent(sp = startFinger, fp = null)

                    initMidPoint.midPointOfEvent(this)
                }
                MotionEvent.ACTION_POINTER_DOWN -> {
                    status = Status.TWO_POINT
                    ptrId2 = event.getPointerId(event.actionIndex)

//                    val startFinger = PointF()
//                    val finishFinger = PointF()
//
//                    getRawPoint(event = event, index = ptrId1, point = startFinger)
//                    getRawPoint(event = event, index = ptrId2, point = finishFinger)
//                    initMidPoint.midPointOfEvent(sp = startFinger, fp = finishFinger)

                    initMidPoint.midPointOfEvent(this)

                    startRot = rotation
                }
                MotionEvent.ACTION_MOVE -> {
//                    if (status == Status.ONE_POINT) { // action 상태가 zoom 일 경우 zoomable view 위치 이동
//                        val startFinger = PointF()
//                        getRawPoint(event = event, index = ptrId1, point = startFinger)
//                        midPoint.midPointOfEvent(sp = startFinger, fp = null)
//
////                        midPoint.midPointOfEvent(event = this)
//                    } else if (status == Status.TWO_POINT) { // action 상태가 zoom 일 경우 zoomable view 위치 이동
//                        val startFinger = PointF()
//                        getRawPoint(event = event, index = ptrId1, point = startFinger)
//
//                        val finishFinger = PointF()
//                        getRawPoint(event = event, index = ptrId2, point = finishFinger)
//                        midPoint.midPointOfEvent(sp = startFinger, fp = finishFinger)
//
////                        midPoint.midPointOfEvent(event = this)
//                    }
                    if (status == Status.ONE_POINT || status == Status.TWO_POINT)
                        midPoint.midPointOfEvent(event = this)
                }
                MotionEvent.ACTION_POINTER_UP,
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL -> { // 한개 또는 두개 포인트에 대한 up 또는 액션 cancel
                    when (status) {
                        Status.IDLE -> {
                        }
                        Status.ONE_POINT,
                        Status.TWO_POINT -> status = Status.IDLE
                    }
                }
            }

            gestureDetector.onTouchEvent(this)

            if (isScaleEnabled) {
                scaleDetector.onTouchEvent(this)
            }

            if (isRotateEnabled)
                rotateDetector.onTouchEvent(this)

        }
        return true
    }

    private fun getRawPoint(event: MotionEvent, index: Int, point: PointF) {
        val location = intArrayOf(0, 0)
        getLocationOnScreen(location)

        var x = event.getX(index)
        var y = event.getY(index)

        val angle = Math.toDegrees(atan2(y, x).toDouble()) + rotation

        val length = PointF.length(x, y)

        x = (length * cos(Math.toRadians(angle))).toFloat() + location[0]
        y = (length * sin(Math.toRadians(angle))).toFloat() + location[1]

        point.set(x, y)
    }
}