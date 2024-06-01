package com.mithilakshar.maithili.UI.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.mithilakshar.maithili.Adapter.categoryAdapter
import com.mithilakshar.maithili.Model.playerData
import com.mithilakshar.maithili.R
import com.mithilakshar.maithili.Repository.firestoreRepository
import com.mithilakshar.maithili.databinding.ActivityCategoryBinding
import kotlinx.coroutines.launch

class CategoryActivity : AppCompatActivity() {
    lateinit var binding : ActivityCategoryBinding

    var playerDataList= listOf<playerData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val repository: firestoreRepository = firestoreRepository()
        val receivedText = intent.getStringExtra("dataKey")
        val avKey = intent.getStringExtra("avKey")

        lifecycleScope.launch {

            playerDataList = receivedText?.let { repository.playerData(it) }!!
            val adapter= categoryAdapter(playerDataList,applicationContext, avKey.toString())
            binding.categoryRecycler.adapter=adapter
        }


    }
}