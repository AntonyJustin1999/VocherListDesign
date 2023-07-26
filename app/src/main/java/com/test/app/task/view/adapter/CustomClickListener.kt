package com.test.app.task.view.adapter

import com.test.app.task.data.dataSets.UserDetails
import com.test.app.task.data.dataSets.VoucherDetails
import com.test.app.task.data.db.UserVoucherDetails

interface CustomClickListener {
    fun itemClicked(f: VoucherDetails?)
    fun btnClicked(f: VoucherDetails?)

}