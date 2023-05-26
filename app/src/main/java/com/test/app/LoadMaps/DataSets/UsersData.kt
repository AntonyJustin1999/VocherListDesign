package com.test.app.LoadMaps.DataSets

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UsersData : RealmObject() {
    // on below line we are creating our variables
    // and with are using primary key for our id.
    @PrimaryKey
    public var id: Long = 0
    public var userName: String? = null
    public var mobileNumber: String? = null
    public var emailId: String? = null
    public var password: String? = null
    public var isCurrentUser: Boolean? = false

//    fun getId(): Long {
//        return id
//    }
//
//    fun setId(id: Long) {
//        this.id = id
//    }
//    fun getuserName(): String? {
//        return userName
//    }
//    fun setuserName(userName: String) {
//        this.userName = userName
//    }
//
//    fun getmobileNumber(): String? {
//        return mobileNumber
//    }
//    fun setmobileNumber(mobileNumber: String) {
//        this.mobileNumber = mobileNumber
//    }

}