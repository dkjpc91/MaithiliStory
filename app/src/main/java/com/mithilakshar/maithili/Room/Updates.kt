package com.mithilakshar.maithili.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "files")
data class Updates(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fileName: String,
    var uniqueString: String
)
