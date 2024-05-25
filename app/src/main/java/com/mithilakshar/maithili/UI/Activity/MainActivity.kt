package com.mithilakshar.maithili.UI.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.mithilakshar.maithili.Adapter.homeAdapter
import com.mithilakshar.maithili.Model.homeData
import com.mithilakshar.maithili.R
import com.mithilakshar.maithili.ViewModel.homeViewModel
import com.mithilakshar.maithili.databinding.ActivityHomeBinding
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    val viewModelhome: homeViewModel by lazy {
        ViewModelProvider(this).get(homeViewModel::class.java)
    }


    var bannerurls: List<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomeBinding.inflate(layoutInflater)

        setContentView(R.layout.activity_main)

        val handler= Handler(Looper.getMainLooper())
        handler.postDelayed( object : Runnable{
            override fun run() {
                setContentView(binding.root)
            }
        }, 2000)

        lifecycleScope.launch {

            bannerurls=viewModelhome.getBannerurlList("a")

            val random = Random.nextInt(bannerurls.size)

            Glide.with(this@MainActivity).load(bannerurls.get(random)).into(binding.homeBanner)

            var list: List<homeData> = listOf()

            list= viewModelhome.homeData()

            val adapter=homeAdapter(list,applicationContext)
            binding.homeRecycler.adapter=adapter

            binding.homeBanner.setOnClickListener {

                    val intent= Intent(this@MainActivity, PlayerActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    this@MainActivity.startActivity(intent)

            }

        }

















    }
}