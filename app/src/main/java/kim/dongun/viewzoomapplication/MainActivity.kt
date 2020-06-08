package kim.dongun.viewzoomapplication

import android.os.Bundle
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import kim.dongun.viewZoom.ViewZoom

class MainActivity : AppCompatActivity() {
    private val glideRequestManager: RequestManager by lazy { Glide.with(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView = findViewById<ImageView>(R.id.imageView)

        glideRequestManager
                .load(R.drawable.sample)
                .apply(RequestOptions().fitCenter())
                .into(imageView)

        ViewZoom.Builder(context = this, target = imageView)
                .interpolator(interpolator = OvershootInterpolator())
                .register()
    }
}