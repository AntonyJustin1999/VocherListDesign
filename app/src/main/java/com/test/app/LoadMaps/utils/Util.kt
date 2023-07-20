package com.test.app.LoadMaps.utils

import android.text.TextUtils
import android.util.Patterns


object Utils {
    fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }
}