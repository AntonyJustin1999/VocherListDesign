package com.test.app.loadmaps.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.test.app.R
import com.test.app.databinding.CountryListRowDetailsBinding
import com.test.app.loadmaps.data.dataSets.CountriesApi
import com.test.app.loadmaps.view.CountryDetailsActivity


class CountriesListAdapter(dataModelList: ArrayList<CountriesApi?>?, ctx: Context?) : RecyclerView.Adapter<CountriesListAdapter.ViewHolder>(), CustomClickListener {
    private var dataModelList: ArrayList<CountriesApi?>? = null
    private var context: Context? = null

    init {
        this.dataModelList = dataModelList
        context = ctx
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: CountryListRowDetailsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.country_list_row_details, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel: CountriesApi = dataModelList?.get(position)!!
        holder.bind(dataModel)
        holder.countryRowBinding.setItemClickListener(this)
    }


    override fun getItemCount(): Int {
        return dataModelList!!.size
    }

    class ViewHolder(countryRowBinding: CountryListRowDetailsBinding) :
        RecyclerView.ViewHolder(countryRowBinding.getRoot()) {
        var countryRowBinding: CountryListRowDetailsBinding

        init {
            this.countryRowBinding = countryRowBinding
        }

        fun bind(obj: Any?) {
            //itemRowBinding.setVariable(BR.countrylistViewModel, obj)
            countryRowBinding.model = obj as CountriesApi
            countryRowBinding.executePendingBindings()
        }
    }

    override fun itemClicked(f: CountriesApi?) {
        //Toast.makeText(context, "You clicked " + f?.name, Toast.LENGTH_LONG).show()
        val intent = Intent(context, CountryDetailsActivity::class.java)
        intent.putExtra("CountryName", f?.name?.common)
        context?.startActivity(intent)
    }

    //@BindingAdapter("bind:imageUrl")
    @BindingAdapter("{bind:imageUrl}")
    fun bindImage(imgView: ImageView, imgUrl: String?) {
        imgUrl?.let {
            val imgUri =
                imgUrl.toUri().buildUpon().scheme("https").build()
            Glide.with(imgView.context)
                .load(imgUri)
                .apply(
                    RequestOptions()
                    .placeholder(R.drawable.baseline_refresh_24)
                    .error(R.drawable.baseline_person_24))
                .into(imgView)
        }
    }

}