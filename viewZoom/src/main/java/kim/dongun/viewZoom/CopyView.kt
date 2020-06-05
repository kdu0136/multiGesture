package kim.dongun.viewZoom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

/**
 * 뷰 복사 클래스
 */
class CopyView : View {
    // 복사한 뷰의 source
    private var source: View? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

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
        this.source?.draw(canvas)
    }
}