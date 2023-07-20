package com.test.app.LoadMaps.view

import android.content.Context
import android.content.Intent
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
import com.test.app.LoadMaps.viewmodel.CommonViewModel
import com.test.app.LoadMaps.viewmodel.CommonViewModelImplementor
import com.test.app.R

class LoginFragment(context: Context?):Fragment() {
    private var mLoginView: View? = null
    private var etUserName: EditText? = null
    private var etPassword:EditText? = null
    private var IsPwdShow = false
    private var iv_pwd_eye: ImageView? = null
    private var btnLogin: Button? = null
    private var tv_register: TextView? = null
    private var progress_bar: LottieAnimationView? = null
    private var context: Context? = null
    private lateinit var viewModel: CommonViewModel

    init {
        this.context = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflates the custom fragment layout
        mLoginView = inflater.inflate(R.layout.fragment_login, container, false)
        val bundle = arguments
        if (bundle != null) {
            val message = bundle.getString("mText")
        }
        viewModel = ViewModelProvider(this)[CommonViewModelImplementor::class.java]
        lifecycle.addObserver(viewModel)
        etUserName = mLoginView!!.findViewById<EditText>(R.id.et_userName)
        etPassword = mLoginView!!.findViewById<EditText>(R.id.et_password)
        btnLogin = mLoginView!!.findViewById<Button>(R.id.btn_login)
        progress_bar = mLoginView!!.findViewById<LottieAnimationView>(R.id.progress_bar)
        iv_pwd_eye = mLoginView!!.findViewById<ImageView>(R.id.iv_password_eye)
        iv_pwd_eye?.setOnClickListener(View.OnClickListener {
            if (IsPwdShow) {
                showeye()
            } else {
                hideeye()
            }
        })
        if (IsPwdShow) {
            iv_pwd_eye?.setImageResource(R.drawable.ic_eye)
            etPassword?.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
        } else {
            iv_pwd_eye?.setImageResource(R.drawable.ic_eye_off)
            etPassword?.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
        }
        btnLogin?.setOnClickListener(View.OnClickListener {
            onLoggedIn(
                etUserName?.getText().toString().trim { it <= ' ' },
                etPassword?.getText().toString().trim { it <= ' ' })
        })
        tv_register = mLoginView!!.findViewById<TextView>(R.id.tv_register)
        tv_register?.setOnClickListener(View.OnClickListener { navigateRegisterPage() })
        viewModel.loginValidation()?.observe(viewLifecycleOwner) { isLogin ->
            if (isLogin!!) {
                hideProgress()
                //viewModel.LoggedInUser(etUserName.getText().toString().trim());
                navigateHomeActivity()
            } else {
                showError("Invalid UserName and Password")
                hideProgress()
            }
        }
        viewModel.errorMessage()?.observe(
            viewLifecycleOwner
        ) { errMsg -> showAlertDialogBox("", errMsg) }
        return mLoginView
    }

    fun onLoggedIn(username: String, password: String) {
        if (username.length > 0) {
            if (password.length > 0) {
                showProgress()
                try {
                    viewModel.LoginAuthentication(username, password)
                } catch (e: Exception) {
                    showError(e.message)
                }
            } else {
                showError("Please enter Password")
            }
        } else {
            showError("Please enter eMail Id")
        }
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

    fun navigateHomeActivity() {
        val intent = Intent(getContext(), HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        activity?.finish()
    }

    fun navigateRegisterPage() {
        val mFragmentManager = parentFragmentManager
        val mFragmentTransaction = mFragmentManager.beginTransaction()
        val mFragment = RegisterFragment(context)
        val mBundle = Bundle()
        mBundle.putString("mText", "TestData")
        mFragment.arguments = mBundle
        mFragmentTransaction.replace(R.id.layout_container, mFragment)
            .addToBackStack("RegisterFragment")
        mFragmentTransaction.commitAllowingStateLoss()
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

    private fun showAlertDialogBox(title: String, msg: String?) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton(
            android.R.string.yes
        ) { dialog, which -> dialog.dismiss() }
        builder.show()
    }

}