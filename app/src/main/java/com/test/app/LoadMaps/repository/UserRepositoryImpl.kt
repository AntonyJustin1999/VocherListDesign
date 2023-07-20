package com.test.app.LoadMaps.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.test.app.LoadMaps.data.db.RealmDBAdapter
import com.test.app.LoadMaps.data.db.RealmDBAdapter.Companion.realmDBAdapterInstance
import com.test.app.LoadMaps.data.db.UserData

class UserRepositoryImpl() : UsersRepository {
    val mutableIsAnyAccountActive: MutableLiveData<Boolean>
    val mutableLoginValidation: MutableLiveData<Boolean>
    val mutableRegisterSuccess: MutableLiveData<Boolean>
    val mutableLogOutProcess: MutableLiveData<Boolean>
    var isAccountActive:Boolean? = false
    val realmDBAdapter: RealmDBAdapter?

    init {
        realmDBAdapter = realmDBAdapterInstance
        mutableIsAnyAccountActive = MutableLiveData()
        mutableLoginValidation = MutableLiveData()
        mutableRegisterSuccess = MutableLiveData()
        mutableLogOutProcess = MutableLiveData()
    }

    @Throws(Exception::class)
    override fun getIsAccoutActive(): LiveData<Boolean?>? {
        isAccountActive = realmDBAdapter?.IsloginAccountExists()
        mutableIsAnyAccountActive.value = isAccountActive
        Log.e("Test", " mutableIsAccountActive reference: $mutableIsAnyAccountActive")
        return mutableIsAnyAccountActive
    }

    override fun IsloginAccountExists(username: String?, password: String?): LiveData<Boolean?>? {
        mutableLoginValidation.value = realmDBAdapter?.IsloginCredentialExists(username, password)
        Log.e("Test", " mutableLoginValidation reference: $mutableLoginValidation")
        return mutableLoginValidation
    }

    override fun FindUserNameExists(userName: String?): LiveData<Boolean?>? {
        mutableRegisterSuccess.value = realmDBAdapter?.findUserNameExists(userName)
        return mutableRegisterSuccess
    }

    @Throws(Exception::class)
    override fun UserRegistration(registerData: UserData?) {
        realmDBAdapter?.RegisterData(registerData!!)
    }

    @Throws(Exception::class)
    override fun OnLogOut(): LiveData<Boolean?>? {
        mutableLogOutProcess.value = realmDBAdapter?.LogOut()
        return mutableLogOutProcess
    }
    companion object {
        var instance: UsersRepository? = null
            get() {
                if (field == null) {
                    field = UserRepositoryImpl()
                }
                return field
            }
    }
}