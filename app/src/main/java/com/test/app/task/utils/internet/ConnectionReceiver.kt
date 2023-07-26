package com.test.app.task.utils.internet

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager


class ConnectionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        //Log.e("Test","BroadCastReceiver - onReceive Called()")
        // initialize connectivity manager
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // Initialize network info
        val networkInfo = connectivityManager.activeNetworkInfo

        // check condition
        if (InternetListener != null) {
            val isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting
            // call listener method
            InternetListener?.onNetworkChange(isConnected)
        }
    }

    interface ReceiverListener {
        // create method
        fun onNetworkChange(isConnected: Boolean)
    }

    companion object {
        // initialize listener
        var InternetListener: ReceiverListener? = null
    }
}