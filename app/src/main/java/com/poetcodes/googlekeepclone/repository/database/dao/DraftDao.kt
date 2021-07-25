package com.poetcodes.googlekeepclone.repository.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.poetcodes.googlekeepclone.repository.models.entities.Draft

@Dao
interface DraftDao {

    @Query("SELECT * FROM drafts")
    suspend fun liveDrafts(): LiveData<List<Draft>>

    @Query("SELECT * FROM drafts")
    suspend fun allDrafts(): List<Draft>

    @Query("SELECT * FROM drafts WHERE id =:id")
    suspend fun findDraftById(id: Int): Draft

    @Query("SELECT * FROM drafts WHERE id =:id")
    suspend fun findDraftByIdLive(id: Int): LiveData<Draft>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDraft(draft: Draft)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateDraft(draft: Draft)

    @Delete
    suspend fun deleteDraft(draft: Draft)

    @Query("DELETE FROM drafts")
    suspend fun deleteAll()
}