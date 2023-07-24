package com.test.app.loadmaps.viewmodel

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.app.loadmaps.data.db.UserData
import com.test.app.loadmaps.repository.UserRepositoryImpl
import com.test.app.loadmaps.utils.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterViewModelImpl : ViewModel(), LifecycleObserver {
    var userRepository: UserRepositoryImpl
    var errorMessage: MutableLiveData<String?>
    var onRegister: MutableLiveData<Boolean>
    var username: MutableLiveData<String?>
    var password: MutableLiveData<String?>
    var con_password: MutableLiveData<String?>
    var mobilenum: MutableLiveData<String?>
    var email: MutableLiveData<String?>
    var onLoginRedirect: MutableLiveData<Boolean?>
    var onLoginrRedirect: MutableLiveData<Boolean?>
    var isProgressShow: MutableLiveData<Boolean?>

    init {
        userRepository = UserRepositoryImpl.instance as UserRepositoryImpl
        onLoginrRedirect = MutableLiveData()
        onRegister = MutableLiveData()
        errorMessage = MutableLiveData()
        username = MutableLiveData()
        password = MutableLiveData()
        con_password = MutableLiveData()
        mobilenum = MutableLiveData()
        email = MutableLiveData()
        onLoginRedirect = MutableLiveData()
        isProgressShow = MutableLiveData()
    }

    fun onRegisterClicked(){
        onRegistration(
            username.value?:"".trim { it <= ' ' },
            password.value?:"".trim { it <= ' ' },
            con_password.value?:"".trim { it <= ' ' },
            mobilenum.value?:"".trim { it <= ' ' },
            email.value?:"".trim { it <= ' ' },)
    }

    fun onLoginClicked(){
        onLoginrRedirect.value = true
    }

    fun onRegistration(username: String, password: String, con_password: String,
        mobilenum: String,
        emailId: String) {
        var username = username?:""
        var mobilenum = mobilenum?:""
        var emailId = emailId?:""
        var password = password?:""
        var con_password = con_password?:""

        if (username.length > 0) {
            if (mobilenum.length > 0) {
                if (emailId.length > 0) {
                    if (Utils.isValidEmail(emailId)) {
                        if (password.length > 0) {
                            if (con_password.length > 0) {
                                if (password == con_password) {
                                    isProgressShow.value = true
                                    val registerData = UserData()
                                    registerData.userName = username
                                    registerData.mobileNumber = mobilenum
                                    registerData.emailId = emailId
                                    registerData.password = password
                                    registerData.isCurrentUser = false
                                    RegisterAccount(registerData)
                                    isProgressShow.value = false
                                } else {
                                    errorMessage.value = "Password and Confirm Password Must be Same"
                                }
                            } else {
                                errorMessage.value = "Please enter Confirm Password"
                            }
                        } else {
                            errorMessage.value = "Please enter Password"
                        }
                    } else {
                        errorMessage.value = "Please enter valid eMail Id"
                    }
                } else {
                    errorMessage.value = "Please enter eMail Id"
                }
            } else {
                errorMessage.value = "Please enter MobileNumber"
            }
        } else {
            errorMessage.value = "Please enter user name"
        }
    }

    fun RegisterAccount(registerData: UserData?) {
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

}