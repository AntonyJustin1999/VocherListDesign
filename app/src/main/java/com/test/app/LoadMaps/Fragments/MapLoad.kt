package com.test.app.LoadMaps.Fragments

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.test.app.LoadMaps.Activities.HomeActivity
import com.test.app.LoadMaps.ApiCall
import com.test.app.LoadMaps.DataSets.Data
import com.test.app.LoadMaps.DataSets.LocationData
import com.test.app.LoadMaps.DataSets.UsersData
import com.test.app.LoadMaps.Fragments.LocationList.functions.animationLoader
import com.test.app.LoadMaps.Fragments.MapLoad.functions.currentLatitude
import com.test.app.LoadMaps.Fragments.MapLoad.functions.currentLongitude
import com.test.app.LoadMaps.Fragments.MapLoad.functions.placename
import com.test.app.LoadMaps.Utils.ReverseGeoCodeApi
import com.test.app.R
import io.realm.Realm
import io.realm.RealmChangeListener
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapLoad(context:Context, activity: Activity):Fragment(), OnMapReadyCallback {

    private var mView: View? = null

    var animationLoader:LottieAnimationView? = null

    private var mMap: GoogleMap? = null

    private val mBearing = 0.0
    private val mMapCenter: LatLng? = null
    private val mMarker: Marker? = null
    var mAlertDialog: AlertDialog.Builder? = null
    var dialogInterface: DialogInterface? = null
    var mcreateDialog: AlertDialog? = null

    var cu: CameraUpdate? = null
    var bounds: LatLngBounds? = null

    var mscrollView: ScrollView? = null
    var done = false


    var mContext:Context? = null
    var mActivity:Activity? = null

    init {
        mContext = context
        mActivity = activity
    }

    private var realm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("Test","onCreate Called()");
        if (arguments != null) {
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater?.inflate(R.layout.map_load, container, false)

        realm = Realm.getDefaultInstance();
        animationLoader = mView?.findViewById(R.id.animation_loader)

        Log.e("Test","onCreateView Called()");

        mscrollView =mView?.findViewById<ScrollView>(R.id.scrollView)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this);
        mapFragment?.getMapAsync { googleMap ->

            //Test
            Log.e("Test","mapFragment?.getMapAsync Called Inside")

            mMap = googleMap

            //LoadMap()

            mMap?.setMapType(GoogleMap.MAP_TYPE_NORMAL)

//            mMap?.addMarker(
//                MarkerOptions()
//                    .title("Vadavalli")
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//
//            )


        }

        val locationDetails = realm?.where(LocationData::class.java)?.equalTo("UserName", getCurrentUserData()?.userName)?.findFirst()
        currentLatitude = locationDetails?.Latitude?.toDouble()
        currentLongitude = locationDetails?.Longitude?.toDouble()

        Log.e("Test","MapLoad OnCreate - currentLatitude = $currentLatitude  currentLongitude = $currentLongitude")

        return mView
    }

    override fun onStart() {
        super.onStart()
        Log.e("Test","onStart Called()");

        LoadMapwithLatLng()

    }

    override fun onResume() {
        super.onResume()
        Log.e("Test","onResume Called()");
    }

    override fun onStop() {
        super.onStop()
    }
    override fun onMapReady(googleMap: GoogleMap) {

        //Test
        Log.e("Test","onMapReady Called() OutSide")

        mMap = googleMap;

    }
    fun LoadMapwithLatLng(){

        mMap?.clear()

        currentLatitude =  currentLatitude?:0.0
        currentLongitude = currentLongitude?:0.0

        Log.e("Test","MapLoad LoadMapwithLatLng - currentLatitude = $currentLatitude  currentLongitude = $currentLongitude")

        mMap?.addMarker(
            MarkerOptions()
                .position(LatLng(currentLatitude!!, currentLongitude!!))
                // custom marker on our map.
                .icon(
                    BitmapFromVector(mContext!!,R.drawable.baseline_circle_13)
                )
        )

        mMap?.addMarker(
            MarkerOptions()
                .position(LatLng(currentLatitude!!, currentLongitude!!))
                // custom marker on our map.
                .icon(
                    BitmapFromVector(mContext!!,R.drawable.baseline_location_on_48)
                )
        )

        mMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    currentLatitude!!,
                    currentLongitude!!
                ), 15f
            )
        )
    }

    fun LoadMap() {
        val pickup = LatLng(
            java.lang.Double.valueOf(11.0268),
            java.lang.Double.valueOf(76.9058)
        )
//        val delivery = LatLng(
//            java.lang.Double.valueOf(Delivery_To.get(0)),
//            java.lang.Double.valueOf(Delivery_To.get(1))
//        )
        //  mMap.addMarker(new MarkerOptions().position(pickup).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        //  mMap.addMarker(new MarkerOptions().position(pickup).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        val markerPickup = MarkerOptions()
        markerPickup.position(pickup)

        val height = 110
        val width = 110
        val b = bitmapFromDrawableRes(mContext!!,R.drawable.baseline_location_on_48)
        //val b = bitmapdraw.bitmap
        val smallMarker = Bitmap.createScaledBitmap(b!!, width, height, false)
        markerPickup.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mMap!!.addMarker(markerPickup)

        //val bitmapdraw1 = resources.getDrawable(R.drawable.red_location) as BitmapDrawable
        //val b1 = bitmapdraw1.bitmap
        //val smallMarker1 = Bitmap.createScaledBitmap(b1, width, height, false)

//        mMap!!.addMarker(
//            MarkerOptions().icon(
//                BitmapDescriptorFactory.defaultMarker(
//                    BitmapDescriptorFactory.HUE_RED
//                )
//            ).position(delivery)
//        )

        val builder = LatLngBounds.Builder()
        builder.include(pickup)
        //builder.include(delivery)
        bounds = builder.build()
        val padding = 50 // offset from edges of the map in pixels

        cu = CameraUpdateFactory.newLatLngBounds(bounds!!, padding)
    }

    private fun bitmapFromDrawableRes(context: Context, resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
// copying drawable object to not manipulate on the same reference
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
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

    fun getCurrentUserData(): UsersData? {
        val loginDetails: UsersData? = realm?.where(UsersData::class.java)?.equalTo("isCurrentUser", true)?.findFirst()
        return loginDetails
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.e("Test","MapLoadFunctions Called() currentLatitude = $currentLatitude" +
                "currentLongitude = $currentLongitude  placename = $placename ")

        LoadMapwithLatLng()


    }

    object functions {

        public fun mapLoad(){
            Log.e("Test","MapLoad - inside functions Called()")
        }
        public var currentLatitude:Double? = 0.0
        public var currentLongitude:Double? = 0.0
        public var placename:String? = ""
        public var animationLoader: LottieAnimationView? = null
    }

}