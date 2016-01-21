package pl.elpassion.window.readfromgps

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

/**
 * Created by dmalantowicz on 21.01.2016.
 */
class MapViewActivity : AppCompatActivity(){

    companion object {
        private val latitudeKey = "latitudeKey"
        private val longitudeKey = "longitudeKey"
        private val messageText = "This is what you are going to see, if you will give this app permission to access GPS"
        private val latitudeWarsaw: Double = 52.232311
        private val longitudeWarsaw: Double = 20.984209

        fun start(context : Context, latitude :Double, longitude : Double) {
            val intent = Intent(context, MapViewActivity::class.java)
            intent.putExtra(latitudeKey, latitude)
            intent.putExtra(longitudeKey, longitude)
            context.startActivity(intent)
        }

        fun start(activity: Activity) {
            val intent = Intent(activity, MapViewActivity::class.java)
            activity.startActivityForResult(intent, 1)
        }
    }

    val image : ImageView by lazy { findViewById(R.id.imageView) as ImageView }
    val message : TextView by lazy { findViewById(R.id.message) as TextView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_layout)
        val latitude = intent.getDoubleExtra(latitudeKey, latitudeWarsaw )
        val longitude = intent.getDoubleExtra(longitudeKey, longitudeWarsaw )
        Glide.with(image.context)
                .load("https://maps.googleapis.com/maps/api/staticmap?center=$latitude,$longitude&zoom=10&size=350x550&key=AIzaSyC8Cl3TYbzkZ6bb8_fwKeMhFvx_Be6B0CY")
                .into(image)
        if (isMessageToUserRequired()){
            message.text = messageText
        }
    }

    private fun isMessageToUserRequired() = !intent.hasExtra(latitudeKey)
}
