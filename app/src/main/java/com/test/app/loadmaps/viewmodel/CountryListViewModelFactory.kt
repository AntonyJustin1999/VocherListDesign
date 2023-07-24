package com.test.app.loadmaps.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CountryListViewModelFactory(val context:Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CountryListViewModelImpl::class.java)){
            return CountryListViewModelImpl(context) as T
        }
        throw IllegalArgumentException ("UnknownViewModel")
    }
}