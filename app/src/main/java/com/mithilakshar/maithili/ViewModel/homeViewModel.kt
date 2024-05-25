package com.mithilakshar.maithili.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mithilakshar.maithili.Model.homeData
import com.mithilakshar.maithili.Repository.firestoreRepository
import kotlinx.coroutines.tasks.await

class homeViewModel(): ViewModel() {


    val firestoreRepository= firestoreRepository()


    suspend fun getBannerurlList(path:String): List<String> {
        return firestoreRepository.getBannerUrlList(path)
    }

    suspend fun homeData(): List<homeData> {
        return firestoreRepository.homeData()

    }


}