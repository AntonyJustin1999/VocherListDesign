package com.test.app.LoadMaps.Activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.test.app.LoadMaps.Activities.HomeActivity.functions.animationLoader
import com.test.app.LoadMaps.Activities.HomeActivity.functions.mViewPager
import com.test.app.LoadMaps.ApiCall
import com.test.app.LoadMaps.DataSets.LocationData
import com.test.app.LoadMaps.DataSets.UsersData
import com.test.app.LoadMaps.Fragments.LocationList
import com.test.app.LoadMaps.Fragments.MapLoad
import com.test.app.LoadMaps.Service.BackgroundService
import com.test.app.LoadMaps.Utils.ReverseGeoCodeApi
import com.test.app.R
import io.realm.Realm
import io.realm.RealmChangeListener
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.Locale


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    public var context: Context? = null
    public var activity: Activity? = null

    var navigationView: NavigationView? = null
    var headerView: View? = null

    var tv_name :TextView? = null
    var tv_email_id :TextView? = null
    var tv_mobile_no :TextView? = null

    var play_btn_holder: FrameLayout? = null

    var mTabLayoutTitles: TabLayout? = null

    private val USER_LOCATION_PERMISSION_CODE = 41

    private var realm: Realm? = null

    private lateinit var realmListener: RealmChangeListener<Realm>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        realm = Realm.getDefaultInstance();

        animationLoader = findViewById(R.id.animation_loader)
        context = this

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        play_btn_holder = toolbar.findViewById(R.id.play_btn_holder)

        play_btn_holder?.setOnClickListener {

            var wayPoints = ArrayList<LatLng>()

            val dataModals = realm?.where(LocationData::class.java)?.equalTo("UserName", getCurrentUserData()?.userName)?.findAll()
            Log.e("Test","ReadAllData - Size = ${dataModals?.size}")
            for(i in 0 until dataModals?.size!!){
                //locationList.add(dataModals.get(i)!!)
                wayPoints.add(LatLng(dataModals.get(i)?.Latitude?.toDouble()!!,dataModals.get(i)?.Longitude?.toDouble()!!))
            }

            if(wayPoints.size>0){
                val intent = Intent(this@HomeActivity, PolyLineDrawActivity::class.java)
                intent.putParcelableArrayListExtra("wayPoints", wayPoints);
                //intent.putExtra("type", "Setting")
                startActivity(intent)
            } else {
                showAlertDialog("","Please Wait...")
            }
        }

        realmListener = RealmChangeListener {
            Log.e("Test","HomeActivity realmListener Called()");
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        navigationView = findViewById(R.id.nav_view)
        navigationView?.setNavigationItemSelectedListener(this)
        headerView = navigationView?.getHeaderView(0)

        tv_name = headerView?.findViewById(R.id.tv_name)
        tv_email_id = headerView?.findViewById(R.id.tv_email_id)
        tv_mobile_no = headerView?.findViewById(R.id.tv_mobile)

        var userDetails = getCurrentUserData();
        tv_name?.setText(userDetails?.userName)
        tv_email_id?.setText(userDetails?.emailId)
        tv_mobile_no?.setText(userDetails?.mobileNumber)

        mTabLayoutTitles = findViewById(R.id.tablay_titles)
        mViewPager = findViewById(R.id.viewpager)

        mTabLayoutTitles?.addTab(mTabLayoutTitles?.newTab()?.setText("Location History")!!)
        mTabLayoutTitles?.addTab(mTabLayoutTitles?.newTab()?.setText("Location Map")!!)

        mTabLayoutTitles?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                //Test
                Log.e("Test", "onTabSelected Called() position = " + tab.position)
                mViewPager?.setCurrentItem(tab.getPosition());
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                //Test
                Log.e("Test", "onTabUnSelected Called() ")
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                //Test
                Log.e("Test", "onTabReselected Called() ")
            }
        })

        val adapter = CustomTablayoutAdapter(
            this,this, supportFragmentManager,
            mTabLayoutTitles?.tabCount!!, true
        )
        mViewPager?.setAdapter(adapter)
        mViewPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(mTabLayoutTitles))

        var manager: LocationManager =
            getSystemService( Context.LOCATION_SERVICE ) as LocationManager;

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }

        realm?.addChangeListener(realmListener)

    }


    override fun onStart() {
        super.onStart()

        if (ActivityCompat.checkSelfPermission(this@HomeActivity, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this@HomeActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askPermission()
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this@HomeActivity,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                askBGPermission()
            } else {
                val intent = Intent(this@HomeActivity, BackgroundService::class.java)
                startService(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        registerReceiver(broadcastReceiver,  IntentFilter(BackgroundService.str_receiver));

//        if (ActivityCompat.checkSelfPermission(this@HomeActivity, Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this@HomeActivity,
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            askPermission()
//        } else {
//            if (ActivityCompat.checkSelfPermission(
//                    this@HomeActivity,
//                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                askBGPermission()
//            } else {
//                val intent = Intent(this@HomeActivity, BackgroundService::class.java)
//                startService(intent)
//            }
//        }

    }

    var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val latitude = java.lang.Double.valueOf(intent.getStringExtra("latitude"))
            val longitude = java.lang.Double.valueOf(intent.getStringExtra("longitude"))

            //Test
            Log.e("Test","broadcastReceiver Called() = $latitude $longitude ")

            //reverseGeoCode(latitude,longitude)

            var locationData = LocationData()
            locationData.Latitude = latitude.toString()
            locationData.Longitude = longitude.toString()
            locationData.Address = ReverseGeoCoding(latitude,longitude)
            locationData.UserName= getCurrentUserData()?.userName
            AddLocationData(locationData)

        }
    }

    fun showAlertDialog(title:String,msg:String){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(msg)
        //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->

        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->

        }

//        builder.setNeutralButton("Maybe") { dialog, which ->
//            Toast.makeText(context,
//                "Maybe", Toast.LENGTH_SHORT).show()
//        }
        builder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //menuInflater.inflate(R.menu.main_menu, menu)
        //update(menu)
        //getNotification(menu)
        return true
    }


