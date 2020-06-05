package kim.dongun.viewZoom

import android.graphics.Color
import android.graphics.Point
import android.graphics.PointF
import android.view.*
import android.view.animation.Interpolator
import kotlin.math.max
import kotlin.math.min

/**
 * 뷰 줌 터치 리스너
 */
class ZoomableTouchListener(private val targetContainer: TargetContainer,
                            private val target: View,
                            private val zoomConfig: ZoomConfig,
                            private val zoomInterpolator: Interpolator,
                            private val touchListener: TouchListener?,
                            private val zoomListener: ZoomListener?) : View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener {
    private enum class State { IDLE, POINTER_DOWN, ZOOMING } // action 상태

    private var zoomableView: CopyView? = null // zoom 가능한 view (target 기반으로 생성)
    private var shadow: View? = null
    private var state: State = State.IDLE
    private var animationZoomEnding = false // view zoom ending animation 동작 중 판단
    private var initialPinchMidPoint: PointF = PointF() // pinch 시작 초기 중심점
    private var currentMovementMidPoint: PointF = PointF() // 현재 pinch pint movement 중심점
    private var targetViewCoordinate: Point = Point() // target view 좌표값
    private var scaleFactor = 1f // zoom scale 값

    // zoom 끝났을 때 액션
    private val endZoomAction: Runnable = Runnable {
        // decor view 삭제
        shadow?.let { removeFromDecorView(view = it) }
        zoomableView?.let { removeFromDecorView(view = it) }
        target.visibility = View.VISIBLE // target view 활성화
        zoomableView = null
        currentMovementMidPoint = PointF()
        initialPinchMidPoint = PointF()
        animationZoomEnding = false
        state = State.IDLE

        zoomListener?.onEndZoom(target)
        if (zoomConfig.isImmersiveModeEnable) showSystemUI()
    }

    // gesture listener
    private val gestureListener: GestureDetector.SimpleOnGestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            touchListener?.onTouch(v = target)
            return super.onSingleTapUp(e)
        }

        override fun onLongPress(p0: MotionEvent?) {
            touchListener?.onLongTouch(v = target)
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            touchListener?.onDoubleTouch(v = target)
            return super.onDoubleTap(e)
        }
    }

    private val scaleGestureDetector: ScaleGestureDetector = ScaleGestureDetector(target.context, this)
    private val gestureDetector: GestureDetector = GestureDetector(target.context, gestureListener)

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        // zoom ending 애니메이션 중 or motion event pointer 수가 2개 이상일 경우
        if (animationZoomEnding || motionEvent.pointerCount > 2) return true

        scaleGestureDetector.onTouchEvent(motionEvent)
        gestureDetector.onTouchEvent(motionEvent)

        // motionEvent.action & MotionEvent.ACTION_MASK 연산을 통해 down or up 시 pointer 의 index 계산
        when (motionEvent.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_POINTER_DOWN,
            MotionEvent.ACTION_DOWN -> { // 한개 또는 두개 포인트에 대한 down
                when (state) {
                    State.IDLE -> {  // action 상태가 idle 일 경우 pointer down 으로 변경
                        state = State.POINTER_DOWN
                    }
                    State.POINTER_DOWN -> { // action 상태가 pointer down 일 경우 zoom 시작
                        state = State.ZOOMING
                        MotionUtils.midPointOfEvent(point = initialPinchMidPoint, event = motionEvent)
                        startZoomView()
                    }
                    State.ZOOMING -> {
                    }
                }
            }

            MotionEvent.ACTION_MOVE -> { // pointer move
                if (state == State.ZOOMING) { // action 상태가 zoom 일 경우 zoomable view 위치 이동
                    MotionUtils.midPointOfEvent(point = currentMovementMidPoint, event = motionEvent)

                    // TODO: coordinate 계산 알고리즘 수정 필요
                    // zoomable view 위치 조정
                    currentMovementMidPoint.x -= initialPinchMidPoint.x
                    currentMovementMidPoint.y -= initialPinchMidPoint.y

                    currentMovementMidPoint.x += targetViewCoordinate.x.toFloat()
                    currentMovementMidPoint.y += targetViewCoordinate.y.toFloat()

                    zoomableView?.x = currentMovementMidPoint.x
                    zoomableView?.y = currentMovementMidPoint.y
                }
            }

            MotionEvent.ACTION_POINTER_UP,
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> { // 한개 또는 두개 포인트에 대한 up 또는 액션 cancel
                when (state) {
                    State.IDLE -> {
                    }
                    State.POINTER_DOWN -> {
                        state = State.IDLE
                    }
                    State.ZOOMING -> {
                        endZoomView()
                    }
                }
            }
        }

        view.performClick()
        return true
    }

    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
        return zoomableView != null
    }

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        return zoomableView?.let {
            scaleFactor *= detector.scaleFactor

            // 최대 줌 설정
            scaleFactor = max(zoomConfig.minScaleFactor, min(scaleFactor, zoomConfig.maxScaleFactor))

            zoomableView?.scaleX = scaleFactor
            zoomableView?.scaleY = scaleFactor
            if (zoomConfig.isBackgroundDim)
                setShadowDimFactor(factor = scaleFactor)
            true
        } ?: let { false }
    }

    override fun onScaleEnd(detector: ScaleGestureDetector) {
        scaleFactor = 1f
    }

    /**
     * target view zoom 시작
     */
    private fun startZoomView() {
        // zoomable view 생성
        zoomableView = CopyView(target.context)
        zoomableView?.layoutParams = ViewGroup.LayoutParams(target.width, target.height)
        zoomableView?.setSource(source = target)

        // zoomable view 위치 target 기반으로 설정
        targetViewCoordinate = ViewUtils.getViewAbsoluteCoordinate(view = target)
        zoomableView?.x = targetViewCoordinate.x.toFloat()
        zoomableView?.y = targetViewCoordinate.y.toFloat()

        if (zoomConfig.isBackgroundDim) {
            // zoom 할 때 background dim view 생성
            shadow = shadow ?: View(target.context)
            shadow?.setBackgroundResource(0)

            shadow?.let { addToDecorView(view = it) }
        }
        zoomableView?.let { addToDecorView(view = it) }

        // target parent view 터치 비활성화
        disablePrentTouch(view = target.parent)
        // target view 비활성화
        target.visibility = View.INVISIBLE

        if (zoomConfig.isImmersiveModeEnable) hideSystemUI()
        zoomListener?.onStartZoom(view = target)
    }

    /**
     * view zoom 종료
     */
    private fun endZoomView() {
        // zoom end animation 활성화일 경우 animation(target view 로 돌아가는) 동작
        if (zoomConfig.isZoomAnimationEnable) {
            animationZoomEnding = true
            zoomableView?.animate()
                    ?.x(targetViewCoordinate.x.toFloat())
                    ?.y(targetViewCoordinate.y.toFloat())
                    ?.scaleX(1f)
                    ?.scaleY(1f)
                    ?.setInterpolator(zoomInterpolator)
                    ?.withEndAction(endZoomAction)
        } else endZoomAction.run()
    }

    /**
     * decor view 추가
     */
    private fun addToDecorView(view: View) {
        targetContainer.getDecorView()?.addView(view)
    }

    /**
     * decor view 삭제
     */
    private fun removeFromDecorView(view: View) {
        targetContainer.getDecorView()?.removeView(view)
    }

    /**
     * shadow dim 값 설정
     */
    private fun setShadowDimFactor(factor: Float) {
        var normalizedValue = (factor - zoomConfig.minScaleFactor) / (zoomConfig.maxScaleFactor - zoomConfig.minScaleFactor)
        normalizedValue = minOf(0.75f, normalizedValue * 2)
        val obscure = Color.argb((normalizedValue * 255).toInt(), 0, 0, 0)
        shadow?.setBackgroundColor(obscure)
    }

    /**
     * parent view touch 비활성화
     */
    private fun disablePrentTouch(view: ViewParent) {
        view.requestDisallowInterceptTouchEvent(true)
        // parent view 존재할 경우 view 의 parent view 비활성화
        if (view.parent != null) disablePrentTouch(view.parent)
    }

    /**
     * system ui 활성화
     */
    private fun showSystemUI() {
        targetContainer.getDecorView()?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }

    /**
     * system ui 비활성화
     */
    private fun hideSystemUI() {
        targetContainer.getDecorView()?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
    }
}