package com.test.app.task.data.dataSets

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class VoucherDetails {
    var id: Long = 0
    var branch_name: String? = null
    var branch_image_path: String? = null
    var landmark: String? = null
    var voucher_title: String? = null
    var purchased_on:String? = null
    var valid_on:String? = null
    var order_number:String? = null
    var price:String? = null
    var voucher_status:Int? = 0
    var voucher_status_label:String? = null
    var prepaid_voucher_code:String? = null
    var redeemed_on:String? = null
    var voucher_value:String? = null
    var payment_option:String? = null
    var is_valid_type:Int? = 0
}