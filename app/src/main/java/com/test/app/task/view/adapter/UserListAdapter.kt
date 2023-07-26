package com.test.app.task.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.test.app.R
import com.test.app.databinding.UserListRowDetailsBinding
import com.test.app.task.data.dataSets.VoucherDetails


class UserListAdapter(dataModelList: ArrayList<VoucherDetails?>?, ctx: Context?) : RecyclerView.Adapter<UserListAdapter.ViewHolder>(), CustomClickListener {
    private var dataModelList: ArrayList<VoucherDetails?>? = null
    private var context: Context? = null

    init {
        this.dataModelList = dataModelList
        context = ctx
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: UserListRowDetailsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.user_list_row_details, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel: VoucherDetails? = dataModelList?.get(position)!!
        holder.bind(dataModel)
        holder.countryRowBinding.setItemClickListener(this)
    }


    override fun getItemCount(): Int {
        return dataModelList!!.size
    }

    class ViewHolder(countryRowBinding: UserListRowDetailsBinding) :
        RecyclerView.ViewHolder(countryRowBinding.getRoot()) {
        var countryRowBinding: UserListRowDetailsBinding

        init {
            this.countryRowBinding = countryRowBinding
        }

        fun bind(obj: Any?) {
            //itemRowBinding.setVariable(BR.countrylistViewModel, obj)
            countryRowBinding.model = obj as VoucherDetails?
            countryRowBinding.executePendingBindings()
        }
    }

    override fun itemClicked(f: VoucherDetails?) {
        Toast.makeText(context, "Item clicked " + f?.order_number, Toast.LENGTH_LONG).show()
    }

    override fun btnClicked(f: VoucherDetails?) {
        Toast.makeText(context, "Button clicked " + f?.order_number, Toast.LENGTH_LONG).show()
    }


}