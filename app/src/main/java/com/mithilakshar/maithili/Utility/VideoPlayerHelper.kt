package com.mithilakshar.maithili.Utility

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class VideoPlayerHelper(
    private val context: Context,
    private val lifecycle: Lifecycle,
    private val youTubePlayerView: YouTubePlayerView,
    private val fullScreenViewContainer: FrameLayout,

    ) {

    private var youTubePlayer: YouTubePlayer? = null
    private var isFullscreen = false

    fun initializePlayer(videoId: String) {


        val iFramePlayerOptions = IFramePlayerOptions.Builder()
            .controls(1)
            .fullscreen(1)
            .build()

        youTubePlayerView.enableAutomaticInitialization = false

        youTubePlayerView.addFullscreenListener(object : FullscreenListener {
            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                isFullscreen = true

                fullScreenViewContainer.addView(fullscreenView)


            }

            override fun onExitFullscreen() {
                isFullscreen = false

                fullScreenViewContainer.removeAllViews()


            }
        })
        lifecycle.addObserver(youTubePlayerView)
        youTubePlayerView.initialize(object : AbstractYouTubePlayerListener() {
            override fun onReady(player: YouTubePlayer) {
                youTubePlayer = player
                youTubePlayer?.loadVideo(videoId, 0f)
            }
        }, iFramePlayerOptions)
    }


    fun loadVideo(videoId: String) {
        youTubePlayer?.loadVideo(videoId, 0f)
    }





    fun releaseYouTubePlayer() {
        youTubePlayerView.release()
    }
}


