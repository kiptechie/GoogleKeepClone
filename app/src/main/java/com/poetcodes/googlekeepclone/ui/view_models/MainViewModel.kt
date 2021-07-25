package com.poetcodes.googlekeepclone.ui.view_models

import androidx.lifecycle.*
import com.poetcodes.googlekeepclone.repository.DataState
import com.poetcodes.googlekeepclone.repository.MainRepository
import com.poetcodes.googlekeepclone.repository.models.entities.*
import com.poetcodes.googlekeepclone.ui.MainStateEvent
import com.poetcodes.googlekeepclone.utils.HelpersUtil
import com.poetcodes.googlekeepclone.utils.MyExecutors
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
    private val myExecutors: MyExecutors,
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

    private val _labelDataState: MutableLiveData<DataState<List<Label>>> = MutableLiveData()
    val labelDataState: LiveData<DataState<List<Label>>> get() = _labelDataState

    fun setStateEvent(mainStateEvent: MainStateEvent) {
        viewModelScope.launch {
            when (mainStateEvent) {

                is MainStateEvent.NoteEvents -> {
                    mainRepository.fetchNotes()
                        .onEach { noteDataState ->
                            _notesDataState.value = noteDataState
                        }
                        .launchIn(viewModelScope)
                }

                is MainStateEvent.ArchiveEvents -> {
                    mainRepository.fetchArchives()
                        .onEach { archiveDataState ->
                            _archivesDataState.value = archiveDataState
                        }
                        .launchIn(viewModelScope)
                }

                is MainStateEvent.DraftEvents -> {
                    mainRepository.fetchDrafts()
                        .onEach { draftDataState ->
                            _draftsDataState.value = draftDataState
                        }
                        .launchIn(viewModelScope)
                }

                is MainStateEvent.TrashEvents -> {
                    mainRepository.fetchTrash()
                        .onEach { trashDataState ->
                            _trashDataState.value = trashDataState
                        }
                        .launchIn(viewModelScope)
                }

                is MainStateEvent.LabelEvents -> {
                    mainRepository.fetchLabels()
                        .onEach { labelDataState ->
                            _labelDataState.value = labelDataState
                        }
                        .launchIn(viewModelScope)
                }

                is MainStateEvent.None -> {
                    //Ha! Gotcha
                }
            }
        }
    }

    fun addLabel(label: Label) {
        myExecutors.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.addLabel(label)
                setStateEvent(MainStateEvent.LabelEvents)
            }
        }
    }

    fun updateLabel(label: Label) {
        myExecutors.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.updateLabel(label)
                setStateEvent(MainStateEvent.LabelEvents)
            }
        }
    }

    fun deleteLabel(label: Label) {
        myExecutors.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.deleteLabel(label)
                setStateEvent(MainStateEvent.LabelEvents)
            }
        }
    }

    fun deleteAllLabels() {
        myExecutors.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.deleteAllLabels()
                setStateEvent(MainStateEvent.LabelEvents)
            }
        }
    }

    fun addNote(note: Note) {
        myExecutors.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.addNote(note)
                setStateEvent(MainStateEvent.NoteEvents)
            }
        }
    }

    fun deleteNote(note: Note) {
        myExecutors.withSingleThread().submit {
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
                setStateEvent(MainStateEvent.NoteEvents)
            }
        }
    }

    fun restoreNote(trash: Trash) {
        myExecutors.withSingleThread().submit {
            viewModelScope.launch {
                val noteEntityUtil = NoteEntityUtil(trash)
                val noteToRestore = noteEntityUtil.note
                addNote(noteToRestore)
                deleteTrash(trash)
            }
        }
    }

    fun updateNote(note: Note) {
        myExecutors.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.updateNote(note)
                setStateEvent(MainStateEvent.NoteEvents)
            }
        }
    }

    fun deleteAllNotes() {
        myExecutors.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.deleteAllNotes()
                setStateEvent(MainStateEvent.NoteEvents)
            }
        }
    }

    fun addArchive(archive: Archive) {
        myExecutors.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.addArchive(archive)
                val noteEntityUtil = NoteEntityUtil(archive)
                val archivedNote = noteEntityUtil.note
                val dateNow = Date(System.currentTimeMillis())
                archivedNote.updatedAt = HelpersUtil.dateMapper.toString(dateNow)
                deleteNote(archivedNote)
                setStateEvent(MainStateEvent.ArchiveEvents)
            }
        }
    }

    fun restoreArchive(archive: Archive) {
        myExecutors.withSingleThread().submit {
            viewModelScope.launch {
                val noteEntityUtil = NoteEntityUtil(archive)
                val archivedNote = noteEntityUtil.note
                val dateNow = Date(System.currentTimeMillis())
                archivedNote.updatedAt = HelpersUtil.dateMapper.toString(dateNow)
                addNote(archivedNote)
                setStateEvent(MainStateEvent.ArchiveEvents)
            }
        }
    }

    fun deleteArchive(archive: Archive) {
        myExecutors.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.deleteArchive(archive)
                setStateEvent(MainStateEvent.ArchiveEvents)
            }
        }
    }

    fun updateArchive(archive: Archive) {
        myExecutors.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.updateArchive(archive)
                setStateEvent(MainStateEvent.ArchiveEvents)
            }
        }
    }

    fun deleteAllArchives() {
        myExecutors.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.deleteAllArchives()
                setStateEvent(MainStateEvent.ArchiveEvents)
            }
        }
    }

    fun addDraft(draft: Draft) {
        myExecutors.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.addDraft(draft)
                setStateEvent(MainStateEvent.DraftEvents)
            }
        }
    }

    fun deleteDraft(draft: Draft) {
        myExecutors.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.deleteDraft(draft)
                setStateEvent(MainStateEvent.DraftEvents)
            }
        }
    }

    fun updateDraft(draft: Draft) {
        myExecutors.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.updateDraft(draft)
                setStateEvent(MainStateEvent.DraftEvents)
            }
        }
    }

    fun deleteAllDrafts() {
        myExecutors.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.deleteAllDrafts()
                setStateEvent(MainStateEvent.DraftEvents)
            }
        }
    }

    private fun addTrash(trash: Trash) {
        myExecutors.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.addTrash(trash)
                setStateEvent(MainStateEvent.TrashEvents)
            }
        }
    }

    fun deleteTrash(trash: Trash) {
        myExecutors.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.deleteTrash(trash)
                setStateEvent(MainStateEvent.TrashEvents)
            }
        }
    }

    fun deleteAllTrash() {
        myExecutors.withSingleThread().submit {
            viewModelScope.launch {
                mainRepository.deleteAllTrash()
                setStateEvent(MainStateEvent.TrashEvents)
            }
        }
    }

}