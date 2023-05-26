package com.test.app.LoadMaps.Fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.airbnb.lottie.LottieAnimationView
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

class LocationList(context:Context,activity: Activity):Fragment() {

    private var mView: View? = null

    var rv_location_history: RecyclerView? = null
    var locationList: ArrayList<LocationData> = ArrayList<LocationData>()
    var mContext:Context? = null
    var mActivity:Activity? = null

    private lateinit var realmListener: RealmChangeListener<Realm>

    init {
        mContext = context
        mActivity = activity
    }

    private var realm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater?.inflate(R.layout.location_history, container, false)

        realm = Realm.getDefaultInstance();
        animationLoader = mView?.findViewById(R.id.animation_loader)
        rv_location_history = mView?.findViewById(R.id.rv_locationhistory)
        realmListener = RealmChangeListener {
            Log.e("Test","LocationList realmListener Called()");
            loadLocationData(false)
        }

        loadLocationData(true)

        realm?.addChangeListener(realmListener)

        return mView
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    fun loadLocationData(istrue:Boolean){


        locationList = ArrayList<LocationData>()
        //Test
        Log.e("Test","loadLocationData Called() locationList = ${locationList.size}")
        //if(istrue)
        //animationLoader?.visibility = View.VISIBLE

        //Log.e("Test","ReadAllData -  Called()")

        //var dataModals = realm!!.where(LocationData::class.java)?.findAll()
        val dataModals = realm?.where(LocationData::class.java)?.equalTo("UserName", getCurrentUserData()?.userName)?.findAll()

        Log.e("Test","ReadAllData - Size = ${dataModals?.size}")

        for(i in 0 until dataModals?.size!!){
            locationList.add(dataModals.get(i)!!)
        }
        //Test
        Log.e("Test","inside taskSuccess locationList = ${locationList.size}")
        if(locationList.size>0){
            locationList.reverse()
            val adapter = LocationHistoryAdapter(locationList,mActivity!!)
            rv_location_history?.layoutManager = LinearLayoutManager(context)
            rv_location_history?.adapter = adapter
        } else {
            locationList = ArrayList<LocationData>()
            val adapter = LocationHistoryAdapter(locationList,mActivity!!)
            rv_location_history?.layoutManager = LinearLayoutManager(mContext)
            rv_location_history?.adapter = adapter
        }
    }

    class LocationHistoryAdapter(placesList:ArrayList<LocationData>?,activity: Activity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        var placesList = placesList
        var activity = activity

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view: View
            return if (viewType == 2) {
                view = LayoutInflater.from(activity)
                    .inflate(R.layout.location_details, parent, false)
                ViewHolderHome(view)
            } else {
                view = LayoutInflater.from(activity).inflate(R.layout.empty_view, parent, false)
                ViewHolderEmpty(view)
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder.itemViewType == 2) {
                val holder1 = holder as ViewHolderHome

                //Log.e("Test","PlacesSearch onBindViewHolder Called")

                holder1.mTvPlacename.setText(placesList?.get(position)?.Address)
                holder1.mLayoutPlaceNameHolder.setOnClickListener { v: View? ->
                    Log.e("Test","Item Clicked");
                    currentLatitude = placesList?.get(position)?.Latitude?.toDouble()
                    currentLongitude = placesList?.get(position)?.Longitude?.toDouble()
                    placename = placesList?.get(position)?.Address
                    HomeActivity.functions.mViewPager?.setCurrentItem(1)
                }

            } else {
                val viewHolderEmpty = holder as ViewHolderEmpty
                viewHolderEmpty.tv_MenuListEmpty.setText("No Data Found")
            }
        }

        override fun getItemCount(): Int {
            return if (placesList != null) {
                if (placesList!!.size > 0) {
                    placesList!!.size
                } else {
                    1
                }
            } else {
                1
            }
        }

        override fun getItemViewType(position: Int): Int {
            return if (placesList != null) {
                if (placesList!!.size > 0) {
                    2
                } else {
                    1
                }
            } else {
                1
            }
        }

        private inner class ViewHolderHome internal constructor(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

            var mTvPlacename: TextView
            var mLayoutPlaceNameHolder: FrameLayout

            init {
                mTvPlacename = itemView.findViewById<TextView>(R.id.tv_location)
                mLayoutPlaceNameHolder = itemView.findViewById<FrameLayout>(R.id.layout_location_history)
            }
        }

        private inner class ViewHolderEmpty internal constructor(view: View) :
            RecyclerView.ViewHolder(view) {
            var tv_MenuListEmpty: TextView

            init {
                tv_MenuListEmpty = view.findViewById<TextView>(R.id.tv_empty)
            }
        }
    }

    fun getCurrentUserData(): UsersData? {
        val loginDetails: UsersData? = realm?.where(UsersData::class.java)?.equalTo("isCurrentUser", true)?.findFirst()
        return loginDetails
    }

    object functions {
        public var animationLoader: LottieAnimationView? = null
    }

}