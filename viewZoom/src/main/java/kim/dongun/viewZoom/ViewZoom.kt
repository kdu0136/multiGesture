package kim.dongun.viewZoom

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import androidx.fragment.app.DialogFragment

/**
 * 뷰 줌
 */
object ViewZoom {
    /**
     * 뷰 줌 Builder 클래스
     *
     * @param context
     * @param target
     */
    class Builder(private val context: Context,
                  private val target: View) {
        private var disposed = false
        private val targetContainer: TargetContainer? = when (context) {
            is Activity -> ActivityContainer(activity = context)
            is Dialog -> DialogContainer(dialog = context)
            is DialogFragment -> DialogFragmentContainer(dialogFragment = context)
            else -> null
        }

        private var zoomConfig: ZoomConfig = ZoomConfig()
        private var zoomInterpolator: Interpolator = AccelerateDecelerateInterpolator()
        private var touchListener: TouchListener? = null
        private var zoomListener: ZoomListener? = null


        /**
         * 뷰 줌 config 설정
         *
         * @param zoomConfig
         */
        fun zoomConfig(zoomConfig: ZoomConfig): Builder {
            checkNotDisposed()
            this.zoomConfig = zoomConfig

            return this
        }

        /**
         * 뷰 줌 interpolator 설정
         *
         * @param interpolator
         */
        fun interpolator(interpolator: Interpolator): Builder {
            checkNotDisposed()
            this.zoomInterpolator = interpolator

            return this
        }

        /**
         * 뷰 줌 touchListener 설정
         *
         * @param touchListener
         */
        fun touchListener(touchListener: TouchListener): Builder {
            checkNotDisposed()
            this.touchListener = touchListener

            return this
        }

        /**
         * 뷰 줌 zoomListener 설정
         *
         * @param zoomListener
         */
        fun zoomListener(zoomListener: ZoomListener): Builder {
            checkNotDisposed()
            this.zoomListener = zoomListener

            return this
        }

        /**
         * 뷰 줌 등록
         */
        fun register() {
            checkNotDisposed()
            disposed = targetContainer?.let {
                target.setOnTouchListener(ZoomableTouchListener(
                        targetContainer = targetContainer,
                        target = target,
                        zoomConfig = zoomConfig,
                        zoomInterpolator = zoomInterpolator,
                        touchListener = touchListener,
                        zoomListener = zoomListener
                ))
                true
            } ?: false
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