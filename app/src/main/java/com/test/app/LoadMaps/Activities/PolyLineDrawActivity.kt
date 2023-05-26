package com.test.app.LoadMaps.Activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CustomCap
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.test.app.LoadMaps.Activities.PolyLineDrawActivity.staticvariables.wayPoints
import com.test.app.LoadMaps.DataSets.LocationData
import com.test.app.LoadMaps.Fragments.MapLoad
import com.test.app.LoadMaps.Fragments.TestFragment
import com.test.app.LoadMaps.Fragments.TestFragment1
import com.test.app.LoadMaps.PolyLineAnimation.AnimatedPolyline
import com.test.app.R
import java.security.AccessController.getContext


class PolyLineDrawActivity() : AppCompatActivity(), OnMapReadyCallback {
    var context: Context? = null

    private lateinit var mMap: GoogleMap
    private lateinit var animatedPolyline: AnimatedPolyline

    private lateinit var mLocationList:ArrayList<LocationData>

//    private val wayPoints = mutableListOf(
//        LatLng(11.02996943896511, 76.90122299759666),
//        LatLng(11.025791833574297, 76.90784931389028),
//        LatLng(11.016811744617684, 76.92265572174412),
//        LatLng(11.012973702557066, 76.93844641031589),
//        LatLng(11.00736416648051, 76.94521384850022),
//
//    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.polyline_map_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(false)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        //animationLoader = findViewById(R.id.animation_loader)
        context = this

        wayPoints =  intent.getParcelableArrayListExtra<LatLng>("wayPoints") as ArrayList<LatLng>

//        for (i in 0 until wayPoints.size){
//            Log.e("Test","wayPoints List = ${wayPoints.size} latitude = ${wayPoints.get(i).latitude}" +
//                    " longitude = ${wayPoints.get(i).longitude}");
//        }

        //Test
//        wayPoints.add(LatLng(11.02996943896511, 76.90122299759666))
//        wayPoints.add(LatLng(11.025791833574297, 76.90784931389028))
//        wayPoints.add(LatLng(11.016811744617684, 76.92265572174412))
//        wayPoints.add(LatLng(11.012973702557066, 76.93844641031589))
//        wayPoints.add(LatLng(11.003851687308376, 76.96928319705995))

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        findViewById<Button>(R.id.clear).setOnClickListener {
            clearAnimation()
        }
        findViewById<Button>(R.id.start).setOnClickListener {
            animatedPolyline.start()
        }


    }

    override fun onResume() {
        super.onResume()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //menuInflater.inflate(R.menu.cancel_order, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
//        if (id == R.id.m_cancel_order) {
//            Log.e("Test","onOptionsItemSelected - Cancel Order Clicked");
//            return true
//        }
        if (id == android.R.id.home) {
            Log.e("Test","onOptionsItemSelected - home Clicked");
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        Log.e("Test","onBackPressed - Called()");
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        addOriginAndDestination()
        zoomToOrigin()

        animatedPolyline = AnimatedPolyline(
            mMap,
            wayPoints,
            polylineOptions = getPolylineOptions(),
            duration = 5000,
            interpolator = DecelerateInterpolator(),
            animatorListenerAdapter = getListener()
        )
        animatedPolyline.startWithDelay(1000)
    }

    private fun clearAnimation() {
        animatedPolyline.remove()
    }

    private fun getListener(): AnimatorListenerAdapter {
        return object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                //animatedPolyline.start()
            }
        }
    }

    private fun getPolylineOptions(): PolylineOptions {
        return PolylineOptions()
            .color(ContextCompat.getColor(this, R.color.purple_700))
//            .add(LatLng(-35.016, 143.321),
//                LatLng(-34.747, 145.592),
//                LatLng(-34.364, 147.891),
//                LatLng(-33.501, 150.217),
//                LatLng(-32.306, 149.248),
//                LatLng(-32.491, 147.309))
//            .pattern(
//                listOf(
//                    Dot(), Gap(20F), Dash(30F), Gap(20F)
//                )
//            )
            .width(10F)
            .startCap(RoundCap())
            .endCap(RoundCap())
            .jointType(JointType.ROUND)
            //.zIndex(1000.0F)
    }

    private fun addOriginAndDestination() {
        //mMap.addMarker(MarkerOptions().position(wayPoints.first()).icon(BitmapFromVector(context!!,R.drawable.baseline_circle_13)))
        mMap.addMarker(MarkerOptions().position(wayPoints.first()).icon(BitmapFromVector(context!!,R.drawable.baseline_location_on_48)))

        //mMap.addMarker(MarkerOptions().position(wayPoints.last()).icon(BitmapFromVector(context!!,R.drawable.baseline_circle_13)))
        mMap.addMarker(MarkerOptions().position(wayPoints.last()).icon(BitmapFromVector(context!!,R.drawable.baseline_location_on_red_48)))

    }

    private fun zoomToOrigin() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(wayPoints.first(), 13f))
    }

    private fun BitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        // below line is use to generate a drawable.
        val vectorDrawable = ContextCompat.getDrawable(
            context, vectorResId
        )

        // below line is use to set bounds to our vector
        // drawable.
        vectorDrawable!!.setBounds(
            0, 0, vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )

        // below line is use to create a bitmap for our
        // drawable which we have added.
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        // below line is use to add bitmap in our canvas.
        val canvas = Canvas(bitmap)

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas)

        // after generating our bitmap we are returning our
        // bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    object staticvariables{
        public var wayPoints = ArrayList<LatLng>()
    }

}