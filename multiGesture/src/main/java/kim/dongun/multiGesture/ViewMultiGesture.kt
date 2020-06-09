package kim.dongun.multiGesture

import android.view.View

/**
 * ViewMultiGesture
 */
object ViewMultiGesture {
    /**
     * View Multi Gesture Builder 클래스
     *
     * @param target
     */
    class Builder(private val target: View) {
        private var disposed = false
        private var gestureConfig: MultiGestureConfig =
            MultiGestureConfig()
        private var touchListener: TouchListener? = null

        /**
         * register multi gesture config
         *
         * @param gestureConfig
         */
        fun gestureConfig(gestureConfig: MultiGestureConfig): Builder {
            checkNotDisposed()
            this.gestureConfig = gestureConfig

            return this
        }

        /**
         * register touch listener
         */
        fun touchListener(touchListener: TouchListener): Builder {
            checkNotDisposed()
            this.touchListener = touchListener

            return this
        }

        /**
         * register multi gesture view
         */
        fun register() {
            checkNotDisposed()
            target.setOnTouchListener(
                MultiGestureListener(
                    target = target,
                    gestureConfig = gestureConfig,
                    touchListener = touchListener
                )
            )
            disposed = true
        }

        /**
         * 뷰 줌 등록 여부 판단
         * 이미 등록 된 이미지 target 일 경우 exception
         */
        private fun checkNotDisposed() {
            throw if (disposed) IllegalStateException("Builder already disposed")
            else return
        }
    }
}