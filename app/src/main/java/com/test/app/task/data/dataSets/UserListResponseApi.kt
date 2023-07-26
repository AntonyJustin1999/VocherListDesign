package com.test.app.task.data.dataSets

import com.google.gson.annotations.SerializedName

class UserListResponseApi {
    @SerializedName("status")
    var status: Int? = 0

    @SerializedName("data")
    var data: UserListData? = null

    @SerializedName("time")
    var time: Long? = 0

    @SerializedName("message")
    var message: String? = ""

}