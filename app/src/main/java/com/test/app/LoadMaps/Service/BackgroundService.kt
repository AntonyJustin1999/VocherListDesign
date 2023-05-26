package com.test.app.LoadMaps.Service

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.test.app.LoadMaps.Utils.UtilClass
import java.util.Timer
import java.util.TimerTask


class BackgroundService : Service(), LocationListener {

    var isGPSEnable = false
    var isNetworkEnable = false
    var latitude = 0.0
    var longitude:kotlin.Double = 0.0
    var locationManager: LocationManager? = null
    var location: Location? = null
    var mHandler: Handler = Handler(Looper.getMainLooper())
    var mTimer: Timer? = null
    //var notify_interval: Long = 60000 * 15 //15 minutes
    var notify_interval: Long = 60000 * 4
   companion object{
        var str_receiver = "servicetutorial.service.receiver"
        var context: Context? = null
        var intent: Intent? = null
    }

    override fun onCreate() {
        super.onCreate()

        //Test
        Log.e("Test","onCreate Called()")

        context = this

        mTimer = Timer()
        mTimer!!.schedule(TimerTaskToGetLocation(), 1, notify_interval)
        intent = Intent(str_receiver)
        //Test
        Log.e("Test","new Intent Called()")


        //Test
        //Log.e("Test","Google Service - onCreate Called()");

//        fn_getlocation();
    }

    override fun onBind(p0: Intent?): IBinder? {
        //Test
        //Log.e("Test","onBind Called()")
        return null
    }

    override fun onLocationChanged(p0: Location) {
        //Test
        //Log.e("Test","onLocationChanged Called() Latitude = ${p0.latitude} Longitude = ${p0.longitude}")
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        //Test
        Log.e("Test","onStatusChanged Called()")
    }

    private fun fn_getlocation(context: Context,intent:Intent) {

        //Test
        Log.e("Test","Google Service - fn_getlocation Called() ${context}");
        locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
        isGPSEnable = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        isNetworkEnable = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (!isGPSEnable && !isNetworkEnable) {
            //Test
            Log.e("Test","Google Service - !isGPSENable and network enable false");
        } else {
            if (isNetworkEnable) {
                location = null
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                locationManager!!.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0,
                    0f,
                    this
                )
                if (locationManager != null) {
                    location =
                        locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    if (location != null) {
                        latitude = location!!.latitude
                        longitude = location!!.longitude
                        fn_update(location!!,intent)
                    }
                }
            } else if (isGPSEnable) {
                location = null
                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
                if (locationManager != null) {
                    location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (location != null) {
                        latitude = location!!.latitude
                        longitude = location!!.longitude
                        fn_update(location!!,intent)
                    }
                }
            }
        }
    }

    private class TimerTaskToGetLocation : TimerTask() {
        override fun run() {
            //Test
            Log.e("Test","TimerTaskToGetLocation - run - Called()")
            var task:BackgroundService = BackgroundService()
            task.mHandler.post(Runnable { task.fn_getlocation(context!!, intent!!) })
        }
    }

    private fun fn_update(location: Location,intent:Intent) {
        Log.e("Test", "GoogleService fn_update = $latitude $longitude")
        intent!!.putExtra("latitude", location.latitude.toString() + "")
        intent!!.putExtra("longitude", location.longitude.toString() + "")
        context?.sendBroadcast(intent)
    }


}