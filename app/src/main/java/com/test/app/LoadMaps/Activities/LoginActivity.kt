package com.test.app.LoadMaps.Activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.airbnb.lottie.LottieAnimationView
import com.test.app.LoadMaps.DataSets.UsersData
import com.test.app.LoadMaps.Databases.DBHelper
import com.test.app.R
import io.realm.Realm


class LoginActivity : AppCompatActivity() {
    var etUserName: EditText? = null
    var etPassword: EditText? = null
    var btnLogin: Button? = null
    var tv_register: TextView? = null
    var progress_bar: LottieAnimationView? = null
    var context: Context? = null
    private var dbHandler: DBHelper? = null

    var IsPwdShow:Boolean = false
    var iv_pwd_eye: ImageView? = null

    private var realm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(false)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        context = this


        realm = Realm.getDefaultInstance();

        etUserName = findViewById(R.id.et_userName)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        progress_bar = findViewById(R.id.progress_bar)

        context = this

        iv_pwd_eye = findViewById(R.id.iv_password_eye)

        iv_pwd_eye?.setOnClickListener {
            if(IsPwdShow){
                iv_pwd_eye?.setImageResource(R.drawable.ic_eye_off)
                etPassword?.transformationMethod = PasswordTransformationMethod.getInstance()
                etPassword?.setSelection(etPassword?.length()!!)
                IsPwdShow = !IsPwdShow
            } else {
                iv_pwd_eye?.setImageResource(R.drawable.ic_eye)
                etPassword?.transformationMethod = HideReturnsTransformationMethod.getInstance()
                etPassword?.setSelection(etPassword?.length()!!)
                IsPwdShow = !IsPwdShow
            }
        }

        if(IsPwdShow){
            iv_pwd_eye?.setImageResource(R.drawable.ic_eye)
            etPassword?.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            iv_pwd_eye?.setImageResource(R.drawable.ic_eye_off)
            etPassword?.transformationMethod = PasswordTransformationMethod.getInstance()
        }

        btnLogin?.setOnClickListener {

            if(etUserName?.text.toString().trim().length>0){
                if(etPassword?.text.toString().trim().length>0){

                    progress_bar?.visibility = View.VISIBLE
                    if(loginCredentials(etUserName?.text.toString(),etPassword?.text.toString())){
                        progress_bar?.visibility = View.GONE
                        loginUser(etUserName?.text.toString())
                        val intent = Intent(this, HomeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    } else {
                        showAlertDialog("","Invalid UserName and Password")
                        progress_bar?.visibility = View.GONE
                    }

                } else {
                    showAlertDialog("","Please enter Password")
                }
            } else {
                showAlertDialog("","Please enter eMail Id")
            }
        }
        tv_register = findViewById(R.id.tv_register)
        tv_register?.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onResume() {
        super.onResume()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //menuInflater.inflate(R.menu.cancel_order, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
//        if (id == R.id.m_cancel_order) {
//            Log.e("Test","onOptionsItemSelected - Cancel Order Clicked");
//            return true
//        }
        if (id == android.R.id.home) {
            Log.e("Test","onOptionsItemSelected - home Clicked");
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        Log.e("Test","onBackPressed - Called()");
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    fun showAlertDialog(title:String,msg:String){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(msg)
        //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            dialog.dismiss()
        }

//        builder.setNegativeButton(android.R.string.no) { dialog, which ->
//
//        }

//        builder.setNeutralButton("Maybe") { dialog, which ->
//            Toast.makeText(context,
//                "Maybe", Toast.LENGTH_SHORT).show()
//        }
        builder.show()
    }

    fun storelogindata(loginusername:String,loginemailId:String){
        val sharedPreference =  getSharedPreferences("Login", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putString("loginUserName",loginusername)
        editor.putString("loginEmailID",loginemailId)
        editor.commit()
    }

    fun loginCredentials(UserName:String,Password:String):Boolean {
        var isExists = false
        var dataModals = realm?.where(UsersData::class.java)?.equalTo("userName",UserName)?.findAll()

        for(i in 0 until dataModals?.size!!){
            if(Password.equals(dataModals?.get(i)?.password)){
                isExists = true
            }
        }
        return isExists
    }
    fun readlogindata(userName:String): UsersData? {

        val loginDetails: UsersData? = realm?.where(UsersData::class.java)?.equalTo("userName", userName)?.findFirst()

        return loginDetails

    }

    fun loginUser(UserName:String){

        var loginDetails =  readlogindata(UserName)
        val modal: UsersData? = realm?.where(UsersData::class.java)?.equalTo("userName", UserName)?.findFirst()
        realm?.executeTransaction { realm -> // on below line we are setting data to our modal class
            // which we get from our edit text fields.
            modal?.userName = loginDetails?.userName
            modal?.password = loginDetails?.password
            modal?.emailId = loginDetails?.emailId
            modal?.mobileNumber = loginDetails?.mobileNumber
            modal?.isCurrentUser = true

            // inside on execute method we are calling a method to copy
            // and update to real m database from our modal class.
            realm.copyToRealmOrUpdate(modal)
        }
    }


}