package com.mithilakshar.maithili.UI.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mithilakshar.maithili.R
import com.mithilakshar.maithili.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val handler= Handler(Looper.getMainLooper())
        handler.postDelayed( object : Runnable{
            override fun run() {

                val intent= Intent(applicationContext,MainActivity::class.java)
                startActivity(intent)
                finish()

            }
        }, 2000)
    }
}