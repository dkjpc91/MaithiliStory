package com.mithilakshar.maithili.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mithilakshar.maithili.Model.homeData
import com.mithilakshar.maithili.R
import com.mithilakshar.maithili.Repository.firestoreRepository
import com.mithilakshar.maithili.UI.Activity.CategoryActivity
import com.mithilakshar.maithili.UI.Activity.PlayerActivity
import com.mithilakshar.maithili.UI.Activity.videoPlayerActivity
import com.mithilakshar.maithili.databinding.HomeItemBinding
import com.mithilakshar.maithili.databinding.HomeItemRcBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class homeAdapter(val list: List<homeData>,val context: Context,val repository: firestoreRepository):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class homeViewholder(val binding: HomeItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(model: homeData,context: Context,repository: firestoreRepository){

            binding.textView.text=model.name
            Glide.with(context).load(model.homeImageUrl).into(binding.imageView)

            binding.root.setOnClickListener {

                if (model.avKey.isNotEmpty()&& (model.avKey=="1")){
                    val intent= Intent(context, PlayerActivity::class.java)
                    intent.putExtra("playerUrl",model.playerUrl)
                    intent.putExtra("playerName",model.name)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                    if (context is Activity) {
                        context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
                else{


                    val intent= Intent(context, videoPlayerActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra("playerUrl",model.playerUrl)
                    intent.putExtra("playerName",model.name)
                    context.startActivity(intent)
                    if (context is Activity) {
                        context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }

            }

        }

    }

    class homeViewholderRC(val binding: HomeItemRcBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(model: homeData,context: Context,repository: firestoreRepository){

            binding.mCategoryTitle.text=model.name


            CoroutineScope(Dispatchers.Main).launch {
                val playerDataList = repository.playerData(model.dataKey)
                val homeNestedAdapter = homeNestedAdapter(playerDataList,context,model.avKey)
                binding.homeNestedRecycler.adapter=homeNestedAdapter
            }
            binding.mSeeAllBtn.setOnClickListener{


                val intent= Intent(context, CategoryActivity::class.java)
                intent.putExtra("dataKey",model.dataKey)
                intent.putExtra("avKey",model.avKey)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                if (context is Activity) {
                    context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }

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

            1->{(holder as homeViewholder).bind(currentdata,context,repository)}
            else->{(holder as homeViewholderRC).bind(currentdata,context,repository)}
        }



    }


}