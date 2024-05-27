package com.mithilakshar.maithili.UI.Activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.Toast
import com.mithilakshar.maithili.Utility.AudioPlayer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.imageview.ShapeableImageView
import com.mithilakshar.maithili.R
import com.mithilakshar.maithili.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity(){
    lateinit var binding:ActivityPlayerBinding
    lateinit var AudioPlayer:AudioPlayer
    private lateinit var seekBar: SeekBar
    private val handler = Handler(Looper.getMainLooper())
    private var isPaused: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityPlayerBinding.inflate(layoutInflater)
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

        // Set up media player and SeekBar max value

        seekBar=binding.seekBar
        AudioPlayer = AudioPlayer(applicationContext)
        AudioPlayer.prepareAndPlayMedia("https://onlinetestcase.com/wp-content/uploads/2023/06/1-MB-MP3.mp3", startImmediately = true) {
            seekBar.max = AudioPlayer.duration


        }

        updateSeekBar()


        AudioPlayer.setProgressListener { progress ->

            seekBar.progress = progress

         }






        AudioPlayer.setCompletionListener {


            Toast.makeText(this, "Media playback completed!", Toast.LENGTH_SHORT).show()

        }





        binding.pauseButton.setOnClickListener {

            if (isPaused) {
                // Set to play state
                binding.pauseButton.setImageResource(R.drawable.pause)
                binding.lottie.visibility = LottieAnimationView.VISIBLE
                binding.songImage.visibility = ShapeableImageView.GONE

                AudioPlayer.AudioPlayerResume() // Assuming you have this method to resume playing
            } else {
                // Set to pause state
                binding.pauseButton.setImageResource(R.drawable.play)
                binding.lottie.visibility = LottieAnimationView.GONE
                binding.songImage.visibility = ShapeableImageView.VISIBLE
                AudioPlayer.AudioPlayerPause()
            }
            isPaused = !isPaused


        }

    }

    override fun onResume() {
        super.onResume()
        AudioPlayer.AudioPlayerResume()
    }



    override fun onPause() {
        super.onPause()
        AudioPlayer.AudioPlayerPause()
    }


    private fun updateSeekBar() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                // Update the SeekBar with the current position
                val currentPosition1 = AudioPlayer.currentPosition
                val duration = AudioPlayer.duration
                if (duration > 0) {
                    val progress = (currentPosition1 * 100) / duration
                    seekBar.progress = progress
                }
                // Schedule the next update
                handler.postDelayed(this, 500)
            }
        }, 500)
    }



    override fun onDestroy() {
        super.onDestroy()
        AudioPlayer.AudioPlayerRelease()
        handler.removeCallbacksAndMessages(null)
    }





}