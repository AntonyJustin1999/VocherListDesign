package com.test.app.LoadMaps.data.db

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserData : RealmObject() {
    // on below line we are creating our variables
    // and with are using primary key for our id.
    @PrimaryKey
    var id: Long = 0
    var userName: String? = null
    var mobileNumber: String? = null
    var emailId: String? = null
    var password: String? = null
    var isCurrentUser:Boolean? = false
}