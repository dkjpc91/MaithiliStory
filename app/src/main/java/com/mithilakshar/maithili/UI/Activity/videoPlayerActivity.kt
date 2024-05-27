package com.mithilakshar.maithili.UI.Activity


import android.os.Bundle
import com.mithilakshar.maithili.Utility.VideoPlayerHelper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mithilakshar.maithili.R

import com.mithilakshar.maithili.Utility.videoPlayerUtil
import com.mithilakshar.maithili.databinding.ActivityVideoPlayerBinding


class videoPlayerActivity : AppCompatActivity() {

    lateinit var binding: ActivityVideoPlayerBinding
    private lateinit var videoPlayerUtil: videoPlayerUtil
    private lateinit var VideoPlayerHelper: VideoPlayerHelper

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
        videoPlayerUtil = videoPlayerUtil(this, lifecycle, binding.youtubePlayerView,binding.fullScreenViewContainer)


       videoPlayerUtil.initializePlayer("SBfPs-PMGTA")















    }

    public override fun onDestroy() {
        super.onDestroy()
        videoPlayerUtil.releaseYouTubePlayer()
    }
}