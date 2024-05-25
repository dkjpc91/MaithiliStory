package com.mithilakshar.maithili.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mithilakshar.maithili.Model.homeData
import com.mithilakshar.maithili.UI.Activity.PlayerActivity
import com.mithilakshar.maithili.databinding.CategoryItemBinding

class categoryAdapter(val list: ArrayList<homeData>, val context: Context): RecyclerView.Adapter<categoryAdapter.categoryAdapterViewholder>() {

    class categoryAdapterViewholder(val binding: CategoryItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(model: homeData, context: Context){

            binding.root.setOnClickListener {


                val intent= Intent(context, PlayerActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): categoryAdapterViewholder {
        var binding= CategoryItemBinding.inflate( LayoutInflater.from(parent.context), parent, false)
        return categoryAdapterViewholder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: categoryAdapterViewholder, position: Int) {
        val currenetdata=list.get(position)
        holder.bind(currenetdata,context)
    }

}