//    private fun getNotification(menu: Menu?) {
//        menu?.findItem(R.id.menu_item)?.isVisible = false
//    }
//
//    private fun update(menu: Menu?) {
//        val view = menu?.findItem(R.id.with_notify)?.actionView
//        val t1 = view?.findViewById<ImageView>(R.id.cart_image_view)
//        val t2 = view?.findViewById<TextView>(R.id.cart_count_value)
//        t1?.setOnClickListener {
//            Log.e("Test","t1 Clicked")
//        }
//        t2?.setOnClickListener {
//            Log.e("Test","t2 Clicked")
//        }
//    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_home) {
            val intent = Intent(this@HomeActivity, HomeActivity::class.java)
            intent.putExtra("type", "Setting")
            startActivity(intent)
            finish()
            Log.e("Test","nav_delivery_history Clickde");
        } else if (id == R.id.nav_location_play) {
            var wayPoints = ArrayList<LatLng>()

            val dataModals = realm?.where(LocationData::class.java)?.equalTo("UserName", getCurrentUserData()?.userName)?.findAll()
            Log.e("Test","ReadAllData - Size = ${dataModals?.size}")
            for(i in 0 until dataModals?.size!!){
                //locationList.add(dataModals.get(i)!!)
                wayPoints.add(LatLng(dataModals.get(i)?.Latitude?.toDouble()!!,dataModals.get(i)?.Longitude?.toDouble()!!))
            }

            if(wayPoints.size>0){
                val intent = Intent(this@HomeActivity, PolyLineDrawActivity::class.java)
                intent.putParcelableArrayListExtra("wayPoints", wayPoints);
                //intent.putExtra("type", "Setting")
                startActivity(intent)
            } else {
                showAlertDialog("","Please Wait...")
            }
        } else if (id == R.id.nav_logout) {
            logOut()
            StopLocationUpdates()
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            intent.putExtra("type", "Setting")
            startActivity(intent)
            finish()
        }

        return true
    }

    class CustomTablayoutAdapter(private val myContext: Context,private val myActivity: Activity,
        fm: FragmentManager?, var totalTabs: Int, private val mIsHomeSearch: Boolean) :
        FragmentStatePagerAdapter(fm!!) {
        init {
            //Test
            Log.e("Test", "CustomTabLayoutAdapter constructor Called")
        }

        // this is for fragment tabs
        override fun getItem(position: Int): Fragment {
            //Test
            Log.e("Test", "CustomTabLayoutAdapter getItem Called position = $position")
            if(position == 0){
                return LocationList(myContext,myActivity)
            } else {
                return MapLoad(myContext,myActivity)
            }
        }

        // this counts total number of tabs
        override fun getCount(): Int {
            return totalTabs
        }
    }

    fun StopLocationUpdates(){
        try {
            if (broadcastReceiver != null) {
                unregisterReceiver(broadcastReceiver)
            }
        } catch (e: Exception) {
            Log.e("Test", "Exception ==> " + e.message)
        }
    }

    private fun askPermission() {
        if (ContextCompat.checkSelfPermission(
                this@HomeActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@HomeActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(this@HomeActivity)
                    .setTitle(R.string.app_name)
                    .setMessage("Please allow the device location")
                    .setPositiveButton(android.R.string.ok,
                        DialogInterface.OnClickListener { dialogInterface: DialogInterface?, i: Int ->
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(
                                this@HomeActivity,
                                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                                USER_LOCATION_PERMISSION_CODE
                            )
                        })
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this@HomeActivity, arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                    USER_LOCATION_PERMISSION_CODE
                )
            }
        }
    }

    private fun askBGPermission() {
        if (ContextCompat.checkSelfPermission(
                this@HomeActivity,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@HomeActivity,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            ) {
                AlertDialog.Builder(this@HomeActivity)
                    .setTitle(com.test.app.R.string.app_name)
                    .setMessage("Please allow the device location")
                    .setPositiveButton(android.R.string.ok,
                        DialogInterface.OnClickListener { dialogInterface: DialogInterface?, i: Int ->
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(
                                this@HomeActivity,
                                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                                42
                            )
                        })
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this@HomeActivity, arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    42
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            41 -> if (grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                askBGPermission()
            } else {
                askPermission()
            }

            42 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                askBGPermission()
            }

            100 -> {}
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton(
                "Yes"
            ) { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
        val alert = builder.create()
        alert.show()

//            .setNegativeButton(
//                "No"
//            ) { dialog, id -> dialog.cancel() }
    }

    public fun AddLocationData(locationData:LocationData){

        Log.e("Test","AddLocationData - Address ${locationData.Address}")

        var modal = LocationData()

        var id = realm!!.where(LocationData::class.java).max("id")

        val nextId: Long

        nextId = if (id == null) {
            // if id is null
            // we are passing it as 1.
            1
        } else {
            // if id is not null then
            // we are incrementing it by 1
            (id.toInt() + 1).toLong()
        }
        modal.id = nextId
        modal.Latitude = locationData.Latitude
        modal.Longitude = locationData.Longitude
        modal.Address = locationData.Address
        modal.UserName= getCurrentUserData()?.userName

        realm?.executeTransaction { realm ->
            // inside on execute method we are calling a method
            // to copy to real m database from our modal class.
            realm.copyToRealm(modal)
            //Test
            Log.e("Test","InsertData -  Insert Data Success")
        }
    }

//    public fun ReverseGeoCoding(latitude:Double,longitude:Double):String{
//
//        // Initializing Geocoder
//        val mGeocoder = Geocoder(applicationContext, Locale.getDefault())
//        var addressString= ""
//
//        // Reverse-Geocoding starts
//        try {
//            val addressList: List<Address> = mGeocoder.getFromLocation(latitude, longitude, 1)
//
//            // use your lat, long value here
//            if (addressList != null && addressList.isNotEmpty()) {
//                //if(addressList.size>0){
//                    val address = addressList[0]
//                    for (i in 0 until address.maxAddressLineIndex) {
//                        addressString = address.getAddressLine(i)
//                    }
//                //}
//            }
//            Log.e("Test","ReverseGeoCoding - addressString =  $addressString")
//            return addressString
//        } catch (e: IOException) {
//            Toast.makeText(applicationContext,"Unable connect to Geocoder",Toast.LENGTH_LONG).show()
//            return addressString
//        }
//
//    }

    public fun ReverseGeoCoding(latitude:Double,longitude:Double):String{

        // Initializing Geocoder
        val mGeocoder = Geocoder(applicationContext, Locale.getDefault())
        var addressString= ""

        // Reverse-Geocoding starts
        try {
            val addressList: List<Address> = mGeocoder.getFromLocation(latitude, longitude, 1)

            // use your lat, long value here
            if (addressList != null && addressList.isNotEmpty()) {
                val address = addressList[0]
                addressString = address.getAddressLine(0)
                Log.e("Test","Test address = "+addressString);
            }
            return addressString
        } catch (e: Exception) {
            Toast.makeText(applicationContext,"Unable connect to Geocoder",Toast.LENGTH_LONG).show()
            return ""
        }

    }

    fun getCurrentUserData(): UsersData? {
        val loginDetails: UsersData? = realm?.where(UsersData::class.java)?.equalTo("isCurrentUser", true)?.findFirst()
        return loginDetails
    }

    fun logOut(): Boolean {
        try{
            val userDetails = getCurrentUserData()
            val modal: UsersData? = realm?.where(UsersData::class.java)?.equalTo("userName", userDetails?.userName)?.findFirst()
            realm?.executeTransaction { realm -> // on below line we are setting data to our modal class
                // which we get from our edit text fields.
                modal?.userName = userDetails?.userName
                modal?.password = userDetails?.password
                modal?.emailId = userDetails?.emailId
                modal?.mobileNumber = userDetails?.mobileNumber
                modal?.isCurrentUser = false

                // inside on execute method we are calling a method to copy
                // and update to real m database from our modal class.
                realm.copyToRealmOrUpdate(modal)
            }
            return true
        } catch (e:Exception){
            return false
        }
    }

    fun reverseGeoCode(lat:Double,lng:Double){

        try{

            //val interceptor = HttpLoggingInterceptor()
            //interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            //val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl(ReverseGeoCodeUrl)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(client)
                .build()
            //animationLoader?.visibility = View.VISIBLE
            var latlng:String = "$lat,$lng"
            val service = retrofit.create(ApiCall::class.java)
            val call = service.reverseGeoCode(latlng, context?.resources?.getString(R.string.ApiKey)!!)
            call.enqueue(object : Callback<ReverseGeoCodeApi> {
                override fun onResponse(call: Call<ReverseGeoCodeApi>, response: Response<ReverseGeoCodeApi>) {
                    if (response.isSuccessful) {
                        //animationLoader?.visibility = View.GONE
                        val searchResponse = response.body()!!

                        if(searchResponse.results?.size!! >0){
                            var locationData = LocationData()
                            locationData.Latitude = lat.toString()
                            locationData.Longitude = lng.toString()
                            locationData.Address = searchResponse.results?.get(0)?.formatted_address
                            locationData.UserName= getCurrentUserData()?.userName
                            AddLocationData(locationData)
                        } else {
                            var locationData = LocationData()
                            locationData.Latitude = lat.toString()
                            locationData.Longitude = lng.toString()
                            locationData.Address = ReverseGeoCoding(lat,lng)
                            locationData.UserName= getCurrentUserData()?.userName
                            AddLocationData(locationData)
                        }

                    } else {
                        //animationLoader?.visibility = View.GONE
                        var locationData = LocationData()
                        locationData.Latitude = lat.toString()
                        locationData.Longitude = lng.toString()
                        locationData.Address = ReverseGeoCoding(lat,lng)
                        locationData.UserName= getCurrentUserData()?.userName
                        AddLocationData(locationData)
                    }
                }

                override fun onFailure(call: Call<ReverseGeoCodeApi>, t: Throwable) {
                    //animationLoader?.visibility = View.GONE
                    Log.e("Test","Search Places Api OnFailure Error= "+t.message);

                    var locationData = LocationData()
                    locationData.Latitude = lat.toString()
                    locationData.Longitude = lng.toString()
                    locationData.Address = ReverseGeoCoding(lat,lng)
                    locationData.UserName= getCurrentUserData()?.userName
                    AddLocationData(locationData)
                }
            })
        } catch (e:Exception){
            var locationData = LocationData()
            locationData.Latitude = lat.toString()
            locationData.Longitude = lng.toString()
            locationData.Address = ReverseGeoCoding(lat,lng)
            locationData.UserName= getCurrentUserData()?.userName
            AddLocationData(locationData)
        }

    }

    object functions {
        public var mViewPager: ViewPager? = null
        public var animationLoader: LottieAnimationView? = null
    }

    companion object{
        private var ReverseGeoCodeUrl:String = "https://maps.googleapis.com/"
    }

}