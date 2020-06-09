package kim.dongun.multiGestureApplication

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import kim.dongun.multiGesture.MultiGestureConfig
import kim.dongun.multiGesture.TouchListener
import kim.dongun.multiGesture.ViewMultiGesture

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainView = findViewById<CardView>(R.id.target)

        val gestureConfig = MultiGestureConfig()

        ViewMultiGesture.Builder(target = mainView)
            .gestureConfig(gestureConfig = gestureConfig)
            .touchListener(touchListener = object: TouchListener {
                override fun onDoubleTouch(view: View) {
                    TODO("Not yet implemented")
                }

                override fun onLongTouch(view: View) {
                    TODO("Not yet implemented")
                }

                override fun onSingleTouch(view: View) {
                    Log.d("mainView", "coordinate: (${view.x}, ${view.y})")
                    Log.d("mainView", "scaleX: ${view.scaleX} scaleY: ${view.scaleY}")
                    Log.d("mainView", "rotation: ${view.rotation}")
                }
            })
            .register()
    }
}