package kim.dongun.viewZoom

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.view.View

/**
 * View Util 기능 object
 */
object ViewUtils {
    /**
     * view 에서 bitmap 추출
     *
     * @param view bitmap 추출할 view
     */
    fun getBitmapFromView(view: View): Bitmap {
        //Define a bitmap with the same size as the view
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        //Bind a canvas to it
        val canvas = Canvas(bitmap)
        // draw the view on the canvas
        view.draw(canvas)

        return bitmap
    }

    /**
     * view 절대 좌표 추출
     *
     * @param view 절대 좌표 추출할 view
     */
    fun getViewAbsoluteCoordinate(view: View): Point {
        val location = IntArray(2)
        view.getLocationInWindow(location)

        return Point(location[0], location[1])
    }
}