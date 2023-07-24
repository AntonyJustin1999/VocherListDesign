package com.test.app.loadmaps.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.app.loadmaps.data.api.RestManager
import com.test.app.loadmaps.data.dataSets.CountriesApi
import com.test.app.loadmaps.repository.UserRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryListViewModelImpl : ViewModel(), LifecycleObserver {
    var userRepository: UserRepositoryImpl
    var isProgressShow: MutableLiveData<Boolean?>
    var mutableCountryList: MutableLiveData<ArrayList<CountriesApi?>?>
    var onNetwork: MutableLiveData<Boolean>
    var countryListErrorMessage: MutableLiveData<String>
    var restManager: RestManager
    var errorMessage: MutableLiveData<String?>

    init {
        userRepository = UserRepositoryImpl.instance as UserRepositoryImpl
        isProgressShow = MutableLiveData()
        restManager = RestManager()
        errorMessage = MutableLiveData()
        mutableCountryList = MutableLiveData()
        onNetwork = MutableLiveData()
        countryListErrorMessage = MutableLiveData()
    }
    fun loadCountryList() {
        var Call: Call<java.util.ArrayList<CountriesApi?>?>? = null

        //if(isNetworkAvailable()){
        try {
            Call = restManager.API.getAllCountrieslist(true, "" + "name,flags")
            if (Call != null) {
                isProgressShow.setValue(true)
                Call.enqueue(object : Callback<java.util.ArrayList<CountriesApi?>?> {
                    override fun onResponse(call: Call<java.util.ArrayList<CountriesApi?>?>,
                                            response: Response<java.util.ArrayList<CountriesApi?>?>)
                    {
                        try {
                            isProgressShow.setValue(false)
                            val data = response.body()
                            if (data != null) {
                                mutableCountryList.setValue(response.body())
                            } else {
                                countryListErrorMessage.setValue("Something Wrong Try Again!!")
                            }
                        } catch (e: java.lang.Exception) {
                            isProgressShow.setValue(false)
                            countryListErrorMessage.setValue("Something Wrong Try Again!!")
                        }
                    }

                    override fun onFailure(call: Call<java.util.ArrayList<CountriesApi?>?>, t: Throwable)
                    {
                        isProgressShow.setValue(false)
                        countryListErrorMessage.setValue("Something Wrong Try Again!!")
                    }
                })
            }
        } catch (e: Throwable) {
            isProgressShow.setValue(false)
            countryListErrorMessage.setValue("Something Wrong Try Again!!")
        }
    }


}