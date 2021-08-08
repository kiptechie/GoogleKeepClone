package com.poetcodes.googlekeepclone.repository

import androidx.lifecycle.LiveData
import com.poetcodes.googlekeepclone.repository.database.dao.*
import com.poetcodes.googlekeepclone.repository.models.entities.*
import kotlinx.coroutines.flow.flow

class MainRepository(
    private val archiveDao: ArchiveDao,
    private val draftDao: DraftDao,
    private val noteDao: NoteDao,
    private val trashDao: TrashDao,
    private val labelDao: LabelDao
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

    fun liveNote(id: Int): LiveData<Note> {
        return noteDao.findNoteByIdLive(id)
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

    suspend fun fetchLabels() = flow {
        emit(DataState.Loading)
        try {
            val labels = labelDao.allLabels()
            emit(DataState.Success(labels))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    suspend fun addLabel(label: Label) {
        labelDao.addLabel(label)
    }

    suspend fun deleteLabel(label: Label) {
        labelDao.deleteLabel(label)
    }

    suspend fun updateLabel(label: Label) {
        labelDao.updateLabel(label)
    }

    suspend fun deleteAllLabels() {
        labelDao.deleteAll()
    }

}