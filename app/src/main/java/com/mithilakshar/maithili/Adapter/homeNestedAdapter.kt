package com.mithilakshar.maithili.Adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mithilakshar.maithili.Model.homeData
import com.mithilakshar.maithili.UI.Activity.CategoryActivity
import com.mithilakshar.maithili.UI.Activity.PlayerActivity
import com.mithilakshar.maithili.databinding.HomeItemBinding
import com.mithilakshar.maithili.databinding.HomenesteditemBinding

class homeNestedAdapter(val list: ArrayList<homeData>, val context: Context): RecyclerView.Adapter<homeNestedAdapter.homeNestedViewholder>() {

    class homeNestedViewholder(val binding: HomenesteditemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(model: homeData,context: Context){

            binding.root.setOnClickListener {

                val intent= Intent(context,PlayerActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): homeNestedViewholder {
        var binding= HomenesteditemBinding.inflate( LayoutInflater.from(parent.context), parent, false)
        return homeNestedViewholder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: homeNestedViewholder, position: Int) {
        val currentdata=list.get(position)
        return holder.bind(currentdata,context)
    }


}