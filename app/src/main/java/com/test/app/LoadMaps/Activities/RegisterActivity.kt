package com.test.app.LoadMaps.Activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.test.app.LoadMaps.DataSets.UsersData
import com.test.app.LoadMaps.Databases.DBHelper
import com.test.app.LoadMaps.Fragments.TestFragment
import com.test.app.LoadMaps.Fragments.TestFragment1
import com.test.app.LoadMaps.Utils.UtilClass
import com.test.app.R
import io.realm.Realm
import java.security.AccessController.getContext


class RegisterActivity : AppCompatActivity() {
    var etUserName:EditText? = null
    var etEmail:EditText? = null
    var et_mobile_number:EditText? = null
    var etPassword:EditText? = null
    var et_con_passwrod:EditText? = null
    var tv_login:TextView? = null
    var btnRegister: Button? = null
    var progress_bar: LottieAnimationView? = null
    var context: Context? = null

    var IsPwdShow:Boolean = false
    var IsPwdShowCon:Boolean = false
    var iv_pwd_eye: ImageView? = null
    var iv_pwd_eye_con: ImageView? = null

    private var realm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(false)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        context = this


        realm = Realm.getDefaultInstance();

        etUserName = findViewById(R.id.et_username)
        etEmail = findViewById(R.id.et_email)
        et_mobile_number = findViewById(R.id.et_mobile_number)
        etPassword = findViewById(R.id.et_password)
        et_con_passwrod = findViewById(R.id.et_con_passwrod)
        tv_login = findViewById(R.id.tv_login)
        btnRegister = findViewById(R.id.btn_register)
        progress_bar = findViewById(R.id.progress_bar)

        context = this

        //showAlertDialog("test title","test message")

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


        iv_pwd_eye_con = findViewById(R.id.iv_password_eye1)

        iv_pwd_eye_con?.setOnClickListener {
            if(IsPwdShowCon){
                iv_pwd_eye_con?.setImageResource(R.drawable.ic_eye_off)
                et_con_passwrod?.transformationMethod = PasswordTransformationMethod.getInstance()
                et_con_passwrod?.setSelection(et_con_passwrod?.length()!!)
                IsPwdShowCon = !IsPwdShowCon
            } else {
                iv_pwd_eye_con?.setImageResource(R.drawable.ic_eye)
                et_con_passwrod?.transformationMethod = HideReturnsTransformationMethod.getInstance()
                et_con_passwrod?.setSelection(et_con_passwrod?.length()!!)
                IsPwdShowCon = !IsPwdShowCon
            }
        }

        if(IsPwdShowCon){
            iv_pwd_eye_con?.setImageResource(R.drawable.ic_eye)
            et_con_passwrod?.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            iv_pwd_eye_con?.setImageResource(R.drawable.ic_eye_off)
            et_con_passwrod?.transformationMethod = PasswordTransformationMethod.getInstance()
        }


        btnRegister?.setOnClickListener {
            //Test
            Log.e("Test","etName len = "+etUserName?.text.toString().trim().length);
            if(etUserName?.text.toString().trim().length>0){
                if(et_mobile_number?.text.toString().trim().length>0){
                    if(etEmail?.text.toString().trim().length>0){
                        if(UtilClass.AppFunctions.isValidEmail(etEmail?.text.toString())){
                            if(etPassword?.text.toString().trim().length>0){
                                if(et_con_passwrod?.text.toString().trim().length>0){
                                    if(etPassword?.text.toString().trim().equals(et_con_passwrod?.text.toString().trim())){
                                        progress_bar?.visibility = View.VISIBLE

                                        var registerData:UsersData = UsersData()
                                        registerData.userName = etUserName?.text.toString()
                                        registerData.mobileNumber = et_mobile_number?.text.toString()
                                        registerData.emailId = etEmail?.text.toString()
                                        registerData.password = etPassword?.text.toString()
                                        registerData.isCurrentUser = false
                                        if(RegisterData(registerData)){
                                            progress_bar?.visibility = View.GONE
                                            //storelogindata(registerData.userName!!)
                                            val intent = Intent(this, LoginActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            progress_bar?.visibility = View.GONE
                                        }
                                    } else {
                                        showAlertDialog("","Password and Confirm Password Must be Same")
                                    }
                                } else {
                                    showAlertDialog("","Please enter Confirm Password")
                                }
                            } else {
                                showAlertDialog("","Please enter Password")
                            }
                        } else {
                            showAlertDialog("","Please enter valid eMail Id")
                        }
                    } else {
                        showAlertDialog("","Please enter eMail Id")
                    }
                } else {
                    showAlertDialog("","Please enter MobileNumber")
                }
            } else {
                showAlertDialog("","Please enter user name")
            }
        }

        tv_login=findViewById(R.id.tv_login)
        tv_login?.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
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
        builder.setPositiveButton(android.R.string.yes) { dialog, which -> }
        //builder.setNegativeButton(android.R.string.no) { dialog, which -> }
        builder.show()
    }

    fun storelogindata(loginusername:String){
        val sharedPreference =  getSharedPreferences("Login", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putString("loginUserName",loginusername)
        editor.commit()
    }

    fun RegisterData(registerData:UsersData):Boolean{

        Log.e("Test","RegisterData -  Called()")

        if(!findUserNameExists(registerData.userName!!)){
            Log.e("Test","RegisterData - findUserNot Exists")
            var userData = UsersData()
            var id = realm!!.where(UsersData::class.java).max("id")
            val nextId: Long

            nextId = if (id == null) {
                // if id is null
                // we are passing it as 1.
                1
            } else {
                // if id is not null then
                // we are incrementing it by 1
                (id.toInt() + 1).toLong()
            }
            userData.id = nextId
            userData.userName = registerData.userName
            userData.mobileNumber = registerData.mobileNumber
            userData.emailId = registerData.emailId
            userData.password = registerData.password
            userData.isCurrentUser = false

            realm?.executeTransaction { realm -> // inside on execute method we are calling a method
                // to copy to real m database from our modal class.
                realm.copyToRealm(userData)
                Log.e("Test","RegisterData - realm.executeTransaction Called")
            }
            Log.e("Test","RegisterData - return true called")
            return true
        } else {
            Log.e("Test","RegisterData - UserName Already Exists")
            showAlertDialog("","UserName Already Exists")
            return false
        }
    }

    fun findUserNameExists(UserName:String):Boolean {
        var isExists = false
        var dataModals = realm?.where(UsersData::class.java)?.equalTo("userName",UserName)?.findAll()

        for(i in 0 until dataModals?.size!!){
            isExists = true
        }
        return isExists
    }


}