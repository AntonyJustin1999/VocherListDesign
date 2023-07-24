package com.test.app.loadmaps.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.test.app.R
import com.test.app.databinding.FragmentLoginBinding
import com.test.app.loadmaps.viewmodel.LoginViewModelImpl


class LoginFragment(context: Context?):Fragment() {
    private var mLoginView: View? = null
    private var context: Context? = null
    private lateinit var viewModel: LoginViewModelImpl
    private lateinit var databinding: FragmentLoginBinding

    init {
        this.context = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflates the custom fragment layout
        //mLoginView = inflater.inflate(R.layout.fragment_login, container, false)
        val bundle = arguments
        if (bundle != null) {
            val message = bundle.getString("mText")
        }

        //databinding = DataBindingUtil.setContentView(requireActivity(),R.layout.fragment_login)
        databinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        mLoginView = databinding.getRoot()
        viewModel = ViewModelProvider(this).get(LoginViewModelImpl::class.java)
        databinding.loginViewModel = viewModel
        databinding.lifecycleOwner = viewLifecycleOwner

        lifecycle.addObserver(viewModel)

        viewModel.onLogin.observe(viewLifecycleOwner) { isLogin ->
            if (isLogin!!) {
                navigateHomeActivity()
            } else {
                showError("Invalid UserName and Password")
            }
        }
        viewModel.onRegisterRedirect.observe(viewLifecycleOwner){ isRedirect ->
            if (isRedirect!!) {
                navigateRegisterPage()
            }
        }
        viewModel.isProgressShow?.observe(viewLifecycleOwner) { isShow ->
            Log.e("Test","Login Fragment - isProgressShow Observer isShow = "+isShow);
            if (isShow!!) {
                databinding.progressShow = true
            } else {
                databinding.progressShow = false
            }
        }
        viewModel.errorMessage?.observe(viewLifecycleOwner) {
                errMsg -> showAlertDialogBox("", errMsg) }

        return mLoginView
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