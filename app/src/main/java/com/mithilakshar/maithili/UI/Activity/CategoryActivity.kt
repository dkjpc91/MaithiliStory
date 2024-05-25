package com.mithilakshar.maithili.UI.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mithilakshar.maithili.Adapter.categoryAdapter
import com.mithilakshar.maithili.Adapter.homeAdapter
import com.mithilakshar.maithili.Model.homeData
import com.mithilakshar.maithili.R
import com.mithilakshar.maithili.databinding.ActivityCategoryBinding

class CategoryActivity : AppCompatActivity() {
    lateinit var binding : ActivityCategoryBinding
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


        var list: ArrayList<homeData> = arrayListOf()

        list.add(homeData("apple",1))
        list.add(homeData("ball",2))
        list.add(homeData("cAT",2))


        val adapter= categoryAdapter(list,applicationContext)
        binding.categoryRecycler.adapter=adapter
    }
}