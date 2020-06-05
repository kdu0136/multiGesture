package kim.dongun.viewZoom

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.View
import androidx.core.view.ViewCompat
import kotlin.math.max
import kotlin.math.min

/**
 * 뷰 복사 클래스
 */
class GestureCopyView@JvmOverloads constructor(context: Context,
                                               attrs: AttributeSet? = null,
                                               defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private val DOUBLE_TAP_ZOOM_DURATION = 200
    private var scaleFactor = 1f // zoom scale 값
    private val minScaleFactor = 1f // min 줌 값
    private var maxScaleFactor = 2f // max 줌 값

    private val gestureListener: SimpleOnGestureListener = object: SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
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
        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float
        ): Boolean {
//            Log.d("Test/Log", "GestureListener onScroll ($distanceX, $distanceY) / view ($x, $y)")
//            x += -distanceX
//            y += -distanceY
//            val offSetX = distanceX * mCurrentViewport.width() / width
//            val offSetY = -distanceY * mCurrentViewport.height() / height
//            setViewportBottomLeft(x = mCurrentViewport.left + offSetX, y = mCurrentViewport.bottom + offSetY)
//            postTranslate(-distanceX, -distanceY)
            return super.onScroll(e1, e2, distanceX, distanceY)
        }
    }

    private val scaleListener: SimpleOnScaleGestureListener = object: SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            if (detector != null) {
                scaleFactor *= detector.scaleFactor
                // 최대 줌 설정
                scaleFactor = max(minScaleFactor, min(scaleFactor, maxScaleFactor))
                scaleX = scaleFactor
                scaleY = scaleFactor
            }
            return true
        }
    }

    private val rotateListener: RotationGestureDetector.SimpleOnRotationGestureListener = object: RotationGestureDetector.SimpleOnRotationGestureListener() {
        override fun onRotation(rotationDetector: RotationGestureDetector?): Boolean {
            if (rotationDetector != null)
                rotation += rotationDetector.getAngle()
            return false
        }
    }

    // 복사한 뷰의 source
    private lateinit var source: View

    private val gestureDetector: GestureDetector by lazy { GestureDetector(getContext(), gestureListener, null, true) }
//    private val scaleDetector: ScaleGestureDetector by lazy { ScaleGestureDetector(getContext(), ScaleListener()) }
    private val scaleDetector: ScaleGestureDetector by lazy { ScaleGestureDetector(getContext(), scaleListener) }
    private val rotateDetector: RotationGestureDetector by lazy { RotationGestureDetector(listener = rotateListener) }

    private var initialPinchMidPoint: PointF = PointF() // pinch 시작 초기 중심점
    private val midPoint: PointF = PointF()

    var isRotateEnabled: Boolean = false
    var isScaleEnabled: Boolean = true
    var doubleTapScaleSteps = 5
    var start = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.run {
//            if ((action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
////                cancelAllAnimations() TODO: CREATE FUNC
//            }
            if (action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_MOVE) {
//                val data = ClipData("", arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), ClipData.Item(""))
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//                    startDragAndDrop(data, DragShadowBuilder(this@GestureCopyView), this, 0)
//                else
//                    startDrag(data, DragShadowBuilder(this@GestureCopyView), this, 0)
            }
            midPoint.midPointOfEvent(event = this)
            gestureDetector.onTouchEvent(this)

            if (isScaleEnabled)
                scaleDetector.onTouchEvent(this)

            if (isRotateEnabled)
                rotateDetector.onTouchEvent(this)

            if ((action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
//                setImageToWrapCropBounds() TODO: CREATE FUNC
            }

//            when (action and MotionEvent.ACTION_MASK ) {
//                MotionEvent.ACTION_POINTER_DOWN -> {
//                    start = true
//                    initialPinchMidPoint.midPointOfEvent(event = this)
//                }
//                MotionEvent.ACTION_MOVE -> {
//                    if (start) {
//                        val offSetX = initialPinchMidPoint.x - midPoint.x
//                        val offSetY = initialPinchMidPoint.y - midPoint.y
//
//                        this@GestureCopyView.x -= offSetX
//                        this@GestureCopyView.y -= offSetY
//                    }
//
//                }
//                MotionEvent.ACTION_POINTER_UP,
//                MotionEvent.ACTION_UP,
//                MotionEvent.ACTION_CANCEL -> { // 한개 또는 두개 포인트에 대한 up 또는 액션 cancel
//                    start = false
//                }
//                else -> {}
//            }
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