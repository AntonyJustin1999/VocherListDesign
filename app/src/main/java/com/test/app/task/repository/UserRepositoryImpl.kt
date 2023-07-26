package com.test.app.task.repository

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.test.app.task.data.dataSets.UserDetails
import com.test.app.task.data.dataSets.VoucherDetails
import com.test.app.task.data.db.RealmDBAdapter
import com.test.app.task.data.db.RealmDBAdapter.Companion.realmDBAdapterInstance
import com.test.app.task.data.db.UserVoucherDetails

class UserRepositoryImpl() : UsersRepository {
    val insertVoucherStatus: MutableLiveData<Boolean>
    val userVoucherlist: MutableLiveData<ArrayList<VoucherDetails?>?>?
    val realmDBAdapter: RealmDBAdapter?

    init {
        realmDBAdapter = realmDBAdapterInstance
        insertVoucherStatus = MutableLiveData()
        userVoucherlist = MutableLiveData()
    }

    override fun insertVoucherList(voucherList: ArrayList<UserDetails>?): LiveData<Boolean?>? {
        try {
            voucherList?.forEach {voucherDetails ->
                if(!realmDBAdapter?.findOrderNumberExists(voucherDetails.order_number)!!){

                var voucherData: UserVoucherDetails = UserVoucherDetails()
                voucherData.branch_name = voucherDetails.branch_name
                voucherData.voucher_status = voucherDetails.voucher_status
                voucherData.voucher_title = voucherDetails.voucher_title
                voucherData.voucher_value = voucherDetails.voucher_value
                voucherData.branch_image_path = voucherDetails.branch_image_path
                voucherData.is_valid_type = voucherDetails.is_valid_type
                voucherData.landmark = voucherDetails.landmark
                voucherData.order_number = voucherDetails.order_number
                voucherData.payment_option = voucherDetails.payment_option
                voucherData.prepaid_voucher_code = voucherDetails.prepaid_voucher_code
                voucherData.price = voucherDetails.price
                voucherData.purchased_on = voucherDetails.purchased_on
                voucherData.redeemed_on = voucherDetails.redeemed_on
                voucherData.valid_on = voucherDetails.valid_on
                voucherData.voucher_status_label = voucherDetails.voucher_status_label
                realmDBAdapter?.InsertVoucherData(voucherData)

                }
            }
            insertVoucherStatus.value = true
            return insertVoucherStatus
        } catch (e:Exception){
            throw Exception(e.message)
        }
    }

    override fun getVoucherListData(): LiveData<ArrayList<VoucherDetails?>?>? {
        userVoucherlist?.value = realmDBAdapter?.getVoucherList()
        return userVoucherlist
    }

    companion object {
        var instance: UsersRepository? = null
            get() {
                if (field == null) {
                    field = UserRepositoryImpl()
                }
                return field
            }
    }
}