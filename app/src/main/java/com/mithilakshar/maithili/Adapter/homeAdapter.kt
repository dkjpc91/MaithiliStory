package com.mithilakshar.maithili.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mithilakshar.maithili.Model.homeData
import com.mithilakshar.maithili.databinding.HomeItemBinding

class homeAdapter(val list: ArrayList<homeData>):RecyclerView.Adapter<homeAdapter.homeViewholder>() {

    class homeViewholder(val binding: HomeItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(model: homeData){

            binding.textView.text=model.name

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): homeViewholder {
        val binding=HomeItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return homeViewholder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: homeViewholder, position: Int) {
        val currentdata=list.get(position)

        if (currentdata != null) {
            holder.bind(currentdata)
        }
    }
}