package com.mithilakshar.maithili.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UpdatesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(updates: Updates)

    @Update
    suspend fun update(updates: Updates)

    @Query("SELECT * FROM files")
    suspend fun getAllUpdates(): List<Updates>

    @Query("SELECT * FROM files WHERE id = :id")
    suspend fun findById(id: Int): Updates?

    @Query("SELECT * FROM files WHERE fileName = :fileName")
    suspend fun getFileUpdate(fileName: String): List<Updates>

    @Query("UPDATE files SET uniqueString = :newUniqueString WHERE fileName = :fileName")
    suspend fun updateUniqueString(fileName: String, newUniqueString: String)
}
