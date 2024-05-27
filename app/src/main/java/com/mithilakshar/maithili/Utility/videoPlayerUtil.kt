package com.mithilakshar.maithili.Utility

import android.content.Context
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class videoPlayerUtil(
    private val context: Context,
    private val lifecycle: Lifecycle,
    private val youTubePlayerView: YouTubePlayerView,
    private val fullScreenViewContainer: FrameLayout
) {

    private var youTubePlayer: YouTubePlayer? = null
    private var isFullscreen = false

    fun initializePlayer(videoId: String) {
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(player: YouTubePlayer) {
                youTubePlayer = player
                youTubePlayer?.loadVideo(videoId, 0f)
            }
        })
    }





    fun loadVideo(videoId: String) {
        youTubePlayer?.loadVideo(videoId, 0f)
    }

    fun releaseYouTubePlayer() {
        youTubePlayerView.release()
    }

}