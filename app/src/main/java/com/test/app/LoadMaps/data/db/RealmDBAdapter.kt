package com.test.app.LoadMaps.data.db

import android.content.Context
import io.realm.Realm

class RealmDBAdapter private constructor(private val context: Context) {
    private val realm: Realm

    init {
        realm = Realm.getDefaultInstance()
    }

    @Throws(Exception::class)
    fun IsloginAccountExists(): Boolean {
        return try {
            var isExists = false
            val dataModals = realm.where(
                UserData::class.java
            ).equalTo("isCurrentUser", true).findAll()
            if (dataModals.size > 0) {
                isExists = true
            }
            isExists
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    @Throws(Exception::class)
    fun RegisterData(registerData: UserData): Boolean {
        return try {
            if (!findUserNameExists(registerData.userName)) {
                val userData = UserData()
                val id = realm.where(UserData::class.java).max("id")
                val nextId: Long
                nextId = if (id == null) {
                    java.lang.Long.valueOf(1)
                } else {
                    (id.toInt() + 1).toLong()
                }
                userData.id = nextId
                userData.userName = registerData.userName
                userData.mobileNumber = registerData.mobileNumber
                userData.emailId = registerData.emailId
                userData.password = registerData.password
                userData.isCurrentUser = false
                realm.executeTransaction { realm -> // inside on execute method we are calling a method
                    // to copy to real m database from our modal class.
                    realm.copyToRealm(userData)
                }
                true
            } else {
                false
            }
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    @Throws(Exception::class)
    fun findUserNameExists(UserName: String?): Boolean {
        return try {
            var isExists = false
            val dataModals = realm.where(UserData::class.java)
                .equalTo("userName", UserName).findAll()
            if (dataModals.size > 0) {
                isExists = true
            }
            isExists
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    @Throws(Exception::class)
    fun IsloginCredentialExists(UserName: String?, Password: String?): Boolean {
        return try {
            var isExists = false
            val dataModals = realm.where(UserData::class.java)
                .equalTo("userName", UserName).findAll()
            for (i in dataModals.indices) {
                if (Password == dataModals[i]?.password) {
                    isExists = true
                    loginUser(UserName)
                }
            }
            isExists
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    fun readlogindata(userName: String?): UserData? {
        return realm.where(
            UserData::class.java
        ).equalTo("userName", userName).findFirst()
    }

    @Throws(Exception::class)
    fun loginUser(UserName: String?) {
        try {
            val loginDetails = readlogindata(UserName)
            val modal = realm.where(UserData::class.java)
                .equalTo("userName", UserName).findFirst()
            realm.executeTransaction { realm -> // inside on execute method we are calling a method
                // to copy to real m database from our modal class.
                modal?.userName = loginDetails?.userName
                modal?.password = loginDetails?.password
                modal?.emailId = loginDetails?.emailId
                modal?.mobileNumber = loginDetails?.mobileNumber
                modal?.isCurrentUser = true
                realm.copyToRealmOrUpdate(modal)
            }
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    private val currentUserData: UserData?
        private get() = realm.where(UserData::class.java).equalTo("isCurrentUser", true).findFirst()

        @Throws(Exception::class)
        fun LogOut(): Boolean {
        return try {
            realm.executeTransaction(object : Realm.Transaction {
                override fun execute(realm: Realm) {
                    val userDetails: UserData? = currentUserData
                    val modal = realm.where(UserData::class.java).equalTo("userName", userDetails?.userName).findFirst()
                    modal?.userName = userDetails?.userName
                    modal?.password = userDetails?.password
                    modal?.emailId = userDetails?.emailId
                    modal?.mobileNumber = userDetails?.mobileNumber
                    modal?.isCurrentUser = false

                    // inside on execute method we are calling a method to copy
                    // and update to real m database from our modal class.
                    realm.copyToRealmOrUpdate(modal)
                }
            })
            true
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    companion object {
        var realmDBAdapterInstance: RealmDBAdapter? = null
        fun getInstance(context: Context): RealmDBAdapter? {
            if (realmDBAdapterInstance == null) {
                realmDBAdapterInstance = RealmDBAdapter(context)
            }
            return realmDBAdapterInstance
        }
    }
}
