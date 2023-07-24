package com.test.app.loadmaps.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData

interface CommonViewModel : LifecycleObserver {
    fun errorMessage(): LiveData<String?>?
    fun OnLogOut()
    fun onLogOut(): LiveData<Boolean?>?
}