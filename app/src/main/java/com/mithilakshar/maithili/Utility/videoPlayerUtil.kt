package com.mithilakshar.maithili.Utility

import android.content.Context
import androidx.lifecycle.Lifecycle
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class videoPlayerUtil(
    private val context: Context,
    private val lifecycle: Lifecycle,
    private val youTubePlayerView: YouTubePlayerView,

    ) {

    var youTubePlayer: YouTubePlayer? = null

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