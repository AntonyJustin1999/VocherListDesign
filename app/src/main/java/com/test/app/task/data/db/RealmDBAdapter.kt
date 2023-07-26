package com.test.app.task.data.db

import android.content.Context
import android.util.Log
import com.test.app.task.data.dataSets.UserDetails
import com.test.app.task.data.dataSets.VoucherDetails
import io.realm.Realm
import io.realm.mongodb.User

class RealmDBAdapter private constructor(private val context: Context) {
    private val realm: Realm

    init {
        realm = Realm.getDefaultInstance()
    }

    @Throws(Exception::class)
    fun InsertVoucherData(voucherData: UserVoucherDetails): Unit {
        return try {
                val id = realm.where(UserVoucherDetails::class.java).max("id")
                val nextId: Long
                nextId = if (id == null) {
                    java.lang.Long.valueOf(1)
                } else {
                    (id.toInt() + 1).toLong()
                }
                voucherData.id = nextId

                realm.executeTransaction { realm -> // inside on execute method we are calling a method
                    // to copy to real m database from our modal class.
                    realm.copyToRealm(voucherData)
                    Log.e("Test","userDetails order_number = ${voucherData.order_number}" +
                            " purchased_on = ${voucherData.landmark} purchased_on = ${voucherData.landmark}")
                }
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    @Throws(Exception::class)
    fun findOrderNumberExists(order_number: String?): Boolean {
        return try {
            var isExists = false
            val dataModals = realm.where(UserVoucherDetails::class.java)
                .equalTo("order_number", order_number).findAll()
            if (dataModals.size > 0) {
                isExists = true
            }
            isExists
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    @Throws(Exception::class)
    fun getVoucherList(): ArrayList<VoucherDetails?> {
        var userVoucherList:ArrayList<VoucherDetails?> = ArrayList<VoucherDetails?>()
        return try {
            val voucherList = realm.where(UserVoucherDetails::class.java).findAll()
            voucherList.forEach { userVoucherDetails ->
                var voucherDetails = VoucherDetails()
                voucherDetails.branch_name = userVoucherDetails.branch_name
                voucherDetails.voucher_status = userVoucherDetails.voucher_status
                voucherDetails.voucher_title = userVoucherDetails.voucher_title
                voucherDetails.voucher_value = userVoucherDetails.voucher_value
                voucherDetails.branch_image_path = userVoucherDetails.branch_image_path
                voucherDetails.is_valid_type = userVoucherDetails.is_valid_type
                voucherDetails.landmark = userVoucherDetails.landmark
                voucherDetails.order_number = userVoucherDetails.order_number
                voucherDetails.payment_option = userVoucherDetails.payment_option
                voucherDetails.prepaid_voucher_code = userVoucherDetails.prepaid_voucher_code
                voucherDetails.price = userVoucherDetails.price
                voucherDetails.purchased_on = userVoucherDetails.purchased_on
                voucherDetails.redeemed_on = userVoucherDetails.redeemed_on
                voucherDetails.valid_on = userVoucherDetails.valid_on
                voucherDetails.voucher_status_label = userVoucherDetails.voucher_status_label
                userVoucherList.add(voucherDetails)
            }
            return userVoucherList
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
