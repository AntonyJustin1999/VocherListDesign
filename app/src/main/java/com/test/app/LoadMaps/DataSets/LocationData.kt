package com.test.app.LoadMaps.DataSets

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class LocationData : RealmObject() {
    // on below line we are creating our variables
    // and with are using primary key for our id.
    @PrimaryKey
    public var id: Long = 0
    public var Latitude: String? = null
    public var Longitude: String? = null
    public var Address: String? = null
    public var UserName: String? = null

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