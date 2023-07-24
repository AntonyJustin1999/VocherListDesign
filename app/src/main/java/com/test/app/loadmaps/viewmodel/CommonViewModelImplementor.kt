package com.test.app.loadmaps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
import com.test.app.loadmaps.repository.UserRepositoryImpl

class CommonViewModelImplementor() : ViewModel(), CommonViewModel {
     var userRepository: UserRepositoryImpl
     var onLogOut: MutableLiveData<Boolean>
    var errorMessage: MutableLiveData<String?>
    init {
         userRepository = UserRepositoryImpl.instance as UserRepositoryImpl
         errorMessage = MutableLiveData()
         onLogOut = MutableLiveData()
     }

    override fun errorMessage(): LiveData<String?>? {
        return errorMessage
    }

    override fun OnLogOut() {
        try {
            if (userRepository.OnLogOut()?.value!!) {
                onLogOut.setValue(true)
            }
        } catch (e: Exception) {
            errorMessage.setValue(e.message)
        }
    }

    override fun onLogOut(): LiveData<Boolean?>? {
        return onLogOut
    }
}