package com.poetcodes.googlekeepclone.repository

import com.poetcodes.googlekeepclone.repository.database.dao.ArchiveDao
import com.poetcodes.googlekeepclone.repository.database.dao.DraftDao
import com.poetcodes.googlekeepclone.repository.database.dao.NoteDao
import com.poetcodes.googlekeepclone.repository.database.dao.TrashDao
import com.poetcodes.googlekeepclone.repository.models.entities.Archive
import com.poetcodes.googlekeepclone.repository.models.entities.Draft
import com.poetcodes.googlekeepclone.repository.models.entities.Note
import com.poetcodes.googlekeepclone.repository.models.entities.Trash
import kotlinx.coroutines.flow.flow

class MainRepository(
    private val archiveDao: ArchiveDao,
    private val draftDao: DraftDao,
    private val noteDao: NoteDao,
    private val trashDao: TrashDao
) {

    suspend fun fetchNotes() = flow {
        emit(DataState.Loading)
        // kotlinx.coroutines.delay(1_000)
        try {
            val notes = noteDao.allNotes()
            emit(DataState.Success(notes))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    suspend fun addNote(note: Note) {
        noteDao.addNote(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    suspend fun deleteAllNotes() {
        noteDao.deleteAll()
    }

    suspend fun fetchArchives() = flow {
        emit(DataState.Loading)
        try {
            val archives = archiveDao.allArchives()
            emit(DataState.Success(archives))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    suspend fun addArchive(archive: Archive) {
        archiveDao.addArchive(archive)
    }

    suspend fun deleteArchive(archive: Archive) {
        archiveDao.deleteArchive(archive)
    }

    suspend fun updateArchive(archive: Archive) {
        archiveDao.updateArchive(archive)
    }

    suspend fun deleteAllArchives() {
        archiveDao.deleteAll()
    }

    suspend fun fetchDrafts() = flow {
        emit(DataState.Loading)
        try {
            val drafts = draftDao.allDrafts()
            emit(DataState.Success(drafts))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    suspend fun addDraft(draft: Draft) {
        draftDao.addDraft(draft)
    }

    suspend fun deleteDraft(draft: Draft) {
        draftDao.deleteDraft(draft)
    }

    suspend fun updateDraft(draft: Draft) {
        draftDao.updateDraft(draft)
    }

    suspend fun deleteAllDrafts() {
        draftDao.deleteAll()
    }

    suspend fun fetchTrash() = flow {
        emit(DataState.Loading)
        try {
            val trash = trashDao.allTrash()
            emit(DataState.Success(trash))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    suspend fun addTrash(trash: Trash) {
        trashDao.addTrash(trash)
    }

    suspend fun deleteTrash(trash: Trash) {
        trashDao.deleteTrash(trash)
    }

    suspend fun deleteAllTrash() {
        trashDao.deleteAll()
    }

}