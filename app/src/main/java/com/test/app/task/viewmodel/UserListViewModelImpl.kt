package com.test.app.task.viewmodel

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.app.task.data.api.RestManager
import com.test.app.task.data.dataSets.UserListResponseApi
import com.test.app.task.data.dataSets.UserListData
import com.test.app.task.data.dataSets.VoucherDetails
import com.test.app.task.data.db.UserVoucherDetails
import com.test.app.task.repository.UserRepositoryImpl
import com.test.app.task.utils.internet.ConnectionReceiver
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserListViewModelImpl(context: Context) : ViewModel(), DefaultLifecycleObserver,
    ConnectionReceiver.ReceiverListener {
    var userRepository: UserRepositoryImpl
    var mutableUserVoucherList: MutableLiveData<ArrayList<VoucherDetails?>?>?
    var restManager: RestManager

    var isProgressShow: MutableLiveData<Boolean?>
    var isNetwork: MutableLiveData<Boolean>
    var isUserListsShow: MutableLiveData<Boolean?>
    var userListEmptyMessage: MutableLiveData<String>
    var isDataEmpty: MutableLiveData<Boolean?>
    var errorMessage: MutableLiveData<String?>
    var isErrorExist: MutableLiveData<Boolean?>

    var mContext:Context
    var connectionReceiver:ConnectionReceiver

    init {
        userRepository = UserRepositoryImpl.instance as UserRepositoryImpl
        restManager = RestManager()
        errorMessage = MutableLiveData()
        mutableUserVoucherList = MutableLiveData()
        isProgressShow = MutableLiveData()
        isNetwork = MutableLiveData()
        isUserListsShow = MutableLiveData()
        userListEmptyMessage = MutableLiveData()
        isDataEmpty = MutableLiveData()
        isErrorExist = MutableLiveData()

        this.mContext = context
        connectionReceiver = ConnectionReceiver()
        Log.e("Test","CountryDetails Context = "+mContext)
    }
    fun loadUserList() {
        var Call: Call<UserListResponseApi?>? = null

        if (isNetworkAvailable()) {
            try {
                Call = restManager.API.getUserlist(RestManager.Authorization, 1,"dev")
                if (Call != null) {
                    isProgressShow.value = true
                    Call.enqueue(object : Callback<UserListResponseApi?> {
                        override fun onResponse(
                            call: Call<UserListResponseApi?>,
                            response: Response<UserListResponseApi?>) {
                            try {
                                val data = response.body()
                                if (data != null && data.status == 200) {
                                    if(data.data?.userList?.size!! > 0){
                                        userRepository.insertVoucherList(data.data?.userList)
                                        mutableUserVoucherList?.value = userRepository.getVoucherListData()?.value
//                                        userRepository.getVoucherListData()?.value?.forEach {
//                                            Log.e("Test","get userVoucherlist OrderNumber= ${it?.order_number}")
//                                        }
                                        isUserListsShow.value = true
                                        isNetwork.value = false
                                        isErrorExist.value = false
                                        isDataEmpty.value = false

                                    } else{
                                        userListEmptyMessage.value = data.message
                                        isDataEmpty.value = true
                                        isUserListsShow.value = false
                                    }
                                } else {
                                    isErrorExist.value = true
                                    isUserListsShow.value = false
                                    errorMessage.value = "Something Wrong Try Again!!"
                                }
                            } catch (e: Exception) {
                                isErrorExist.value = true
                                isUserListsShow.value = false
                                errorMessage.value = "Something Wrong Try Again!!"
                            } finally {
                                isProgressShow.setValue(false)
                            }
                        }

                        override fun onFailure(call: Call<UserListResponseApi?>,
                                               t: Throwable) {
                            isErrorExist.value = true
                            isUserListsShow.value = false
                            isProgressShow.value = false
                            errorMessage.value = "Something Wrong Try Again!!"
                        }
                    })
                }
            } catch (e: Throwable) {
                isErrorExist.value = true
                isUserListsShow.value = false
                errorMessage.value = "Something Wrong Try Again!!"
            }
        } else {
            isUserListsShow.value = false
            isNetwork.value = true
        }
    }

    override fun onNetworkChange(isConnected: Boolean) {
        //if(isConnected){
            loadUserList()
        //} else {
            //isCountryDetailsShow.value = false
            //isNetwork.value = true
        //}
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        mContext.registerReceiver(connectionReceiver, intentFilter)
        ConnectionReceiver.InternetListener = this
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        mContext.unregisterReceiver(connectionReceiver)
    }

    private fun isNetworkAvailable(): Boolean {
        try {
            if (mContext != null) {
                val connectivity =
                    mContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (connectivity != null) {
                    val info = connectivity.activeNetworkInfo
                    if (info != null && info.isConnected) {
                        return true
                    }
                }
            }
            return false
        } catch (e: Exception) {
            return false
        }
    }

}