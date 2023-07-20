package com.test.app.LoadMaps.view.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.app.LoadMaps.data.dataSets.CountriesApi
import com.test.app.R

class CountiresListAdapter(
    private val mCountryList: ArrayList<CountriesApi?>?,
    private val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 2) {
            CountryViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.country_details, parent, false)
            )
        } else {
            EmptyViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.empty_view, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 2) {
            val countryViewHolder = holder as CountryViewHolder
            countryViewHolder.tv_common_name.text = mCountryList!![position]?.name?.common
            countryViewHolder.tv_official_name.text = mCountryList[position]?.name?.official
            Glide.with(context).load(mCountryList[position]?.flags?.png_url)
                .into(countryViewHolder.iv_icon)
        } else {
            val emptyViewHolder = holder as EmptyViewHolder
            emptyViewHolder.tv_Empty.text = "No Data Found!!"
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (mCountryList != null) {
            if (mCountryList.size > 0) {
                2
            } else {
                1
            }
        } else {
            1
        }
    }

    override fun getItemCount(): Int {
        return if (mCountryList != null) {
            if (mCountryList.size > 0) {
                mCountryList.size
            } else {
                1
            }
        } else {
            1
        }
    }

    inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var tv_common_name: TextView
        var tv_official_name: TextView
        var iv_icon: ImageView
        var layout_country_detail_holder: CardView

        init {
            tv_common_name = itemView.findViewById<View>(R.id.tv_common_name) as TextView
            tv_official_name = itemView.findViewById<View>(R.id.tv_official_name) as TextView
            iv_icon = itemView.findViewById<ImageView>(R.id.iv_icon)
            layout_country_detail_holder =
                itemView.findViewById<CardView>(R.id.layout_country_detail_holder)
            layout_country_detail_holder.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (v.id == R.id.layout_country_detail_holder) {
                //mPresenter.redirectCountryDetails(mCountryList.get(getAdapterPosition()).getName().getCommon());
            }
        }
    }

    inner class EmptyViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val tv_Empty: TextView

        init {
            tv_Empty = itemView.findViewById<TextView>(R.id.tv_empty)
        }
    }
}