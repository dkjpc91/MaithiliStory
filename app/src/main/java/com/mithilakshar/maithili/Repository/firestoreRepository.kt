package com.mithilakshar.maithili.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
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


}
