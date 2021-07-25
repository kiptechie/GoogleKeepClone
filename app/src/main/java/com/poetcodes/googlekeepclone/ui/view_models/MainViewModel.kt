package com.poetcodes.googlekeepclone.ui.view_models

import androidx.lifecycle.*
import com.poetcodes.googlekeepclone.di.ExecutorServiceModule
import com.poetcodes.googlekeepclone.repository.DataState
import com.poetcodes.googlekeepclone.repository.MainRepository
import com.poetcodes.googlekeepclone.repository.models.entities.Archive
import com.poetcodes.googlekeepclone.repository.models.entities.Draft
import com.poetcodes.googlekeepclone.repository.models.entities.Note
import com.poetcodes.googlekeepclone.repository.models.entities.Trash
import com.poetcodes.googlekeepclone.ui.MainStateEvent
import com.poetcodes.googlekeepclone.utils.HelpersUtil
import com.poetcodes.googlekeepclone.utils.NoteEntityUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val executorServiceModule: ExecutorServiceModule,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _notesDataState: MutableLiveData<DataState<List<Note>>> = MutableLiveData()
    val notesDataState: LiveData<DataState<List<Note>>> get() = _notesDataState

    private val _archivesDataState: MutableLiveData<DataState<List<Archive>>> = MutableLiveData()
    val archivesDataState: LiveData<DataState<List<Archive>>> get() = _archivesDataState

    private val _draftsDataState: MutableLiveData<DataState<List<Draft>>> = MutableLiveData()
    val draftsDataState: LiveData<DataState<List<Draft>>> get() = _draftsDataState

    private val _trashDataState: MutableLiveData<DataState<List<Trash>>> = MutableLiveData()
    val trashDataState: LiveData<DataState<List<Trash>>> get() = _trashDataState

    fun setStateEvent(mainStateEvent: MainStateEvent) {
        viewModelScope.launch {
            when (mainStateEvent) {

                is MainStateEvent.FetchNotesEvents -> {
                    mainRepository.fetchNotes()
                        .onEach { noteDataState ->
                            _notesDataState.value = noteDataState
                        }
                        .launchIn(viewModelScope)
                }

                is MainStateEvent.FetchArchivesEvents -> {
                    mainRepository.fetchArchives()
                        .onEach { archiveDataState ->
                            _archivesDataState.value = archiveDataState
                        }
                        .launchIn(viewModelScope)
                }

                is MainStateEvent.FetchDraftsEvents -> {
                    mainRepository.fetchDrafts()
                        .onEach { draftDataState ->
                            _draftsDataState.value = draftDataState
                        }
                        .launchIn(viewModelScope)
                }

                is MainStateEvent.FetchTrashEvents -> {
                    mainRepository.fetchTrash()
                        .onEach { trashDataState ->
                            _trashDataState.value = trashDataState
                        }
                        .launchIn(viewModelScope)
                }

                is MainStateEvent.None -> {
                    //Ha! Gotcha
                }
            }
        }
    }

    fun addNote(note: Note) {
        executorServiceModule.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.addNote(note)
                setStateEvent(MainStateEvent.FetchNotesEvents)
            }
        }
    }

    fun deleteNote(note: Note) {
        executorServiceModule.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.deleteNote(note)
                val noteEntityUtil = NoteEntityUtil(note)
                val deletedNote = noteEntityUtil.note
                val dateNow = Date(System.currentTimeMillis())
                deletedNote.updatedAt = HelpersUtil.dateMapper.toString(dateNow)
                deletedNote.deletedAt = HelpersUtil.dateMapper.toString(dateNow)
                val trash = Trash(
                    null,
                    deletedNote
                )
                addTrash(trash)
                setStateEvent(MainStateEvent.FetchNotesEvents)
            }
        }
    }

    fun restoreNote(trash: Trash) {
        executorServiceModule.withSingleThread().submit {
            viewModelScope.launch {
                val noteEntityUtil = NoteEntityUtil(trash)
                val noteToRestore = noteEntityUtil.note
                addNote(noteToRestore)
                deleteTrash(trash)
            }
        }
    }

    fun updateNote(note: Note) {
        executorServiceModule.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.updateNote(note)
                setStateEvent(MainStateEvent.FetchNotesEvents)
            }
        }
    }

    fun deleteAllNotes() {
        executorServiceModule.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.deleteAllNotes()
                setStateEvent(MainStateEvent.FetchNotesEvents)
            }
        }
    }

    fun addArchive(archive: Archive) {
        executorServiceModule.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.addArchive(archive)
                val noteEntityUtil = NoteEntityUtil(archive)
                val archivedNote = noteEntityUtil.note
                val dateNow = Date(System.currentTimeMillis())
                archivedNote.updatedAt = HelpersUtil.dateMapper.toString(dateNow)
                deleteNote(archivedNote)
                setStateEvent(MainStateEvent.FetchArchivesEvents)
            }
        }
    }

    fun restoreArchive(archive: Archive) {
        executorServiceModule.withSingleThread().submit {
            viewModelScope.launch {
                val noteEntityUtil = NoteEntityUtil(archive)
                val archivedNote = noteEntityUtil.note
                val dateNow = Date(System.currentTimeMillis())
                archivedNote.updatedAt = HelpersUtil.dateMapper.toString(dateNow)
                addNote(archivedNote)
                setStateEvent(MainStateEvent.FetchArchivesEvents)
            }
        }
    }

    fun deleteArchive(archive: Archive) {
        executorServiceModule.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.deleteArchive(archive)
                setStateEvent(MainStateEvent.FetchArchivesEvents)
            }
        }
    }

    fun updateArchive(archive: Archive) {
        executorServiceModule.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.updateArchive(archive)
                setStateEvent(MainStateEvent.FetchArchivesEvents)
            }
        }
    }

    fun deleteAllArchives() {
        executorServiceModule.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.deleteAllArchives()
                setStateEvent(MainStateEvent.FetchArchivesEvents)
            }
        }
    }

    fun addDraft(draft: Draft) {
        executorServiceModule.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.addDraft(draft)
                setStateEvent(MainStateEvent.FetchDraftsEvents)
            }
        }
    }

    fun deleteDraft(draft: Draft) {
        executorServiceModule.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.deleteDraft(draft)
                setStateEvent(MainStateEvent.FetchDraftsEvents)
            }
        }
    }

    fun updateDraft(draft: Draft) {
        executorServiceModule.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.updateDraft(draft)
                setStateEvent(MainStateEvent.FetchDraftsEvents)
            }
        }
    }

    fun deleteAllDrafts() {
        executorServiceModule.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.deleteAllDrafts()
                setStateEvent(MainStateEvent.FetchDraftsEvents)
            }
        }
    }

    private fun addTrash(trash: Trash) {
        executorServiceModule.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.addTrash(trash)
                setStateEvent(MainStateEvent.FetchTrashEvents)
            }
        }
    }

    fun deleteTrash(trash: Trash) {
        executorServiceModule.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.deleteTrash(trash)
                setStateEvent(MainStateEvent.FetchTrashEvents)
            }
        }
    }

    fun deleteAllTrash() {
        executorServiceModule.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.deleteAllTrash()
                setStateEvent(MainStateEvent.FetchTrashEvents)
            }
        }
    }

}