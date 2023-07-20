package com.test.app.LoadMaps.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.app.LoadMaps.data.api.RestManager
import com.test.app.LoadMaps.data.dataSets.CountriesApi
import com.test.app.LoadMaps.data.db.UserData
import com.test.app.LoadMaps.repository.UserRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommonViewModelImplementor() : ViewModel(), CommonViewModel {
     var userRepository: UserRepositoryImpl
     var mutableIsAccountActive: MutableLiveData<Boolean?>
     var errorMessage: MutableLiveData<String?>
     var onLogin: MutableLiveData<Boolean?>
     var onRegister: MutableLiveData<Boolean>
     var onLogOut: MutableLiveData<Boolean>
     var mutableCountryList: MutableLiveData<ArrayList<CountriesApi?>?>
     var onNetwork: MutableLiveData<Boolean>
     var countryListErrorMessage: MutableLiveData<String>
     var isProgressShow: MutableLiveData<Boolean?>
     var restManager: RestManager
     //var context: Context

    init {
        userRepository = UserRepositoryImpl.instance as UserRepositoryImpl
        restManager = RestManager()
        mutableIsAccountActive = MutableLiveData()
        errorMessage = MutableLiveData()
        onLogin = MutableLiveData()
        onRegister = MutableLiveData()
        onLogOut = MutableLiveData()
        mutableCountryList = MutableLiveData()
        onNetwork = MutableLiveData()
        countryListErrorMessage = MutableLiveData()
        isProgressShow = MutableLiveData()
        //this.context = context
    }

    override fun getMutableIsAccountActive(): LiveData<Boolean?> {
        return try {
            mutableIsAccountActive.value = userRepository.isAccountActive
            mutableIsAccountActive
        } catch (e: Exception) {
            errorMessage.value = e.message
            MutableLiveData()
        }
    }

    override fun errorMessage(): LiveData<String?>? {
        return errorMessage
    }

    override fun LoginAuthentication(username: String?, password: String?) {
        try {
            onLogin.setValue(userRepository.IsloginAccountExists(username, password)?.value)
        } catch (e: Exception) {
            errorMessage.setValue(e.message)
        }
    }

    override fun loginValidation(): LiveData<Boolean?>? {
        return onLogin
    }

    override fun RegisterAccount(registerData: UserData?) {
        try {
            if (!userRepository.FindUserNameExists(registerData?.userName)?.value!!) {
                onRegister.value = false
                userRepository.UserRegistration(registerData)
            } else {
                onRegister.setValue(true)
            }
        } catch (e: Exception) {
            errorMessage.setValue(e.message)
        }
    }

    override fun registrationSuccess(): LiveData<Boolean?>? {
        return onRegister
    }

    override fun loadCountryList() {
        var Call: Call<java.util.ArrayList<CountriesApi?>?>? = null

        //if(isNetworkAvailable()){
        try {
            Call = restManager.API.getAllCountrieslist(true, "" + "name,flags")
            if (Call != null) {
                isProgressShow.setValue(true)
                Call.enqueue(object : Callback<java.util.ArrayList<CountriesApi?>?> {
                    override fun onResponse(call: Call<java.util.ArrayList<CountriesApi?>?>,
                        response: Response<java.util.ArrayList<CountriesApi?>?>) {
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

                    override fun onFailure(
                        call: Call<java.util.ArrayList<CountriesApi?>?>,
                        t: Throwable
                    ) {
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

    override fun countryList(): LiveData<ArrayList<CountriesApi?>?>? {
        return mutableCountryList
    }

    override fun networkIsAvailable(): LiveData<Boolean?>? {
        return onNetwork
    }

    override fun countrylistgetError(): LiveData<String?>? {
        return countryListErrorMessage
    }

    override fun showAndHideProgress(): LiveData<Boolean?>? {
        return isProgressShow
    }

    override fun OnLogOut() {
        try {
            if (userRepository.OnLogOut()?.value!!) {
                onLogOut.setValue(true)
            } else {
                onRegister.setValue(false)
            }
        } catch (e: Exception) {
            errorMessage.setValue(e.message)
        }
    }

    override fun onLogOut(): LiveData<Boolean?>? {
        return onLogOut
    }
}