package com.poetcodes.googlekeepclone.ui.adapters.notes

import com.poetcodes.googlekeepclone.repository.models.entities.Note
import com.poetcodes.googlekeepclone.ui.fragments.notes.NotesFragment

class NoteAdapterSwipeListener(private val notesFragment: NotesFragment) : OnNoteSwipeListener {
    override fun archiveNote(note: Note) {
        notesFragment.onNoteSwipe(note)
    }
}