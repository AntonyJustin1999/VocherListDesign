package com.test.app.LoadMaps.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.test.app.LoadMaps.data.dataSets.CountriesApi
import com.test.app.LoadMaps.view.Adapter.CountiresListAdapter
import com.test.app.LoadMaps.viewmodel.CommonViewModel
import com.test.app.LoadMaps.viewmodel.CommonViewModelImplementor
import com.test.app.R

class CountryListFragment : Fragment {
    private var mCountryListView: View? = null
    var mContext: Context? = null
    var activity: Activity? = null
    private var animationLoader: LottieAnimationView? = null
    private var rv_country_list: RecyclerView? = null
    private var mRestaurantListAdapter: CountiresListAdapter? = null
    private var viewModel: CommonViewModel? = null

    constructor() {
        // Required empty public constructor
    }

    constructor(context: Context?) {
        this.mContext = context
    }

    // Override function when the view is being created
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflates the custom fragment layout
        mCountryListView = inflater.inflate(R.layout.fragment_country_list, container, false)
        val bundle = arguments
        val message = bundle!!.getString("mText")
        viewModel = ViewModelProvider(this)[CommonViewModelImplementor::class.java]
        lifecycle.addObserver(viewModel!!)
        animationLoader = mCountryListView!!.findViewById<LottieAnimationView>(R.id.progress_bar)
        rv_country_list = mCountryListView!!.findViewById<RecyclerView>(R.id.rv_restaurant_list)
        viewModel?.errorMessage()?.observe(viewLifecycleOwner) {
                errMsg -> showAlertDialogBox("", errMsg)
        }
        viewModel?.countryList()?.observe(viewLifecycleOwner) {
                countrylist -> onSuccessCountrylistLoaded(countrylist)
        }
        viewModel?.countrylistgetError()?.observe(viewLifecycleOwner) {
                msg -> onFailureCountryList(msg) }
        viewModel?.showAndHideProgress()?.observe(viewLifecycleOwner) {
                isShow ->
            if (isShow!!) {
                showProgress()
            } else {
                hideProgress()
            }
        }
        viewModel?.networkIsAvailable()?.observe(viewLifecycleOwner)
        { isNetwork ->
            if (isNetwork!!) {
                showError("NetworkNotAvailable")
                onFailureCountryList("Network Not Available")
            }
        }
        if (isNetworkAvailable()) {
            viewModel?.loadCountryList()
        } else {
            showError("NetworkNotAvailable")
            onFailureCountryList("Network Not Available")
        }
        return mCountryListView
    }

    fun showProgress() {
        animationLoader?.visibility = View.VISIBLE
    }

    fun hideProgress() {
        animationLoader?.visibility = View.GONE
    }

    fun onSuccessCountrylistLoaded(countryList: ArrayList<CountriesApi?>?) {
        val linearLayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_country_list?.layoutManager = linearLayoutManager
        mRestaurantListAdapter = getContext()?.let { CountiresListAdapter(countryList, it) }
        rv_country_list?.adapter = mRestaurantListAdapter
    }

    fun onFailureCountryList(message: String?) {
        val countrieslist = ArrayList<CountriesApi?>()
        val linearLayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_country_list?.layoutManager = linearLayoutManager
        mRestaurantListAdapter = getContext()?.let { CountiresListAdapter(countrieslist, it) }
        rv_country_list?.adapter = mRestaurantListAdapter
    }

    fun showError(ErrorMsg: String) {
        showAlertDialogBox("", ErrorMsg)
    }

    fun showCountryDetailsPage(commonName: String?) {
//        val intent = Intent(context, CountryDetailsActivity::class.java)
//        intent.putExtra("CountryName", commonName)
//        context?.startActivity(intent)
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

    private fun isNetworkAvailable(): Boolean{
        try{
            if (context != null) {
                val connectivity = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (connectivity != null) {
                    val info = connectivity.activeNetworkInfo
                    if (info != null && info.isConnected) {
                        return true
                    }
                }
            }
            return false
        } catch (e: IllegalArgumentException) {
            return false
        }
    }
}