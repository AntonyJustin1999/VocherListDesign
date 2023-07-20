package com.test.app.LoadMaps.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.test.app.LoadMaps.viewmodel.CommonViewModel
import com.test.app.LoadMaps.viewmodel.CommonViewModelImplementor
import com.test.app.R

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var context: Context? = null
    var navigationView: NavigationView? = null
    var headerView: View? = null
    private var viewModel: CommonViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        context = this
        viewModel = ViewModelProvider(this)[CommonViewModelImplementor::class.java]
        lifecycle.addObserver(viewModel!!)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView?.setNavigationItemSelectedListener(this)
        headerView = navigationView?.getHeaderView(0)
        navigateToCountryListPage()
        viewModel?.onLogOut()?.observe(
            this
        ) { isLogOutProcessSuccess ->
            if (isLogOutProcessSuccess!!) {
                try {
                    navigateToLoginPage()
                } catch (e: Exception) {
                    showAlertDialogBox("", e.message)
                }
            }
        }
        viewModel!!.errorMessage()?.observe(
            this
        ) { errMsg -> showAlertDialogBox("", errMsg) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.nav_home) {
            navigateToReloadPage()
        } else if (id == R.id.nav_logout) {
            viewModel!!.OnLogOut()
        }
        return false
    }

    fun navigateToCountryListPage() {
        val mFragmentManager = supportFragmentManager
        val mFragmentTransaction = mFragmentManager.beginTransaction()
        val mFragment = CountryListFragment(this)
        val mBundle = Bundle()
        mBundle.putString("mText", "TestData")
        mFragment.arguments = mBundle
        mFragmentTransaction.replace(R.id.layout_home_container, mFragment)
            .addToBackStack("CountryListFragment")
        mFragmentTransaction.commitAllowingStateLoss()
    }

    fun navigateToReloadPage() {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun navigateToLoginPage() {
        val intent = Intent(applicationContext, AccountActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showAlertDialogBox(title: String, msg: String?) {
        val builder = AlertDialog.Builder(applicationContext)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton(
            android.R.string.yes
        ) { dialog, which -> dialog.dismiss() }
        builder.show()
    }
}