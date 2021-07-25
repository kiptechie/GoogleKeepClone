package com.poetcodes.googlekeepclone.repository.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.poetcodes.googlekeepclone.repository.models.entities.Trash

@Dao
interface TrashDao {

    @Query("SELECT * FROM trash")
    fun liveTrash(): LiveData<List<Trash>>

    @Query("SELECT * FROM trash")
    suspend fun allTrash(): List<Trash>

    @Query("SELECT * FROM trash WHERE id =:id")
    suspend fun findTrashById(id: Int): Trash

    @Query("SELECT * FROM trash WHERE id =:id")
    fun findTrashByIdLive(id: Int): LiveData<Trash>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrash(trash: Trash)

    @Delete
    suspend fun deleteTrash(trash: Trash)

    @Query("DELETE FROM trash")
    suspend fun deleteAll()
}