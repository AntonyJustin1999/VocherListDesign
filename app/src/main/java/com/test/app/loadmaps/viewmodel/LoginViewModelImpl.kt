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

class LoginViewModelImpl : ViewModel(), LifecycleObserver {
    var userRepository: UserRepositoryImpl
    var errorMessage: MutableLiveData<String?>
    var username: MutableLiveData<String?>
    var password: MutableLiveData<String?>
    var onLogin: MutableLiveData<Boolean?>
    var onRegisterRedirect: MutableLiveData<Boolean?>
    var isProgressShow: MutableLiveData<Boolean?>

    init {
        userRepository = UserRepositoryImpl.instance as UserRepositoryImpl
        onLogin = MutableLiveData()
        onRegisterRedirect = MutableLiveData()
        username = MutableLiveData()
        password = MutableLiveData()
        isProgressShow = MutableLiveData(false)
        errorMessage = MutableLiveData()
    }

    fun LoginAuthentication(username: String?, password: String?) {
        try {
            onLogin.setValue(userRepository.IsloginAccountExists(username, password)?.value)
        } catch (e: Exception) {
            errorMessage.setValue(e.message)
        }
    }

    fun onLoginClicked() {
        onLoggedIn(
            username.value?:"".trim { it <= ' ' },
            password.value?:"".trim { it <= ' ' })
    }

    fun onRegisterClicked() {
        onRegisterRedirect.value = true
    }

    fun onLoggedIn(username: String?, password: String?) {
        var username = username?:""
        var password = password?:""
        if (username.length > 0) {
            if (password.length > 0) {
                isProgressShow.value = true
                try {
                    LoginAuthentication(username, password)
                    isProgressShow.value = false
                } catch (e: Exception) {
                    errorMessage.value = e.message
                    isProgressShow.value = false
                }
            } else {
                errorMessage.value = "Please enter Password"
            }
        } else {
            errorMessage.value = "Please enter UserName"
        }
    }

}