package com.mithilakshar.maithili.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mithilakshar.maithili.Model.playerData
import com.mithilakshar.maithili.R
import com.mithilakshar.maithili.UI.Activity.PlayerActivity
import com.mithilakshar.maithili.UI.Activity.videoPlayerActivity
import com.mithilakshar.maithili.databinding.CategoryItemBinding

class categoryAdapter(val list: List<playerData>, val context: Context,val avKey: String): RecyclerView.Adapter<categoryAdapter.categoryAdapterViewholder>() {

    class categoryAdapterViewholder(val binding: CategoryItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(model: playerData, context: Context,avKey: String){

            Glide.with(context).load(model.playerImage).into(binding.mvideoImage)
            binding.mVideoTitle.text=model.playerName
            binding.root.setOnClickListener {


                if (avKey.isNotEmpty()&& (avKey=="1")){
                    val intent= Intent(context, PlayerActivity::class.java)
                    intent.putExtra("playerUrl",model.playerUrl)
                    intent.putExtra("playerName",model.playerName)
                    intent.putExtra("playerImage",model.playerImage)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                    if (context is Activity) {
                        context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
                else{


                    val intent= Intent(context, videoPlayerActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra("playerUrl",model.playerUrl)
                    intent.putExtra("playerName",model.playerName)
                    intent.putExtra("playerImage",model.playerImage)
                    context.startActivity(intent)
                    if (context is Activity) {
                        context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
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
        holder.bind(currenetdata,context,avKey)
    }

}