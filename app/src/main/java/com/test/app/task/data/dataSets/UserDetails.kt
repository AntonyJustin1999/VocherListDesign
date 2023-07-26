package com.test.app.task.data.dataSets

import com.google.gson.annotations.SerializedName

class UserDetails {

    @SerializedName("branch_name")
    var branch_name: String? = ""

    @SerializedName("branch_image_path")
    var branch_image_path: String? = ""

    @SerializedName("landmark")
    var landmark: String? = ""

    @SerializedName("voucher_title")
    var voucher_title: String? = ""

    @SerializedName("purchased_on")
    var purchased_on: String? = ""

    @SerializedName("valid_on")
    var valid_on: String? = ""

    @SerializedName("order_number")
    var order_number: String? = ""

    @SerializedName("price")
    var price: String? = ""

    @SerializedName("voucher_status")
    var voucher_status: Int? = 0

    @SerializedName("voucher_status_label")
    var voucher_status_label: String? = ""

    @SerializedName("prepaid_voucher_code")
    var prepaid_voucher_code: String? = ""

    @SerializedName("redeemed_on")
    var redeemed_on: String? = ""

    @SerializedName("voucher_value")
    var voucher_value: String? = ""

    @SerializedName("payment_option")
    var payment_option: String? = ""

    @SerializedName("is_valid_type")
    var is_valid_type: Int? = 0

}