package com.test.app.loadmaps.viewmodel

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.test.app.loadmaps.data.api.RestManager
import com.test.app.loadmaps.data.dataSets.CountryDetailsApi
import com.test.app.loadmaps.repository.UserRepositoryImpl
import com.test.app.loadmaps.utils.internet.ConnectionReceiver
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CountryDetailsViewModelImpl(val context: Context) : ViewModel(), DefaultLifecycleObserver,
    ConnectionReceiver.ReceiverListener {
    var userRepository: UserRepositoryImpl
    var isProgressShow: MutableLiveData<Boolean?>
    var isNetwork: MutableLiveData<Boolean>
    var isCountryDetailsShow: MutableLiveData<Boolean?>
    var countryListEmptyMessage: MutableLiveData<String>
    var isDataEmpty: MutableLiveData<Boolean?>
    var restManager: RestManager
    var errorMessage: MutableLiveData<String?>
    var isErrorExist: MutableLiveData<Boolean?>

    var commonName: MutableLiveData<String?>
    var officialName: MutableLiveData<String?>
    var currency: MutableLiveData<String?>
    var capital: MutableLiveData<String?>
    var languages: MutableLiveData<String?>
    var start_of_week: MutableLiveData<String?>
    var lat_lng: MutableLiveData<String?>
    var population: MutableLiveData<String?>

    var mContext:Context
    var connectionReceiver:ConnectionReceiver
    var countryName:String? = null

    init {
        userRepository = UserRepositoryImpl.instance as UserRepositoryImpl
        restManager = RestManager()
        isProgressShow = MutableLiveData()
        isNetwork = MutableLiveData()
        isCountryDetailsShow = MutableLiveData()
        countryListEmptyMessage = MutableLiveData()
        isDataEmpty = MutableLiveData()
        errorMessage = MutableLiveData()
        isErrorExist = MutableLiveData()

        commonName = MutableLiveData()
        officialName = MutableLiveData()
        currency = MutableLiveData()
        capital = MutableLiveData()
        languages = MutableLiveData()
        start_of_week = MutableLiveData()
        lat_lng = MutableLiveData()
        population = MutableLiveData()
        this.mContext = context
        connectionReceiver = ConnectionReceiver()
        Log.e("Test","CountryDetails Context = "+mContext)
    }

    fun loadCountryDetails(name:String?) {
        countryName = name
        var Call: Call<ArrayList<CountryDetailsApi?>?>? = null

        if (isNetworkAvailable()) {
            try {
                Call = restManager.API.getCountryInfo(name)
                if (Call != null) {
                    isProgressShow.setValue(true)
                    Call.enqueue(object : Callback<ArrayList<CountryDetailsApi?>?> {
                        override fun onResponse(call: Call<ArrayList<CountryDetailsApi?>?>,
                                                response: Response<ArrayList<CountryDetailsApi?>?>)
                        {
                            try {
                                val data = response.body()
                                if (data != null) {
                                    if (data.size > 0) {

                                        isCountryDetailsShow.value = true
                                        isNetwork.value = false
                                        isErrorExist.value = false
                                        isDataEmpty.value = false

                                        commonName.value = data.get(0)?.name?.common
                                        officialName.value = data.get(0)?.name?.official
                                        var temp: StringBuilder = StringBuilder("")
                                        for (i in 0 until data.get(0)?.capital?.size!!) {
                                            temp.append(data.get(0)?.capital?.get(i))
                                        }
                                        capital.value = temp.toString()

                                        start_of_week.value = data.get(0)?.startOfWeek
                                        population.value = data.get(0)?.population.toString()


                                        if (data.get(0)?.latLng?.size!! > 0) {
                                            lat_lng.value = "${data?.get(0)?.latLng?.get(0)},${
                                                data?.get(0)?.latLng?.get(1)
                                            }"
                                        }

//                                    Glide.with(context!!).load(countrieslist[0].getCoatOfArms().getPng_url())
//                                        .placeholder(R.drawable.baseline_image_24).into<Target<Drawable>>(iv_flag)

                                    } else {
                                        countryListEmptyMessage.value = "No Data Found"
                                        isDataEmpty.value = true
                                        isCountryDetailsShow.value = false
                                    }
                                } else {
                                    isErrorExist.value = true
                                    isCountryDetailsShow.value = false
                                    errorMessage.value = "Something Wrong Try Again!!"
                                }
                            } catch (e: Exception) {
                                isErrorExist.value = true
                                isCountryDetailsShow.value = false
                                errorMessage.value = "Something Wrong Try Again!!"
                            } finally {
                                isProgressShow.setValue(false)
                            }
                        }

                        override fun onFailure(call: Call<ArrayList<CountryDetailsApi?>?>, t: Throwable)
                        {
                            isErrorExist.value = true
                            isCountryDetailsShow.value = false
                            errorMessage.value = "Something Wrong Try Again!!"
                        }
                    })
                }
            } catch (e: Throwable) {
                isErrorExist.value = true
                isCountryDetailsShow.value = false
                errorMessage.value = "Something Wrong Try Again!!"
            }
        } else {
            isCountryDetailsShow.value = false
            isNetwork.value = true
        }
    }

    override fun onNetworkChange(isConnected: Boolean) {
        if(isConnected){
            loadCountryDetails(countryName)
        } else {
            //isCountryDetailsShow.value = false
            //isNetwork.value = true
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        context.registerReceiver(connectionReceiver, intentFilter)
        ConnectionReceiver.InternetListener = this
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        context.unregisterReceiver(connectionReceiver)
    }

    private fun isNetworkAvailable(): Boolean{
        try{
            if (mContext != null) {
                val connectivity = mContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (connectivity != null) {
                    val info = connectivity.activeNetworkInfo
                    if (info != null && info.isConnected) {
                        return true
                    }
                }
            }
            return false
        } catch (e: IllegalArgumentException) {
            return false
        }
    }

}