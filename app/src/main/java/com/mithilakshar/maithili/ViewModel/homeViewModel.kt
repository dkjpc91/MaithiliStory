package com.mithilakshar.maithili.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mithilakshar.maithili.Repository.firestoreRepository

class homeViewModel(): ViewModel() {


    val firestoreRepository= firestoreRepository()


    suspend fun getBannerurlList(path:String): List<String> {
        return firestoreRepository.getBannerUrlList(path)
    }


}