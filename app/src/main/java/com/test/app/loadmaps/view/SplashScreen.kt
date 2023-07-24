package com.test.app.loadmaps.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.test.app.R
import com.test.app.databinding.ActivitySplashScreenBinding
import com.test.app.loadmaps.viewmodel.SplashViewModelImpl


class SplashScreen: AppCompatActivity() {
    private lateinit var viewModel: SplashViewModelImpl
    private lateinit var context: Context
    private lateinit var databinding:ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context = this
        databinding = DataBindingUtil.setContentView(this,R.layout.activity_splash_screen)
        viewModel = ViewModelProvider(this).get(SplashViewModelImpl::class.java)
        databinding.splashViewModel = viewModel
        databinding.lifecycleOwner = this

        lifecycle.addObserver(viewModel)

        viewModel.initSplashScreen()

        viewModel.splashliveData?.observe(this@SplashScreen) {
                isAccountActive ->
            if (isAccountActive) {
                redirectToHome()
            } else {
                redirectToAccount()
            }
        }

        viewModel.errorMessage?.observe(this) {
                errMsg -> showAlertDialogBox("", errMsg)
        }
    }

    override fun onStart() {
        super.onStart()
    }


    fun redirectToAccount() {
        val intent = Intent(applicationContext, AccountActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun redirectToHome() {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showAlertDialogBox(title: String, msg: String?) {
        val builder = AlertDialog.Builder(this@SplashScreen)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton(android.R.string.yes)
        { dialog, which -> dialog.dismiss() }
        builder.show()
    }
}