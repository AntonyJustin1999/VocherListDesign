package com.test.app.LoadMaps.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import com.test.app.LoadMaps.data.dataSets.CountriesApi
import com.test.app.LoadMaps.data.db.UserData

interface CommonViewModel : LifecycleObserver {
    fun getMutableIsAccountActive(): LiveData<Boolean?>?
    fun errorMessage(): LiveData<String?>?

    fun LoginAuthentication(username: String?, password: String?)
    fun loginValidation(): LiveData<Boolean?>?

    //void LoggedInUser(String UserName);
    fun RegisterAccount(registerData: UserData?)
    fun registrationSuccess(): LiveData<Boolean?>?

    fun OnLogOut()
    fun onLogOut(): LiveData<Boolean?>?

    fun loadCountryList()
    fun countryList(): LiveData<ArrayList<CountriesApi?>?>?
    fun networkIsAvailable(): LiveData<Boolean?>?
    fun countrylistgetError(): LiveData<String?>?

    fun showAndHideProgress(): LiveData<Boolean?>?
}