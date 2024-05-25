package com.mithilakshar.maithili.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mithilakshar.maithili.Model.homeData
import com.mithilakshar.maithili.UI.Activity.CategoryActivity
import com.mithilakshar.maithili.UI.Activity.PlayerActivity
import com.mithilakshar.maithili.databinding.HomeItemBinding
import com.mithilakshar.maithili.databinding.HomeItemRcBinding

class homeAdapter(val list: List<homeData>,val context: Context):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class homeViewholder(val binding: HomeItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(model: homeData,context: Context){

            binding.textView.text=model.name

            binding.root.setOnClickListener {


                val intent= Intent(context, PlayerActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }

        }

    }

    class homeViewholderRC(val binding: HomeItemRcBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(model: homeData,context: Context){

            var list= arrayListOf<homeData>()

            list.add(homeData("apple",1))
            list.add(homeData("ball",2))
            list.add(homeData("cAT",2))
            list.add(homeData("dog",0))
            list.add(homeData("apple",1))
            list.add(homeData("ball",2))
            list.add(homeData("cAT",2))
            list.add(homeData("dog",0))

            val homeNestedAdapter = homeNestedAdapter(list,context)
            binding.homeNestedRecycler.adapter=homeNestedAdapter

            binding.mSeeAllBtn.setOnClickListener{


                val intent= Intent(context, CategoryActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }



    }

    override fun getItemViewType(position:Int):Int{
        return list.get(position).viewType
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            1 -> {
                homeViewholder(
                    HomeItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                homeViewholderRC(
                    HomeItemRcBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentdata=list.get(position)
        when(currentdata.viewType){

            1->{(holder as homeViewholder).bind(currentdata,context)}
            else->{(holder as homeViewholderRC).bind(currentdata,context)}
        }



    }


}