package com.mithilakshar.maithili.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mithilakshar.maithili.Model.homeData
import com.mithilakshar.maithili.Model.nesteddata
import com.mithilakshar.maithili.R
import com.mithilakshar.maithili.UI.Activity.CategoryActivity
import com.mithilakshar.maithili.UI.Activity.PlayerActivity
import com.mithilakshar.maithili.UI.Activity.videoPlayerActivity
import com.mithilakshar.maithili.Utility.dbDownloadersequenceurl
import com.mithilakshar.maithili.Utility.dbHelper
import com.mithilakshar.maithili.databinding.HomeItemBinding
import com.mithilakshar.maithili.databinding.HomeItemRcBinding

class HomeAdapter(
    private val homeList: List<homeData>,
    private val context: Context,
    private val dbHelper: dbHelper,
    private val dbDownloadersequenceurl: dbDownloadersequenceurl,
    private val isUpdateNeeded: String,
    private val nestedDataProvider: (Int) -> List<nesteddata> // Function to get nested data for a specific position
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val nestedDataMap = mutableMapOf<Int, List<nesteddata>>()

    fun updateNestedData(position: Int, nestedData: List<nesteddata>) {
        nestedDataMap[position] = nestedData
        notifyItemChanged(position)
    }


    inner class HomeViewHolder(private val binding: HomeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: homeData) {
            binding.root.visibility = if (model.viewtype == "0") View.GONE else View.VISIBLE
            binding.textView.text = model.name
            Glide.with(context)
                .load(model.imageurl)
                .placeholder(R.drawable.notificationlogo)
                .into(binding.imageView)

            binding.root.setOnClickListener {
                handleItemClick(model)
            }
        }

        private fun handleItemClick(model: homeData) {
            val intent = when {
                model.avkey.isNotEmpty() && model.avkey == "1" -> {
                    Intent(context, PlayerActivity::class.java).apply {
                        putExtra("playerUrl", model.avkey)
                        putExtra("playerName", model.name)
                        putExtra("playerImage", model.avkey)
                    }
                }
                else -> {
                    Intent(context, videoPlayerActivity::class.java).apply {
                        putExtra("playerUrl", model.avkey)
                        putExtra("playerName", model.name)
                    }
                }
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

            if (context is Activity) {
                context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    inner class HomeViewHolderRC(private val binding: HomeItemRcBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var nestedAdapter: homeNestedAdapter

        fun bind(model: homeData) {
            binding.root.visibility = View.VISIBLE
            binding.mCategoryTitle.text = model.name

            // Set up nested RecyclerView
            binding.homeNestedRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val nestedData = nestedDataProvider(adapterPosition)
            nestedAdapter = homeNestedAdapter(nestedData, context)
            binding.homeNestedRecycler.adapter = nestedAdapter

            binding.mSeeAllBtn.setOnClickListener {
                val intent = Intent(context, CategoryActivity::class.java).apply {
                    putExtra("dataKey", model.avkey)
                    putExtra("avKey", model.avkey)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)

                if (context is Activity) {
                    context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return homeList[position].avkey.toInt()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> HomeViewHolder(
                HomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> HomeViewHolderRC(
                HomeItemRcBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        return homeList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentData = homeList[position]

        when (currentData.viewtype) {
            "1" -> (holder as HomeViewHolder).bind(currentData)
            else -> (holder as HomeViewHolderRC).bind(currentData)
        }
    }
}
