package com.test.app.LoadMaps.Utils

import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast

class UtilClass() {
    lateinit var context:Context

    fun Init(context: Context){
        this.context = context
    }

    object AppFunctions{

        var vlaue:UtilClass = UtilClass()
        var context:Context?=null

        fun isValidEmail(target: CharSequence?): Boolean {
            return if (TextUtils.isEmpty(target)) {
                false
            } else {
                Patterns.EMAIL_ADDRESS.matcher(target).matches()
            }
        }


    }

}