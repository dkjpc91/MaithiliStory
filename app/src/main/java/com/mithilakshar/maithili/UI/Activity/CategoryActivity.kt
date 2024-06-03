package com.mithilakshar.maithili.UI.Activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryActivity : AppCompatActivity() {
    lateinit var binding : ActivityCategoryBinding

    val repository: firestoreRepository = firestoreRepository()
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

        val receivedText = intent.getStringExtra("dataKey")
        val avKey = intent.getStringExtra("avKey")

        lifecycleScope.launch(Dispatchers.IO){


            var playerDataList = repository.playerData(receivedText.toString())
            var adapter= categoryAdapter(playerDataList,applicationContext, avKey.toString())
            withContext(Dispatchers.Main) { // Switching to the Main dispatcher to update UI
                binding.categoryRecycler.adapter = adapter
                // Hide progress bar after data is fetched (optional)
                // binding.progressBar.visibility = View.GONE
            }

        }

        binding.backBTN.setOnClickListener {
            finish()

            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back press event here
                // For example, show a confirmation dialog or exit the activity
                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)








    }
}