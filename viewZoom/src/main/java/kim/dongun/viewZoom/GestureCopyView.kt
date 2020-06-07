package kim.dongun.viewZoom

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.PointF
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

class GestureCopyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val DOUBLE_TAP_ZOOM_DURATION = 200
    private var scaleFactor = 1f // zoom scale 값
    private val minScaleFactor = 1f // min 줌 값
    private var maxScaleFactor = 3f // max 줌 값

    private var minTextSize: Int = 0
    private val maxTextSize: Int = 120

    private val textView: AppCompatTextView by lazy { findViewById<AppCompatTextView>(R.id.textView) }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.text_tag, this, false)
        addView(view)

//        setBackgroundColor(Color.BLACK)
        minTextSize = textView.textSize.pxToSp
    }

    private val gestureListener: SimpleOnGestureListener = object : SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
//            val textSize = textView.textSize
//            Log.d("Test/Log", "pixel: $textSize")
//            Log.d("Test/Log", "convert to sp: ${textSize.pxToSp}")
            Log.d("Test/Log", "View Coordinate ($x, $y)")
//            Log.d("Test/Log", "GestureListener onSingleTapUp")
            return super.onSingleTapUp(e)
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            e?.run {
//                zoomImageToPosition(getDoubleTapTargetScale(), e.x, e.y, DOUBLE_TAP_ZOOM_DURATION)
            }
//            Log.d("Test/Log", "GestureListener onDoubleTap.")
            return super.onDoubleTap(e)
        }

        override fun onScroll(
            e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float
        ): Boolean {
            if (status == Status.ONE_POINT || status == Status.TWO_POINT) { // action 상태가 zoom 일 경우 zoomable view 위치 이동
                this@GestureCopyView.x += midPoint.x - initMidPoint.x
                this@GestureCopyView.y += midPoint.y - initMidPoint.y
            }
            return super.onScroll(e1, e2, distanceX, distanceY)
        }
    }

    private val scaleListener: SimpleOnScaleGestureListener =
        object : SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                if (status == Status.TWO_POINT && detector != null && detector.scaleFactor != 0f) {
                    var newTextSize: Float = textView.textSize.pxToSp * detector.scaleFactor
                    newTextSize =
                        floor(max(minTextSize.toFloat(), min(newTextSize, maxTextSize.toFloat())))
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, newTextSize)
                }
                return true
            }
        }

    private val rotateListener: RotationGestureDetector.SimpleOnRotationGestureListener =
        object : RotationGestureDetector.SimpleOnRotationGestureListener() {
            override fun onRotation(rotationDetector: RotationGestureDetector?): Boolean {
                if (rotationDetector != null) {
//                Log.d("Test/Log", rotationDetector.getAngle().toString())
                    rotation += rotationDetector.getAngle()// * 1.05f
                }
                return true
            }
        }

    // 복사한 뷰의 source
    private lateinit var source: View

    private val gestureDetector: GestureDetector by lazy {
        GestureDetector(
            getContext(),
            gestureListener,
            null,
            true
        )
    }

    //    private val scaleDetector: ScaleGestureDetector by lazy { ScaleGestureDetector(getContext(), ScaleListener()) }
    private val scaleDetector: ScaleGestureDetector by lazy {
        ScaleGestureDetector(
            getContext(),
            scaleListener
        )
    }
    private val rotateDetector: RotationGestureDetector by lazy { RotationGestureDetector(listener = rotateListener) }

    private val initMidPoint: PointF = PointF()
    private val midPoint: PointF = PointF()

    var isRotateEnabled: Boolean = true
    var isScaleEnabled: Boolean = true
    var doubleTapScaleSteps = 5

    enum class Status { IDLE, ONE_POINT, TWO_POINT }

    private var status: Status = Status.ONE_POINT

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.run {
            if (event.pointerCount > 2) return true

            when (action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN,
                MotionEvent.ACTION_POINTER_DOWN -> {
                    status =
                        if (action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_DOWN) Status.ONE_POINT
                        else Status.TWO_POINT
                    initMidPoint.midPointOfEvent(this)
                }
                MotionEvent.ACTION_MOVE -> {
                    if (status == Status.ONE_POINT || status == Status.TWO_POINT) { // action 상태가 zoom 일 경우 zoomable view 위치 이동
                        midPoint.midPointOfEvent(event = this)
                    }
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

            if (isScaleEnabled)
                scaleDetector.onTouchEvent(this)

            if (isRotateEnabled)
                rotateDetector.onTouchEvent(this)
        }
        return true
    }

    /**
     * 뷰 복사
     *
     * @param source 복사 할 view
     */
    fun setSource(source: View) {
        this.source = source
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        if (::source.isInitialized)
            this.source.draw(canvas)
    }
}