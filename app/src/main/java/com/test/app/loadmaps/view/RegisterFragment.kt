package com.test.app.loadmaps.view

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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.test.app.loadmaps.data.db.UserData
import com.test.app.loadmaps.utils.Utils
import com.test.app.loadmaps.viewmodel.CommonViewModel
import com.test.app.loadmaps.viewmodel.CommonViewModelImplementor
import com.test.app.R
import com.test.app.databinding.FragmentLoginBinding
import com.test.app.databinding.FragmentRegisterBinding
import com.test.app.loadmaps.viewmodel.LoginViewModelImpl
import com.test.app.loadmaps.viewmodel.RegisterViewModelImpl

class RegisterFragment : Fragment {
    private var mRegisterView: View? = null
    private var context: Context? = null
    private lateinit var viewModel: RegisterViewModelImpl
    private lateinit var databinding: FragmentRegisterBinding

    constructor() {
        // Required empty public constructor
    }

    constructor(context: Context?) {
        this.context = context
    }

    // Override function when the view is being created
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflates the custom fragment layout
        databinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        mRegisterView = databinding.getRoot()
        viewModel = ViewModelProvider(this).get(RegisterViewModelImpl::class.java)
        databinding.registerViewModel = viewModel
        databinding.lifecycleOwner = viewLifecycleOwner

        lifecycle.addObserver(viewModel)

        viewModel.onRegister.observe(viewLifecycleOwner) { isRegisterSucces ->
            if (isRegisterSucces!!) {
                navigateToLoginPage()
            }
        }
        viewModel.onLoginRedirect.observe(viewLifecycleOwner){ isRedirect ->
            if (isRedirect!!) {
                navigateToLoginPage()
            }
        }
        viewModel.isProgressShow.observe(viewLifecycleOwner){ isShow ->
            if (isShow!!) {
                databinding.progressShow = true
            } else {
                databinding.progressShow = false
            }
        }
        viewModel?.errorMessage?.observe(viewLifecycleOwner
        ) { errMsg -> showAlertDialogBox("", errMsg) }

        return mRegisterView
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

}