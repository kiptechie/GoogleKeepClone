package com.poetcodes.googlekeepclone.repository.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.poetcodes.googlekeepclone.repository.models.entities.Archive

@Dao
interface ArchiveDao {

    @Query("SELECT * FROM archives")
    suspend fun liveArchives(): LiveData<List<Archive>>

    @Query("SELECT * FROM archives")
    suspend fun allArchives(): List<Archive>

    @Query("SELECT * FROM archives WHERE id =:id")
    suspend fun findArchiveById(id: Int): Archive

    @Query("SELECT * FROM archives WHERE id =:id")
    suspend fun findArchiveByIdLive(id: Int): LiveData<Archive>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addArchive(archive: Archive)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateArchive(archive: Archive)

    @Delete
    suspend fun deleteArchive(archive: Archive)

    @Query("DELETE FROM archives")
    suspend fun deleteAll()

}