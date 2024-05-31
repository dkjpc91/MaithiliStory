package com.mithilakshar.maithili.UI.Activity

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

    private lateinit var appUpdateManager: AppUpdateManager
    private val updateType=AppUpdateType.IMMEDIATE

    val viewModelhome: homeViewModel by lazy {
        ViewModelProvider(this).get(homeViewModel::class.java)
    }

    private lateinit var networkLiveCheck: NetworkLiveCheck

    var bannerurls: List<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appUpdateManager= AppUpdateManagerFactory.create(applicationContext)
        if (updateType==AppUpdateType.FLEXIBLE){
            appUpdateManager.registerListener(installStateUpdatedListener)
        }
        checkForAppUpdate()


        lifecycleScope.launch {

            bannerurls=viewModelhome.getBannerurlList("a")

            val random = Random.nextInt(bannerurls.size)

            Glide.with(this@MainActivity).load(bannerurls.get(random)).into(binding.homeBanner)

            var list: List<homeData> = listOf()

            list= viewModelhome.homeData()

            val adapter=homeAdapter(list,applicationContext)
            binding.homeRecycler.adapter=adapter

            binding.homeBanner.setOnClickListener {

                    val intent= Intent(this@MainActivity, videoPlayerActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    this@MainActivity.startActivity(intent)

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

    private val installStateUpdatedListener= InstallStateUpdatedListener{
        if (it.installStatus()== InstallStatus.DOWNLOADED){

            Toast.makeText(this,"Download Completed", Toast.LENGTH_LONG).show()
            lifecycleScope.launch {
                delay(5.seconds)
                appUpdateManager.completeUpdate()
            }
        }
    }

    private fun checkForAppUpdate(){

        appUpdateManager.appUpdateInfo.addOnSuccessListener {
            val isUpdateAvailable=it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateAllowed=when(updateType){

                AppUpdateType.IMMEDIATE->{it.isImmediateUpdateAllowed}
                AppUpdateType.FLEXIBLE->{it.isFlexibleUpdateAllowed}
                else->false

            }

            if (isUpdateAvailable && isUpdateAllowed){
                appUpdateManager.startUpdateFlowForResult(
                    it,updateType,this,113
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (updateType==AppUpdateType.IMMEDIATE){
            appUpdateManager.appUpdateInfo.addOnSuccessListener {
                if (it.updateAvailability()==UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS)
                {
                    appUpdateManager.startUpdateFlowForResult(
                        it,updateType,this,113
                    )

                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (updateType==AppUpdateType.FLEXIBLE){
            appUpdateManager.unregisterListener(installStateUpdatedListener)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==113){
            if (resultCode!= RESULT_OK){
                println("Something went wrong updating")
            }
        }
    }


}