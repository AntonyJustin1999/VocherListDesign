package com.test.app.task.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.app.R
import com.test.app.task.repository.UserRepositoryImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModelImpl(context: Context) : ViewModel(), LifecycleObserver {
    var splashlivedata: MutableLiveData<Boolean?>
    var errorMessage: MutableLiveData<String?>
    var mContext:Context?

    init {
        errorMessage = MutableLiveData()
        splashlivedata = MutableLiveData()
        this.mContext = context
    }

    fun initSplashScreen() {
        viewModelScope.launch {
            try {
                delay(3000)
                splashlivedata.value = true
            } catch (e: Exception) {
                errorMessage.value = mContext?.getString(R.string.something_wrong)
                splashlivedata.value = false
                Log.e("Test","Exception => "+e.message)
            }
        }
    }

}