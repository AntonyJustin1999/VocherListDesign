package com.test.app.loadmaps.repository

import androidx.lifecycle.LiveData
import com.test.app.loadmaps.data.db.UserData

interface UsersRepository {
    @Throws(Exception::class)
    fun getIsAccoutActive(): LiveData<Boolean?>?

    @Throws(Exception::class)
    fun IsloginAccountExists(username: String?, password: String?): LiveData<Boolean?>?

    //void loggedInUser(String UserName) throws Exception;
    @Throws(Exception::class)
    fun FindUserNameExists(userName: String?): LiveData<Boolean?>?

    @Throws(Exception::class)
    fun UserRegistration(registerData: UserData?)

    @Throws(Exception::class)
    fun OnLogOut(): LiveData<Boolean?>?
}
