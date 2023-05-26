package com.test.app.LoadMaps.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.test.app.LoadMaps.DataSets.UsersData
import com.test.app.LoadMaps.Databases.DBHelper
import com.test.app.R
import io.realm.Realm


class SplashScreen : AppCompatActivity() {

    var context: Context? = null

    private var realm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        context = this

        realm = Realm.getDefaultInstance();

        getSupportActionBar()?.hide();

        // This method is used so that your splash activity can cover the entire screen.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed(Runnable {
            //Test
            Log.e("Test","isLogin() "+isLogin());
            if(IsloginAccount()){
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 2000)

    }

    fun isLogin():String?{
        var str:String? = ""
        val sharedPreference =  getSharedPreferences("Login", Context.MODE_PRIVATE)
        str = sharedPreference.getString("loginUserName","")
        return str;
    }

    fun IsloginAccount():Boolean {
        var isExists = false
        var dataModals = realm?.where(UsersData::class.java)?.equalTo("isCurrentUser",true)?.findAll()

        for(i in 0 until dataModals?.size!!){
                isExists = true
        }
        return isExists
    }
}