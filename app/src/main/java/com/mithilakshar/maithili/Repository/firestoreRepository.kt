package com.mithilakshar.maithili.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.mithilakshar.maithili.Model.homeData
import com.mithilakshar.maithili.Model.playerData
import kotlinx.coroutines.tasks.await

class firestoreRepository {


    val db = FirebaseFirestore.getInstance()

    suspend fun getBannerUrlList(documentId: String): List<String> {
        val documentSnapshot = db.collection("banner").document(documentId).get().await()
        return if (documentSnapshot.exists()) {
            documentSnapshot.data?.values?.map { it.toString() } ?: emptyList()
        } else {
            emptyList()
        }
    }

    suspend fun homeData(): List<homeData> {
        val db = FirebaseFirestore.getInstance()
        val documentSnapshot = db.collection("homeData").get().await()
        return documentSnapshot.toObjects(homeData::class.java)

    }

    suspend fun playerData(path:String): List<playerData> {
        val db = FirebaseFirestore.getInstance()
        val documentSnapshot = db.collection(path).get().await()
        return documentSnapshot.toObjects(playerData::class.java)

    }


}
