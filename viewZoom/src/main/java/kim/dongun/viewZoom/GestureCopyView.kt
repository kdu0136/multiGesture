package kim.dongun.viewZoom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
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

    private val mainView: CardView by lazy { findViewById<CardView>(R.id.mainLayout) }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.text_tag, this, false)
        addView(view)
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.CENTER

        setBackgroundColor(Color.BLACK)

        cal()
    }

    private val gestureListener: SimpleOnGestureListener = object : SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
//            val location = intArrayOf(0, 0)
//            getLocationOnScreen(location)
//            Log.d("Test/Log", "(${location[0]}, ${location[1]})")
            Log.d("Test/Log", "(${mainView.width}, ${mainView.height}), rotation: $rotation")
//            Log.d("Test/Log", "initMidPoint (${oldMidPoint.x}, ${oldMidPoint.y})")
//            Log.d("Test/Log", "midPoint (${midPoint.x}, ${midPoint.y})")
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
//                Log.d("Test/Log", "initMidPoint (${oldMidPoint.x}, ${oldMidPoint.y})")
//                Log.d("Test/Log", "midPoint (${midPoint.x}, ${midPoint.y})")
                this@GestureCopyView.x += midPoint.x - oldMidPoint.x
                this@GestureCopyView.y += midPoint.y - oldMidPoint.y
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
//                    mainView.rotation = startRot - rotationDetector.getAngle()
                    mainView.rotation = startRot - rotationDetector.getAngle()
//                    Log.d("Test/Log", "mainView size (${mainView.width}, ${mainView.height})")
//                    cal()
                }
                return false
            }
        }

    private fun cal() {
        mainView.post {
            val rotation = 45f
            Log.d("Test/Log", "(${mainView.width}, ${mainView.height}), rotation: $rotation")

            val w = (mainView.width * cos(rotation)) +
                    (mainView.height * cos(90 - rotation))

            val h = (mainView.width * sin(rotation)) +
                    (mainView.height * sin(90 - rotation))

            val max = sqrt(mainView.width.toDouble().pow(2) + mainView.height.toDouble().pow(2)).toInt()

//            val max = abs(floor(max(mainView.width.toDouble() * 1.1, mainView.height.toDouble() * 1.1))).toInt()

            Log.d("Test/Log", "new ($w, $h), rotation: $rotation, max: $max")

            val params = layoutParams
            params.width = max
            params.height = max
            layoutParams = params
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

    private val oldMidPoint: PointF = PointF()
    private val midPoint: PointF = PointF()

    var isRotateEnabled: Boolean = true
    var isScaleEnabled: Boolean = true

    enum class Status { IDLE, ONE_POINT, TWO_POINT }

    private var status: Status = Status.ONE_POINT

    private var startRot: Float = 0f

//    private val INVALID_POINTER_INDEX = -1
//    private var ptrId1: Int = INVALID_POINTER_INDEX
//    private var ptrId2: Int = INVALID_POINTER_INDEX

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.run {
            if (event.pointerCount > 2) return true

            if (isScaleEnabled)
                scaleDetector.onTouchEvent(this)

            if (isRotateEnabled)
                rotateDetector.onTouchEvent(this)

            when (action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
//                    ptrId1 = event.getPointerId(event.actionIndex)
                    status = Status.ONE_POINT

//                    val startFinger = PointF()
//                    getRawPoint(event = event, index = ptrId1, point = startFinger)
//                    oldMidPoint.midPointOfEvent(sp = startFinger, fp = null)

                    oldMidPoint.midPointOfEvent(this)
                }
                MotionEvent.ACTION_POINTER_DOWN -> {
//                    ptrId2 = event.getPointerId(event.actionIndex)
                    status = Status.TWO_POINT

//                    val startFinger = PointF()
//                    val finishFinger = PointF()
//
//                    getRawPoint(event = event, index = ptrId1, point = startFinger)
//                    getRawPoint(event = event, index = ptrId2, point = finishFinger)
//                    oldMidPoint.midPointOfEvent(sp = startFinger, fp = finishFinger)

                    oldMidPoint.midPointOfEvent(this)

                    startRot = mainView.rotation
                }
                MotionEvent.ACTION_MOVE -> {
//                    if (status == Status.ONE_POINT) { // action 상태가 zoom 일 경우 zoomable view 위치 이동
//                        val startFinger = PointF()
//                        getRawPoint(event = event, index = ptrId1, point = startFinger)
//                        midPoint.midPointOfEvent(sp = startFinger, fp = null)
//                    } else if (status == Status.TWO_POINT) { // action 상태가 zoom 일 경우 zoomable view 위치 이동
//                        val startFinger = PointF()
//                        getRawPoint(event = event, index = ptrId1, point = startFinger)
//
//                        val finishFinger = PointF()
//                        getRawPoint(event = event, index = ptrId2, point = finishFinger)
//                        midPoint.midPointOfEvent(sp = startFinger, fp = finishFinger)
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

//            if (status == Status.TWO_POINT) {
//                val
//            }
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