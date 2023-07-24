package com.test.app.loadmaps.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.test.app.R
import com.test.app.databinding.ActivitySplashScreenBinding
import com.test.app.databinding.CountryDetailActivityBinding
import com.test.app.loadmaps.data.dataSets.CountryDetailsApi
import com.test.app.loadmaps.viewmodel.CountryDetailsViewModelFactory
import com.test.app.loadmaps.viewmodel.CountryDetailsViewModelImpl
import com.test.app.loadmaps.viewmodel.SplashViewModelImpl

class CountryDetailsActivity: AppCompatActivity() {
    private var context: Context? = null
    private var countryName: String? = ""
    private lateinit var viewModel: CountryDetailsViewModelImpl
    private lateinit var databinding: CountryDetailActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //val toolbar = findViewById<Toolbar>(R.id.toolbar)
        //setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(false)
        val intent = intent
        countryName = intent.getStringExtra("CountryName")
        context = this

        databinding = DataBindingUtil.setContentView(this,R.layout.country_detail_activity)
        val factory = CountryDetailsViewModelFactory(this)
        viewModel = ViewModelProvider(this,factory).get(CountryDetailsViewModelImpl::class.java)
        databinding.countryDetailsViewModel = viewModel
        databinding.lifecycleOwner = this
        lifecycle.addObserver(viewModel)


//        viewModel?.isCountryDetailsShow?.observe(this) { isCountryDetailsShow ->
//            databinding.contentShow = isCountryDetailsShow
//        }

//        viewModel?.errorMessage?.observe(this) { errMsg ->
//            ShowAlertDialog("", errMsg)
//        }

//        viewModel?.countryListEmptyMessage?.observe(this) { msg ->
//            ShowAlertDialog("",msg) }

//        viewModel?.onNetwork?.observe(this) { isNetwork ->
//            if (isNetwork!!) {
//                ShowAlertDialog("","NetworkNotAvailable")
//            }
//        }

        viewModel?.loadCountryDetails(countryName!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.home) {
            finish()
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

    fun ShowAlertDialog(title: String?, msg: String?) {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton(android.R.string.yes) {
                dialog, which -> dialog.dismiss() }
        builder.show()
    }


}
