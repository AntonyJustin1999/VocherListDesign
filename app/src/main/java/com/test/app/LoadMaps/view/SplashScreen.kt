package com.test.app.LoadMaps.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.test.app.LoadMaps.viewmodel.CommonViewModel
import com.test.app.LoadMaps.viewmodel.CommonViewModelFactory
import com.test.app.LoadMaps.viewmodel.CommonViewModelImplementor
import com.test.app.R

class SplashScreen: AppCompatActivity() {
    private lateinit var viewModel: CommonViewModelImplementor
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        context = this
        //val factory = CommonViewModelFactory(context = this)
        viewModel = ViewModelProvider(this)[CommonViewModelImplementor::class.java]
        lifecycle.addObserver(viewModel)

        // This method is used so that your splash activity can cover the entire screen.
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        Handler().postDelayed({
            viewModel.getMutableIsAccountActive()?.observe(this@SplashScreen) {
                    isAccountActive ->
                if (isAccountActive!!) {
                    redirectToHome()
                } else {
                    redirectToAccount()
                }
            }
        }, 2000)

        viewModel.errorMessage()?.observe(
            this
        ) { errMsg -> showAlertDialogBox("", errMsg) }
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