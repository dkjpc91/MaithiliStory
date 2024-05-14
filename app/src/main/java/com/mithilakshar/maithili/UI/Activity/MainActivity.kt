package com.mithilakshar.maithili.UI.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.mithilakshar.maithili.R
import com.mithilakshar.maithili.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val handler= Handler(Looper.getMainLooper())
        handler.postDelayed( object : Runnable{
            override fun run() {
                setContentView(R.layout.activity_home)
            }
        }, 2000)



    }
}