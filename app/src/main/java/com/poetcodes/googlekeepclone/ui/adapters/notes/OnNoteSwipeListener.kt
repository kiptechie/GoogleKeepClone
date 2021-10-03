package com.poetcodes.googlekeepclone.ui.adapters.notes

import com.poetcodes.googlekeepclone.repository.models.entities.Note

interface OnNoteSwipeListener {

    fun archiveNote(note: Note)

}