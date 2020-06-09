package kim.dongun.multiGesture

/**
 * MultiGesture Config class
 */
class MultiGestureConfig {
    val minScaleFactor = 1f // minimum scale factor
    var maxScaleFactor = 2f // maximum scale factor
        private set

    var isRotateEnabled: Boolean = true
        private set

    var isScaleEnabled: Boolean = true
        private set

    var isSimpleGestureEnabled: Boolean = true
        private set

    /**
     * maxScaleFactor setter (default 2)
     *
     * @param maxScaleFactor
     */
    fun setMaxScaleFactor(maxScaleFactor: Float): MultiGestureConfig {
        this.maxScaleFactor =
                if (maxScaleFactor > minScaleFactor) maxScaleFactor
                else this.maxScaleFactor

        return this
    }

    /**
     * isRotateEnabled setter (default true)
     *
     * @param isRotateEnabled
     */
    fun setRotateEnable(isRotateEnabled: Boolean): MultiGestureConfig {
        this.isRotateEnabled = isRotateEnabled
        return this
    }

    /**
     * isScaleEnabled setter (default true)
     *
     * @param isScaleEnabled
     */
    fun setScaleEnabled(isScaleEnabled: Boolean): MultiGestureConfig {
        this.isScaleEnabled = isScaleEnabled

        return this
    }

    /**
     * isSimpleGestureEnabled setter (default true)
     *
     * @param isSimpleGestureEnabled
     */
    fun setSimpleGestureEnabled(isSimpleGestureEnabled: Boolean): MultiGestureConfig {
        this.isSimpleGestureEnabled = isSimpleGestureEnabled

        return this
    }
}