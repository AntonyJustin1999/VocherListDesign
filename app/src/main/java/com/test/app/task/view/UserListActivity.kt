package com.test.app.task.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.test.app.R
import com.test.app.databinding.UserListActivityBinding
import com.test.app.task.view.adapter.UserListAdapter
import com.test.app.task.viewmodel.UserListViewModelFactory
import com.test.app.task.viewmodel.UserListViewModelImpl

class UserListActivity: AppCompatActivity() {
    private var context: Context? = null
    private lateinit var viewModel: UserListViewModelImpl
    private lateinit var databinding: UserListActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(false)
        supportActionBar?.apply {
            title = getString(R.string.text_vouchers)
        }
        context = this

        databinding = DataBindingUtil.setContentView(this,R.layout.user_list_activity)
        val factory = UserListViewModelFactory(this)
        viewModel = ViewModelProvider(this,factory).get(UserListViewModelImpl::class.java)
        databinding.userlistViewModel = viewModel
        databinding.lifecycleOwner = this
        lifecycle.addObserver(viewModel)

        viewModel?.mutableUserVoucherList?.observe(this) { uservoucherlist ->
            val voucherListRecyclerViewAdapter = UserListAdapter(uservoucherlist, context)
            databinding.setMyAdapter(voucherListRecyclerViewAdapter)
        }

        //viewModel?.loadUserList()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onBack() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }
    override fun onBackPressed() {
        onBack()
    }

}
