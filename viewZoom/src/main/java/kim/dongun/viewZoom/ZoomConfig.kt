package kim.dongun.viewZoom

/**
 * 뷰 줌 설정
 */
class ZoomConfig {
    var isBackgroundDim = false // zoom 할때 배경 dim 처리 여부
        private set
    var isZoomAnimationEnable = false // zoom end animation 여부
        private set
    var isImmersiveModeEnable = false // system ui 덮어 씌우기 여부
        private set

    val minScaleFactor = 1f // min 줌 값
    var maxScaleFactor = 5f // max 줌 값
        private set

    /**
     * isBackgroundDim setter
     *
     * @param isBackgroundDim
     */
    fun setIsBackgroundDim(isBackgroundDim: Boolean): ZoomConfig {
        this.isBackgroundDim = isBackgroundDim

        return this
    }

    /**
     * isZoomAnimationEnable setter
     *
     * @param isZoomAnimationEnable
     */
    fun setIsZoomAnimationEnable(isZoomAnimationEnable: Boolean): ZoomConfig {
        this.isZoomAnimationEnable = isZoomAnimationEnable

        return this
    }

    /**
     * isImmersiveModeEnable setter
     *
     * @param isImmersiveModeEnable
     */
    fun setIsImmersiveModeEnable(isImmersiveModeEnable: Boolean): ZoomConfig {
        this.isImmersiveModeEnable = isImmersiveModeEnable

        return this
    }

    /**
     * maxScaleFactor setter
     *
     * @param maxScaleFactor
     */
    fun setMaxScaleFactor(maxScaleFactor: Float): ZoomConfig {
        this.maxScaleFactor =
                if (maxScaleFactor > minScaleFactor) maxScaleFactor
                else this.maxScaleFactor

        return this
    }
}