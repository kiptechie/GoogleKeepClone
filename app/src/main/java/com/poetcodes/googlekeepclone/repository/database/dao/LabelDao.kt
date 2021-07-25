package com.poetcodes.googlekeepclone.repository.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.poetcodes.googlekeepclone.repository.models.entities.Label

@Dao
interface LabelDao {

    @Query("SELECT * FROM labels")
    fun liveLabels(): LiveData<List<Label>>

    @Query("SELECT * FROM labels")
    suspend fun allLabels(): List<Label>

    @Query("SELECT * FROM labels WHERE id =:id")
    suspend fun findLabelById(id: Int): Label

    @Query("SELECT * FROM labels WHERE id =:id")
    fun findLabelByIdLive(id: Int): LiveData<Label>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLabel(label: Label)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLabel(label: Label)

    @Delete
    suspend fun deleteLabel(label: Label)

    @Query("DELETE FROM drafts")
    suspend fun deleteAll()

}