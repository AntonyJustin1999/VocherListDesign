package com.test.app.task.repository

import androidx.lifecycle.LiveData
import com.test.app.task.data.dataSets.UserDetails
import com.test.app.task.data.dataSets.VoucherDetails
import com.test.app.task.data.db.UserVoucherDetails

interface UsersRepository {
    @Throws(Exception::class)
    fun insertVoucherList(voucherList: ArrayList<UserDetails>?): LiveData<Boolean?>?
    @Throws(Exception::class)
    fun getVoucherListData(): LiveData<ArrayList<VoucherDetails?>?>?

}
