package com.test.app.loadmaps.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CountryDetailsViewModelFactory(val context:Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CountryDetailsViewModelImpl::class.java)){
            return CountryDetailsViewModelImpl(context) as T
        }
        throw IllegalArgumentException ("UnknownViewModel")
    }
}