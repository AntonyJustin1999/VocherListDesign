package com.test.app.task.data.dataSets

import com.google.gson.annotations.SerializedName

class UserListData {
    @SerializedName("list")
    var userList: ArrayList<UserDetails>? = null

    @SerializedName("total_page")
    var total_page: Int? = 0

    @SerializedName("current_page")
    var current_page: String? = ""
}