package com.mithilakshar.maithili.UI.Activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.mithilakshar.maithili.Adapter.homeAdapter
import com.mithilakshar.maithili.Model.homeData
import com.mithilakshar.maithili.R
import com.mithilakshar.maithili.Repository.firestoreRepository
import com.mithilakshar.maithili.Utility.InAppUpdate
import com.mithilakshar.maithili.Utility.NetworkDialog
import com.mithilakshar.maithili.Utility.NetworkLiveCheck
import com.mithilakshar.maithili.ViewModel.homeViewModel
import com.mithilakshar.maithili.databinding.ActivityHomeBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    val repository: firestoreRepository= firestoreRepository()
    private val inAppUpdateManager: InAppUpdate by lazy {
        InAppUpdate(this, MY_REQUEST_CODE)
    }
    private val MY_REQUEST_CODE = 123

    val viewModelhome: homeViewModel by lazy {
        ViewModelProvider(this).get(homeViewModel::class.java)
    }

    private lateinit var networkLiveCheck: NetworkLiveCheck

    var bannerurls: List<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)



        lifecycleScope.launch {

            bannerurls=viewModelhome.getBannerurlList("a")

            val random = Random.nextInt(bannerurls.size)

            Glide.with(this@MainActivity).load(bannerurls.get(random)).into(binding.homeBanner)

            var list: List<homeData> = listOf()

            list= viewModelhome.homeData()

            val adapter=homeAdapter(list,this@MainActivity,repository)
            binding.homeRecycler.adapter=adapter

            binding.homeBanner.setOnClickListener {

                    val intent= Intent(this@MainActivity, CategoryActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    this@MainActivity.startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

            }

        }

        networkLiveCheck = NetworkLiveCheck(this)
        val networkStatusDialog = NetworkDialog(this)
        networkLiveCheck.observe(this, Observer{

           if (!it)
            {networkStatusDialog.show() }
            else
            {
                networkStatusDialog.dismiss()
            }
        })

    }





    override fun onResume() {
        super.onResume()

        inAppUpdateManager.checkForAppUpdate()
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        inAppUpdateManager.handleActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }


}