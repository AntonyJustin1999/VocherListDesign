package com.test.app.loadmaps.view

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.test.app.R

class AccountActivity : AppCompatActivity() {
    //private lateinit var  toolbar:Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_activity)
        //toolbar = findViewById<Toolbar>(R.id.toolbar)
        //setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        navigateToLoginPage()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        Log.e("Test", "onOptionsItemSelected - ItemId = "+id +" Home = "+R.id.home)
        if (id == R.id.home) {
            Log.e("Test", "onOptionsItemSelected - home Clicked")
            if (supportFragmentManager.backStackEntryCount > 1) {
                supportFragmentManager.popBackStack()
            } else {
                finish()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        Log.e("Test", "onBackPressed - Called()")
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    fun navigateToLoginPage() {
        supportActionBar!!.title = "Account"
        val mLoginFragment = LoginFragment(applicationContext)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        //fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.layout_container, mLoginFragment, "mLoginFragment")
            .addToBackStack("mLoginFragment")
        fragmentTransaction.commitAllowingStateLoss()
    }

    fun showError(ErrorMsg: String) {
        showAlertDialogBox("", ErrorMsg)
    }

    private fun showAlertDialogBox(title: String, msg: String) {
        val builder = AlertDialog.Builder(applicationContext)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton(
            android.R.string.yes
        ) { dialog, which -> dialog.dismiss() }
        builder.show()
    }
}