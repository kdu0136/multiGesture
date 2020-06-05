package kim.dongun.viewZoom

import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.View

/**
 * 뷰 복사 클래스
 */
class GestureCopyView@JvmOverloads constructor(context: Context,
                                               attrs: AttributeSet? = null,
                                               defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private val DOUBLE_TAP_ZOOM_DURATION = 200

    private class GestureListener : SimpleOnGestureListener() {

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            e?.run {
//                zoomImageToPosition(getDoubleTapTargetScale(), e.x, e.y, DOUBLE_TAP_ZOOM_DURATION)
            }
            return super.onDoubleTap(e)
        }
        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float
        ): Boolean {
//            postTranslate(-distanceX, -distanceY)
            return true
        }
    }

    private class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
//            postScale(detector.scaleFactor, mMidPntX, mMidPntY)
            return true
        }
    }

    private class RotateListener : RotationGestureDetector.SimpleOnRotationGestureListener() {
        override fun onRotation(rotationDetector: RotationGestureDetector?): Boolean = true
    }

    // 복사한 뷰의 source
    private lateinit var source: View

    private val gestureDetector: GestureDetector by lazy { GestureDetector(getContext(), GestureListener(), null, true) }
    private val scaleDetector: ScaleGestureDetector by lazy { ScaleGestureDetector(getContext(), ScaleListener()) }
    private val rotateDetector: RotationGestureDetector by lazy { RotationGestureDetector(listener = RotateListener()) }

    private val midPoint: PointF = PointF()

    var isRotateEnabled: Boolean = true
    var isScaleEnabled: Boolean = true
    var doubleTapScaleSteps = 5

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.run {
            if ((action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
//                cancelAllAnimations() TODO: CREATE FUNC
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
        }
        return true
    }

//    private fun getDoubleTapTargetScale(): Float =
//        getCurrentScale() * Math.pow(getMaxScale() / getMinScale(), 1f / doubleTapScaleSteps)

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