package com.mithilakshar.maithili.UI.Activity




import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View

import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.google.android.material.bottomsheet.BottomSheetBehavior

import com.mithilakshar.maithili.R
import com.mithilakshar.maithili.Utility.videoPlayerUtil
import com.mithilakshar.maithili.databinding.ActivityVideoPlayerBinding

import com.mithilakshar.maithili.databinding.BottomsheetBinding


class videoPlayerActivity : AppCompatActivity() {

    lateinit var binding: ActivityVideoPlayerBinding
    private lateinit var videoPlayerUtil: videoPlayerUtil
    private var isPaused: Boolean = false
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        BottomSheetBehavior.from(binding.sheet).apply {

            this.state= BottomSheetBehavior.STATE_COLLAPSED
            this.setPeekHeight(200)


        }

        val playerUrl = intent.getStringExtra("playerUrl")
        val playerName = intent.getStringExtra("playerName")


        binding.videoName.text=playerName


        videoPlayerUtil = videoPlayerUtil(this, lifecycle, binding.youtubePlayerView)

        if (playerUrl != null) {
            videoPlayerUtil.initializePlayer(playerUrl)
        }


        val includedView = binding.sheet
        val includedLayoutBinding = BottomsheetBinding.bind(includedView)
        includedLayoutBinding.fullscreenButton.setOnClickListener {
            requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        includedLayoutBinding.sharebutton.setOnClickListener {

            val intent = Intent(Intent.ACTION_SEND).apply {
                // Replace "your.package.name" with your app's package name

                val appLink = "https://play.google.com/store/apps/details?id=${applicationContext.packageName}"
                putExtra(Intent.EXTRA_TEXT, "मैथिली ऐप के प्रयोग करबाक लेल धन्यवाद! \n\nमैथिली भाषा के प्रचार प्रसार के लेल अहि एप्प के शेयर जरूर करब। गूगल प्ले स्टोर स ऐप डाउनलोड करबाक लेल नीचा देल गेल लिंक पर क्लिक करू। \n \n $appLink")
                type = "text/plain"
            }
            startActivity(Intent.createChooser(intent, "Share App"))

        }

        includedLayoutBinding.playbutton.setOnClickListener {

            if (isPaused) {
                // Set to play state
                videoPlayerUtil.youTubePlayer?.play()
                includedLayoutBinding.playbutton.setImageResource(R.drawable.pause)

            } else {
                // Set to pause state
                videoPlayerUtil.youTubePlayer?.pause()
                includedLayoutBinding.playbutton.setImageResource(R.drawable.play)

            }
            isPaused = !isPaused



        }

        binding.backBtnPA.setOnClickListener {
            finish()

            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back press event here
                // For example, show a confirmation dialog or exit the activity
                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)




















    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
           Toast.makeText(this,"Landscape",Toast.LENGTH_LONG).show()
            binding.sheet.visibility=View.GONE
            binding.topsheet.visibility=View.GONE
            binding.videoName.visibility=View.GONE
            binding.lottie.visibility=View.GONE
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    )
            // Optionally, you can also hide the action bar if it's present
            supportActionBar?.hide()

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this,"Portrait",Toast.LENGTH_LONG).show()
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            // Show the action bar if it's hidden
            supportActionBar?.show()
        }
    }



    public override fun onDestroy() {
        super.onDestroy()
        videoPlayerUtil.releaseYouTubePlayer()
    }

    override fun getOnBackInvokedDispatcher(): OnBackInvokedDispatcher {
        return super.getOnBackInvokedDispatcher()


    }
}