package com.test.app.LoadMaps.view

import android.content.Context
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.test.app.LoadMaps.data.db.UserData
import com.test.app.LoadMaps.utils.Utils
import com.test.app.LoadMaps.viewmodel.CommonViewModel
import com.test.app.LoadMaps.viewmodel.CommonViewModelImplementor
import com.test.app.R

class RegisterFragment : Fragment {
    private var mRegisterView: View? = null
    private var etUserName: EditText? = null
    private var etPassword: EditText? = null
    private var et_mobile_number: EditText? = null
    private var et_con_password: EditText? = null
    private var etEmail: EditText? = null
    private var IsPwdShow = false
    private var IsPwdShowCon = false
    private var iv_pwd_eye: ImageView? = null
    private var iv_pwd_eye_con: ImageView? = null
    private var btnRegister: Button? = null
    private var tv_login: TextView? = null
    private var progress_bar: LottieAnimationView? = null
    private var context: Context? = null
    private var viewModel: CommonViewModel? = null

    constructor() {
        // Required empty public constructor
    }

    constructor(context: Context?) {
        this.context = context
    }

    // Override function when the view is being created
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflates the custom fragment layout
        mRegisterView = inflater.inflate(R.layout.fragment_register, container, false)
        val bundle = arguments
        val message = bundle!!.getString("mText")
        viewModel = ViewModelProvider(this)[CommonViewModelImplementor::class.java]
        lifecycle.addObserver(viewModel!!)
        etUserName = mRegisterView!!.findViewById<EditText>(R.id.et_username)
        etEmail = mRegisterView!!.findViewById<EditText>(R.id.et_email)
        et_mobile_number = mRegisterView!!.findViewById<EditText>(R.id.et_mobile_number)
        etPassword = mRegisterView!!.findViewById<EditText>(R.id.et_password)
        et_con_password = mRegisterView!!.findViewById<EditText>(R.id.et_con_passwrod)
        tv_login = mRegisterView!!.findViewById<TextView>(R.id.tv_login)
        btnRegister = mRegisterView!!.findViewById<Button>(R.id.btn_register)
        progress_bar = mRegisterView!!.findViewById<LottieAnimationView>(R.id.progress_bar)
        iv_pwd_eye = mRegisterView!!.findViewById<ImageView>(R.id.iv_password_eye)
        iv_pwd_eye?.setOnClickListener(View.OnClickListener {
            if (IsPwdShow) {
                hideeye()
            } else {
                showeye()
            }
        })
        if (IsPwdShow) {
            iv_pwd_eye?.setImageResource(R.drawable.ic_eye)
            etPassword?.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
        } else {
            iv_pwd_eye?.setImageResource(R.drawable.ic_eye_off)
            etPassword?.setTransformationMethod(PasswordTransformationMethod.getInstance())
        }
        iv_pwd_eye_con = mRegisterView!!.findViewById<ImageView>(R.id.iv_password_eye1)
        iv_pwd_eye_con?.setOnClickListener(View.OnClickListener {
            if (IsPwdShowCon) {
                hideconeye()
            } else {
                showconeye()
            }
        })
        if (IsPwdShowCon) {
            iv_pwd_eye_con?.setImageResource(R.drawable.ic_eye)
            et_con_password?.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
        } else {
            iv_pwd_eye_con?.setImageResource(R.drawable.ic_eye_off)
            et_con_password?.setTransformationMethod(PasswordTransformationMethod.getInstance())
        }
        btnRegister?.setOnClickListener(View.OnClickListener {
            onRegistration(
                etUserName?.getText().toString().trim { it <= ' ' },
                etPassword?.getText().toString().trim { it <= ' ' },
                et_con_password?.getText().toString().trim { it <= ' ' },
                et_mobile_number?.getText().toString().trim { it <= ' ' },
                etEmail?.getText().toString().trim { it <= ' ' })
        })
        tv_login = mRegisterView!!.findViewById<TextView>(R.id.tv_login)
        tv_login?.setOnClickListener(View.OnClickListener { navigateToLoginPage() })
        viewModel?.registrationSuccess()?.observe(
            viewLifecycleOwner
        ) { isRegisterSuccess ->
            try {
                if (!isRegisterSuccess!!) {
                    hideProgress()
                    navigateToLoginPage()
                } else {
                    hideProgress()
                    showError("UserName Already Exists")
                }
            } catch (e: Exception) {
                showError(e.message)
            }
        }
        viewModel?.errorMessage()?.observe(
            viewLifecycleOwner
        ) { errMsg -> showAlertDialogBox("", errMsg) }
        return mRegisterView
    }

    fun showProgress() {
        progress_bar!!.visibility = View.VISIBLE
    }

    fun hideProgress() {
        progress_bar!!.visibility = View.GONE
    }

    fun showError(ErrorMsg: String?) {
        showAlertDialogBox("", ErrorMsg)
    }

    fun onRegistration(
        username: String,
        password: String,
        con_password: String,
        mobilenum: String,
        emailId: String
    ) {
        if (username.length > 0) {
            if (mobilenum.length > 0) {
                if (emailId.length > 0) {
                    if (Utils.isValidEmail(emailId)) {
                        if (password.length > 0) {
                            if (con_password.length > 0) {
                                if (password == con_password) {
                                    showProgress()
                                    val registerData = UserData()
                                    registerData.userName = username
                                    registerData.mobileNumber = mobilenum
                                    registerData.emailId = emailId
                                    registerData.password = password
                                    registerData.isCurrentUser = false
                                    viewModel?.RegisterAccount(registerData)
                                } else {
                                    showError("Password and Confirm Password Must be Same")
                                }
                            } else {
                                showError("Please enter Confirm Password")
                            }
                        } else {
                            showError("Please enter Password")
                        }
                    } else {
                        showError("Please enter valid eMail Id")
                    }
                } else {
                    showError("Please enter eMail Id")
                }
            } else {
                showError("Please enter MobileNumber")
            }
        } else {
            showError("Please enter user name")
        }
    }

    fun navigateToLoginPage() {
        val mFragmentManager = parentFragmentManager
        val mFragmentTransaction = mFragmentManager.beginTransaction()
        val mFragment = LoginFragment(getContext())
        val mBundle = Bundle()
        mBundle.putString("mText", "TestData")
        mFragment.arguments = mBundle
        mFragmentTransaction.replace(R.id.layout_container, mFragment)
            .addToBackStack("LoginFragment")
        mFragmentTransaction.commitAllowingStateLoss()
    }

    fun showAlertDialogBox(title: String?, msg: String?) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton(
            android.R.string.yes
        ) { dialog, which -> dialog.dismiss() }
        builder.show()
    }

    fun showeye() {
        iv_pwd_eye!!.setImageResource(R.drawable.ic_eye)
        etPassword!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
        etPassword!!.setSelection(etPassword!!.length())
        IsPwdShow = !IsPwdShow
    }

    fun hideeye() {
        iv_pwd_eye!!.setImageResource(R.drawable.ic_eye_off)
        etPassword!!.transformationMethod = PasswordTransformationMethod.getInstance()
        etPassword!!.setSelection(etPassword!!.length())
        IsPwdShow = !IsPwdShow
    }

    fun showconeye() {
        iv_pwd_eye_con!!.setImageResource(R.drawable.ic_eye)
        et_con_password!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
        et_con_password!!.setSelection(et_con_password!!.length())
        IsPwdShowCon = !IsPwdShowCon
    }

    fun hideconeye() {
        iv_pwd_eye_con!!.setImageResource(R.drawable.ic_eye_off)
        et_con_password!!.transformationMethod = PasswordTransformationMethod.getInstance()
        et_con_password!!.setSelection(et_con_password!!.length())
        IsPwdShowCon = !IsPwdShowCon
    }
}