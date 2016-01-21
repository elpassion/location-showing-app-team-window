package pl.elpassion.window.readfromgps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity(), LocationListener {

    companion object {
        private val readLocationFromGPSCode = 1
    }

    val button: Button by lazy { findViewById(R.id.show_location_button) as Button }
    var longitude: Double = 0.0
    var latitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            showLocationViewIfPermissionGranted()
        }
    }

    private fun showLocationViewIfPermissionGranted() {
        if (isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            startLocationListener()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                MapViewActivity.start(this)
            } else {
                requestPermission(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
            }
        }
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, permission)
    }

    private fun requestPermission(permissions: Array<String>) {
        ActivityCompat.requestPermissions(this, permissions, readLocationFromGPSCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray) {
        if (readLocationFromGPSCode == requestCode) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationListener()
            }
        }
    }

    private fun startLocationListener() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0F, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        requestPermission(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
    }

    override fun onLocationChanged(location: Location) {
        latitude = location.latitude
        longitude = location.longitude
        MapViewActivity.start(this, latitude, longitude)
    }

    override fun onProviderDisabled(provider: String?) {
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onPause() {
        super.onPause()
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.removeUpdates(this)
    }

}
