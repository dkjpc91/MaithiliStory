package com.mithilakshar.maithili.UI.Activity




import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.View

import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import com.mithilakshar.maithili.Utility.VideoPlayerHelper
import androidx.activity.enableEdgeToEdge
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
    private lateinit var VideoPlayerHelper: VideoPlayerHelper
    private var isPaused: Boolean = false
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
        videoPlayerUtil = videoPlayerUtil(this, lifecycle, binding.youtubePlayerView)

        videoPlayerUtil.initializePlayer("SBfPs-PMGTA")


        val includedView = binding.sheet
        val includedLayoutBinding = BottomsheetBinding.bind(includedView)
        includedLayoutBinding.fullscreenButton.setOnClickListener {
            requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
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