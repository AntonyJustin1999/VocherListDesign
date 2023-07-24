package com.test.app.loadmaps.viewmodel

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.app.loadmaps.repository.UserRepositoryImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModelImpl : ViewModel(), LifecycleObserver {
    var userRepository: UserRepositoryImpl
    var splashliveData: MutableLiveData<Boolean>
    var mutableIsAccountActive: MutableLiveData<Boolean?>
    var errorMessage: MutableLiveData<String?>

    init {
        userRepository = UserRepositoryImpl.instance as UserRepositoryImpl
        splashliveData = MutableLiveData()
        mutableIsAccountActive = MutableLiveData()
        errorMessage = MutableLiveData()
    }

    fun initSplashScreen() {
        viewModelScope.launch {
            try {
                delay(3000)
                splashliveData.value = userRepository.getIsAccoutActive()?.value
                Log.e("Test","viewmodel called - "+splashliveData.value)
            } catch (e: Exception) {
                errorMessage.value = e.message
            }
        }
    }

}