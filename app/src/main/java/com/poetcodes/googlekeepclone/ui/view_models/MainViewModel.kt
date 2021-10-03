package com.poetcodes.googlekeepclone.ui.view_models

import androidx.lifecycle.*
import com.poetcodes.googlekeepclone.repository.DataState
import com.poetcodes.googlekeepclone.repository.MainRepository
import com.poetcodes.googlekeepclone.repository.models.entities.*
import com.poetcodes.googlekeepclone.ui.MainStateEvent
import com.poetcodes.googlekeepclone.ui.fragments.archives.ArchiveAddListener
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

    init {
        cleanNotes()
        cleanLabels()
    }

    fun cleanNotes() {
        viewModelScope.launch {
            val noteDao = mainRepository.noteDao()
            val notes: List<Note> = noteDao.allNotes()
            for (note in notes) {
                if (HelpersUtil.isNewNote(note)) {
                    noteDao.deleteNote(note)
                }
            }
            setStateEvent(MainStateEvent.NoteEvents)
        }
    }

    fun cleanLabels() {
        viewModelScope.launch {
            val labelDao = mainRepository.labelDao()
            val labels: List<Label> = labelDao.allLabels()
            for (label in labels) {
                if (label.name == "") {
                    labelDao.deleteLabel(label)
                }
            }
            setStateEvent(MainStateEvent.LabelEvents)
        }
    }

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

    fun liveNote(id: String): LiveData<Note> {
        return mainRepository.liveNote(id)
    }

    fun addLabel(label: Label) {
        viewModelScope.launch {
            mainRepository.addLabel(label)
            setStateEvent(MainStateEvent.LabelEvents)
        }
    }

    fun updateLabel(label: Label) {
        viewModelScope.launch {
            mainRepository.updateLabel(label)
            setStateEvent(MainStateEvent.LabelEvents)
        }
    }

    fun deleteLabel(label: Label) {
        viewModelScope.launch {
            mainRepository.deleteLabel(label)
            setStateEvent(MainStateEvent.LabelEvents)
        }
    }

    fun deleteAllLabels() {
        viewModelScope.launch {
            mainRepository.deleteAllLabels()
            setStateEvent(MainStateEvent.LabelEvents)
        }
    }

    private fun addNote(note: Note) {
        viewModelScope.launch {
            mainRepository.addNote(note)
            setStateEvent(MainStateEvent.NoteEvents)
        }
    }

    interface NoteFetchListener {
        fun onNoteFetch(note: Note)
    }

    fun fetchNote(newNote: Note, listener: NoteFetchListener) {
        viewModelScope.launch {
            mainRepository.addNote(newNote)
            listener.onNoteFetch(newNote)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            mainRepository.deleteNote(note)
            val noteEntityUtil = NoteEntityUtil(note)
            val deletedNote = noteEntityUtil.note
            val dateNow = Date(System.currentTimeMillis())
            deletedNote.updatedAt = HelpersUtil.dateMapperInstance().toString(dateNow)
            deletedNote.deletedAt = HelpersUtil.dateMapperInstance().toString(dateNow)
            val trash = Trash(
                null,
                deletedNote
            )
            addTrash(trash)
            setStateEvent(MainStateEvent.NoteEvents)
        }
    }

    fun restoreNote(trash: Trash) {
        viewModelScope.launch {
            val noteEntityUtil = NoteEntityUtil(trash)
            val noteToRestore = noteEntityUtil.note
            addNote(noteToRestore)
            deleteTrash(trash)
        }
    }

    fun executorsInstance(): MyExecutors {
        return myExecutors
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            mainRepository.updateNote(note)
            setStateEvent(MainStateEvent.NoteEvents)
        }
    }

    fun pinNote(note: Note?) {
        if (note != null) {
            viewModelScope.launch {
                note.isPinned = !note.isPinned
                mainRepository.updateNote(note)
                setStateEvent(MainStateEvent.NoteEvents)
            }
        }
    }

    fun pinNotes(notes: List<Note>) {
        viewModelScope.launch {
            for (note in notes) {
                note.isPinned = true
                mainRepository.updateNote(note)
            }
            setStateEvent(MainStateEvent.NoteEvents)
        }

    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            mainRepository.deleteAllNotes()
            setStateEvent(MainStateEvent.NoteEvents)
        }
    }

    fun addArchive(archive: Archive) {
        viewModelScope.launch {
            mainRepository.addArchive(archive)
            val noteEntityUtil = NoteEntityUtil(archive)
            val archivedNote = noteEntityUtil.note
            val dateNow = Date(System.currentTimeMillis())
            archivedNote.updatedAt = HelpersUtil.dateMapperInstance().toString(dateNow)
            deleteNote(archivedNote)
            setStateEvent(MainStateEvent.ArchiveEvents)
            setStateEvent(MainStateEvent.NoteEvents)
        }
    }

    fun addArchive(archive: Archive, listener: ArchiveAddListener) {
        viewModelScope.launch {
            mainRepository.addArchive(archive)
            val noteEntityUtil = NoteEntityUtil(archive)
            val archivedNote = noteEntityUtil.note
            val dateNow = Date(System.currentTimeMillis())
            archivedNote.updatedAt = HelpersUtil.dateMapperInstance().toString(dateNow)
            deleteNote(archivedNote)
            setStateEvent(MainStateEvent.ArchiveEvents)
            executorsInstance().withHandler().post {
                listener.archiveAdded(archive)
            }
        }
    }

    fun unarchive(note: Note) {
        viewModelScope.launch {
            val archives = mainRepository.archivesList()
            for (archive in archives) {
                if (archive.note.id == note.id) {
                    mainRepository.deleteArchive(archive);
                }
            }
            val dateNow = Date(System.currentTimeMillis())
            note.updatedAt = HelpersUtil.dateMapperInstance().toString(dateNow)
            addNote(note)
            setStateEvent(MainStateEvent.ArchiveEvents)
            setStateEvent(MainStateEvent.NoteEvents)
        }
    }

    fun deleteArchive(archive: Archive) {
        viewModelScope.launch {
            mainRepository.deleteArchive(archive)
            setStateEvent(MainStateEvent.ArchiveEvents)
        }
    }

    fun updateArchive(note: Note) {
        viewModelScope.launch {
            val archives = mainRepository.archivesList()
            for (archive in archives) {
                if (archive.note.id == note.id) {
                    archive.note = note
                    mainRepository.updateArchive(archive)
                }
            }
            setStateEvent(MainStateEvent.ArchiveEvents)
        }
    }

    fun deleteAllArchives() {
        viewModelScope.launch {
            mainRepository.deleteAllArchives()
            setStateEvent(MainStateEvent.ArchiveEvents)
        }
    }

    fun addDraft(draft: Draft) {
        viewModelScope.launch {
            mainRepository.addDraft(draft)
            setStateEvent(MainStateEvent.DraftEvents)
        }
    }

    fun deleteDraft(draft: Draft) {
        viewModelScope.launch {
            mainRepository.deleteDraft(draft)
            setStateEvent(MainStateEvent.DraftEvents)
        }
    }

    fun updateDraft(draft: Draft) {
        viewModelScope.launch {
            mainRepository.updateDraft(draft)
            setStateEvent(MainStateEvent.DraftEvents)
        }
    }

    fun deleteAllDrafts() {
        viewModelScope.launch {
            mainRepository.deleteAllDrafts()
            setStateEvent(MainStateEvent.DraftEvents)
        }
    }

    private fun addTrash(trash: Trash) {
        viewModelScope.launch {
            mainRepository.addTrash(trash)
            setStateEvent(MainStateEvent.TrashEvents)
        }
    }

    fun deleteTrash(trash: Trash) {
        viewModelScope.launch {
            mainRepository.deleteTrash(trash)
            setStateEvent(MainStateEvent.TrashEvents)
        }
    }

    fun deleteAllTrash() {
        viewModelScope.launch {
            mainRepository.deleteAllTrash()
            setStateEvent(MainStateEvent.TrashEvents)
        }
    }

}