package com.test.app.loadmaps.viewmodel

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.app.loadmaps.data.api.RestManager
import com.test.app.loadmaps.data.dataSets.CountriesApi
import com.test.app.loadmaps.repository.UserRepositoryImpl
import com.test.app.loadmaps.utils.internet.ConnectionReceiver
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryListViewModelImpl(val context: Context) : ViewModel(), DefaultLifecycleObserver,
    ConnectionReceiver.ReceiverListener {
    var userRepository: UserRepositoryImpl
    var mutableCountryList: MutableLiveData<ArrayList<CountriesApi?>?>
    var restManager: RestManager

    var isProgressShow: MutableLiveData<Boolean?>
    var isNetwork: MutableLiveData<Boolean>
    var isCountryListsShow: MutableLiveData<Boolean?>
    var countryListEmptyMessage: MutableLiveData<String>
    var isDataEmpty: MutableLiveData<Boolean?>
    var errorMessage: MutableLiveData<String?>
    var isErrorExist: MutableLiveData<Boolean?>

    var mContext:Context
    var connectionReceiver:ConnectionReceiver

    init {
        userRepository = UserRepositoryImpl.instance as UserRepositoryImpl
        restManager = RestManager()
        errorMessage = MutableLiveData()
        mutableCountryList = MutableLiveData()
        isProgressShow = MutableLiveData()
        isNetwork = MutableLiveData()
        isCountryListsShow = MutableLiveData()
        countryListEmptyMessage = MutableLiveData()
        isDataEmpty = MutableLiveData()
        isErrorExist = MutableLiveData()

        this.mContext = context
        connectionReceiver = ConnectionReceiver()
        Log.e("Test","CountryDetails Context = "+mContext)
    }
    fun loadCountryList() {
        var Call: Call<java.util.ArrayList<CountriesApi?>?>? = null

        if (isNetworkAvailable()) {
            try {
                Call = restManager.API.getAllCountrieslist(true, "" + "name,flags")
                if (Call != null) {
                    isProgressShow.value = true
                    Call.enqueue(object : Callback<java.util.ArrayList<CountriesApi?>?> {
                        override fun onResponse(
                            call: Call<java.util.ArrayList<CountriesApi?>?>,
                            response: Response<java.util.ArrayList<CountriesApi?>?>) {
                            try {
                                val data = response.body()
                                if (data != null) {
                                    if(data.size>0){
                                        isCountryListsShow.value = true
                                        isNetwork.value = false
                                        isErrorExist.value = false
                                        isDataEmpty.value = false
                                        mutableCountryList.setValue(response.body())
                                    } else{
                                        countryListEmptyMessage.value = "No Data Found"
                                        isDataEmpty.value = true
                                        isCountryListsShow.value = false
                                    }
                                } else {
                                    isErrorExist.value = true
                                    isCountryListsShow.value = false
                                    errorMessage.value = "Something Wrong Try Again!!"
                                }
                            } catch (e: Exception) {
                                isErrorExist.value = true
                                isCountryListsShow.value = false
                                errorMessage.value = "Something Wrong Try Again!!"
                            } finally {
                                isProgressShow.setValue(false)
                            }
                        }

                        override fun onFailure(call: Call<java.util.ArrayList<CountriesApi?>?>,
                                               t: Throwable) {
                            isErrorExist.value = true
                            isCountryListsShow.value = false
                            errorMessage.value = "Something Wrong Try Again!!"
                        }
                    })
                }
            } catch (e: Throwable) {
                isErrorExist.value = true
                isCountryListsShow.value = false
                errorMessage.value = "Something Wrong Try Again!!"
            }
        } else {
            isCountryListsShow.value = false
            isNetwork.value = true
        }
    }

    override fun onNetworkChange(isConnected: Boolean) {
        if(isConnected){
            loadCountryList()
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