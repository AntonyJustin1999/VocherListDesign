package com.test.app.task.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UserListViewModelFactory(val context:Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UserListViewModelImpl::class.java)){
            return UserListViewModelImpl(context) as T
        }
        throw IllegalArgumentException ("UnknownViewModel")
    }
}