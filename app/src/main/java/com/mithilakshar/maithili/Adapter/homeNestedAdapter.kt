package com.mithilakshar.maithili.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mithilakshar.maithili.Model.nesteddata
import com.mithilakshar.maithili.R
import com.mithilakshar.maithili.UI.Activity.PlayerActivity
import com.mithilakshar.maithili.UI.Activity.videoPlayerActivity
import com.mithilakshar.maithili.databinding.HomenesteditemBinding

class homeNestedAdapter(
    private val list: List<nesteddata>,
    private val context: Context,
) : RecyclerView.Adapter<homeNestedAdapter.homeNestedViewholder>() {

    inner class homeNestedViewholder(val binding: HomenesteditemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: nesteddata, context: Context) {
            // Load the image using Glide
            Glide.with(context).load(model.imageurl).into(binding.imageView)

            // Set click listener for the item
            binding.root.setOnClickListener {
                val intent = if (model.avkey.isNotEmpty() && model.avkey == "1") {
                    Intent(context, PlayerActivity::class.java).apply {
                        putExtra("playerUrl", model.videourl) // Assuming videourl is used as playerUrl
                        putExtra("playerName", model.name)
                        putExtra("playerImage", model.imageurl)
                    }
                } else {
                    Intent(context, videoPlayerActivity::class.java).apply {
                        putExtra("playerUrl", model.videourl) // Assuming videourl is used as playerUrl
                        putExtra("playerName", model.name)
                        putExtra("playerImage", model.imageurl)
                    }
                }

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)

                if (context is Activity) {
                    context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): homeNestedViewholder {
        val binding = HomenesteditemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return homeNestedViewholder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: homeNestedViewholder, position: Int) {
        val currentdata = list[position]
        holder.bind(currentdata, context)
    }
}
