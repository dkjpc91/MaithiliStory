package com.mithilakshar.maithili.UI.Activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.LinearLayout
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
import com.mithilakshar.maithili.databinding.BottomsheetBinding

class PlayerActivity : AppCompatActivity(){
    lateinit var binding:ActivityPlayerBinding
    lateinit var AudioPlayer:AudioPlayer
    private lateinit var seekBar: SeekBar
    private var isUserSeeking = false
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

        seekBar = binding.seekBar
        AudioPlayer = AudioPlayer(applicationContext)
        AudioPlayer.prepareAndPlayMedia("https://commondatastorage.googleapis.com/codeskulptor-demos/DDR_assets/Sevish_-__nbsp_.mp3", startImmediately = true) {
            seekBar.max = AudioPlayer.duration


        }



        AudioPlayer.setProgressListener { progress ->

            if (!isUserSeeking) { // Update SeekBar progress only if user is not currently seeking

            }

         }


        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2) { // Check if SeekBar change is initiated by user
                    AudioPlayer.AudioPlayerSeekto(p1)// Seek audio to the new position
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }


        })

        updateSeekBar()






        AudioPlayer.setCompletionListener {


            //Toast.makeText(this, "Media playback completed!", Toast.LENGTH_SHORT).show()

        }

        val includedView = binding.sheet
        val includedLayoutBinding = BottomsheetBinding.bind(includedView)

        includedLayoutBinding.playbutton .setOnClickListener {
            toggleButton(includedLayoutBinding)
        }


        binding.pauseButton.setOnClickListener {

            toggleButton(includedLayoutBinding)


        }

    }

    private fun toggleButton(includedLayoutBinding: BottomsheetBinding) {
        if (isPaused) {
            // Set to play state
            binding.pauseButton.setImageResource(R.drawable.pause)
            binding.lottie.visibility = LottieAnimationView.VISIBLE
            binding.songImage.visibility = ShapeableImageView.GONE
            binding.seekbarlayout.visibility=LinearLayout.VISIBLE
            includedLayoutBinding.playbutton.setImageResource(R.drawable.pause)
            AudioPlayer.AudioPlayerResume() // Assuming you have this method to resume playing
        } else {
            // Set to pause state
            binding.pauseButton.setImageResource(R.drawable.play)
            binding.lottie.visibility = LottieAnimationView.GONE
            binding.songImage.visibility = ShapeableImageView.VISIBLE
            binding.seekbarlayout.visibility=LinearLayout.INVISIBLE
            includedLayoutBinding.playbutton.setImageResource(R.drawable.play)
            AudioPlayer.AudioPlayerPause()
        }
        isPaused = !isPaused
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

                if (!isUserSeeking){
                    if (duration > 0) {
                        seekBar.max = duration // Update SeekBar max value if necessary
                        seekBar.progress = currentPosition1
                    }
                }

                if (duration > 0) {
                    if (!AudioPlayer.mediaPlayer?.isPlaying!!) {
                        binding.pauseButton.setImageResource(R.drawable.play)
                        binding.lottie.visibility = LottieAnimationView.GONE
                        binding.songImage.visibility = ShapeableImageView.VISIBLE
                        // performSomeAction()
                    }
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