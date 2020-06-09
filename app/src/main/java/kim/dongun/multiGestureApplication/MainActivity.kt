package kim.dongun.multiGestureApplication

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import kim.dongun.multiGesture.MultiGestureConfig
import kim.dongun.multiGesture.TouchListener
import kim.dongun.multiGesture.ViewMultiGesture

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val target = findViewById<CardView>(R.id.target)

        val gestureConfig = MultiGestureConfig()

        ViewMultiGesture.Builder(target = target)
            .gestureConfig(gestureConfig = gestureConfig)
            .touchListener(touchListener = object: TouchListener {
                override fun onDoubleTouch(view: View) {
                    TODO("Not yet implemented")
                }

                override fun onLongTouch(view: View) {
                    TODO("Not yet implemented")
                }

                override fun onSingleTouch(view: View) {
                }
            })
            .register()
    }
}