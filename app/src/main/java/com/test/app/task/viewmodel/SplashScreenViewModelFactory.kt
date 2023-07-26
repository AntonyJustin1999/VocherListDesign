package com.test.app.task.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SplashScreenViewModelFactory(val context:Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SplashViewModelImpl::class.java)){
            return SplashViewModelImpl(context) as T
        }
        throw IllegalArgumentException ("UnknownViewModel")
    }
}