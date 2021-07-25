package com.poetcodes.googlekeepclone.repository.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.poetcodes.googlekeepclone.repository.models.entities.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    suspend fun liveNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes")
    suspend fun allNotes(): List<Note>

    @Query("SELECT * FROM notes WHERE id =:id")
    suspend fun findNoteById(id: Int): Note

    @Query("SELECT * FROM notes WHERE id =:id")
    suspend fun findNoteByIdLive(id: Int): LiveData<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: Note)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM notes")
    suspend fun deleteAll()

}